package view;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import model.cameras.CameraConnectionIssueException;
import model.cameras.CameraImageJavaCV;
import model.imageProcessing.NewSubtraction.AvgPixelFill;
import model.imageProcessing.NewSubtraction.NewBGSubtractor;
import model.imageProcessing.SceneObject;
import model.imageProcessing.imageTypes.ImageBin;
import model.imageProcessing.imageTypes.ImageGray;
import model.imageProcessing.imageTypes.NVImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    private ImageView imageViewOriginal;
    @FXML
    private ImageView imageViewDerivative1;
    @FXML
    private ImageView imageViewDerivative2;
    @FXML
    private Label labelFPS;

    @FXML
    private Slider sliderMatchingThreshold;
    @FXML
    private Slider sliderRequiredMatches;
    @FXML
    private Slider sliderUpdateFactor;

    @FXML
    private TextField textMatchingThreshold;
    @FXML
    private TextField textRequiredMatches;
    @FXML
    private TextField textUpdateFactor;

    NewBGSubtractor subtractor;

    private int fps = 0;

    public void initialize(URL location, ResourceBundle resources) {

        sliderMatchingThreshold.setValue(40);
        sliderRequiredMatches.setValue(10);
        sliderUpdateFactor.setValue(16);
        textMatchingThreshold.setText(String.valueOf((int)sliderMatchingThreshold.getValue()));
        textRequiredMatches.setText(String.valueOf((int)sliderRequiredMatches.getValue()));
        textUpdateFactor.setText(String.valueOf((int)sliderUpdateFactor.getValue()));

        sliderMatchingThreshold.valueProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                textMatchingThreshold.setText( String.valueOf(newValue.intValue()));
            }
        });
        sliderRequiredMatches.valueProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                textRequiredMatches.setText( String.valueOf(newValue.intValue()));
            }
        });
        sliderUpdateFactor.valueProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                textUpdateFactor.setText( String.valueOf(newValue.intValue()));
            }
        });

        imageViewOriginal.setImage(SwingFXUtils.toFXImage(new BufferedImage(10,10,1),null));
//"rtsp://admin:skymallcamera7@46.219.14.78:30001/h264/ch01/sub/av_stream"
        //"rtsp://admin:dlandre12@192.168.0.64/h264/ch01/sub/av_stream"
        CameraImageJavaCV camera = new CameraImageJavaCV("rtsp://admin:skymallcamera5@46.219.14.65:30001/h264/ch01/sub/av_stream");

        //BufferedImage initialImage = null;

        Thread t = new Thread(() -> {
            BufferedImage initialImage = null;
            try {
                initialImage = camera.getBufferedImage();
            } catch (CameraConnectionIssueException e) {
                e.printStackTrace();
            }
            subtractor = new NewBGSubtractor(new ImageGray(initialImage));

            subtractor.backgroundModel.setMatchingThreshold( Integer.valueOf( textMatchingThreshold.getText() ) );
            subtractor.backgroundModel.setUpdateFactor( Integer.valueOf( textUpdateFactor.getText() ) );
            subtractor.backgroundModel.setRequiredMatches( Integer.valueOf( textRequiredMatches.getText() ) );

            try {
                initialImage = camera.getBufferedImage();
            } catch (CameraConnectionIssueException e) {
                e.printStackTrace();
            }
            while (true){
                BufferedImage camImage = null;

                try {
                    camImage = camera.getBufferedImage();
                } catch (CameraConnectionIssueException e) {
                    e.printStackTrace();
                }

                ImageBin result = subtractor.getSubtractedImage(new ImageGray(camImage),null);

                AvgPixelFill filter = new AvgPixelFill();

                NVImage filterResultImg = filter.AvarageFilter(result);

                BufferedImage filterResult = filterResultImg.toBufferedImage();

                int[][] imageArray = filterResultImg.toPixelArray();

                //List<SceneObject> objects = getObjects(imageArray);

                List<SceneObject> objects = new ArrayList<>();
                getPeople(objects, imageArray);

                Graphics g = filterResult.createGraphics();
                g.setColor(Color.BLUE);
                for (SceneObject object : objects){
                    Rectangle bounds = object.getBounds();
                    g.drawRect(bounds.x,bounds.y,bounds.width,bounds.height);
                }

                g.dispose();


                BufferedImage test = camImage;

                Platform.runLater(() -> {
                    imageViewOriginal.setImage(SwingFXUtils.toFXImage(test,null));
                    imageViewDerivative1.setImage(SwingFXUtils.toFXImage(result.toBufferedImage(),null));
                    imageViewDerivative2.setImage(SwingFXUtils.toFXImage(filterResult,null));
                });
                fps = fps+1;


            }
        });

        Timer timerFPS = new Timer(1000, e -> Platform.runLater(() -> {
            int localFPS = fps;
            labelFPS.setText(String.valueOf(localFPS));

            fps = 0;
        }));

        timerFPS.start();

        t.setDaemon(true);
        t.start();
    }

    public void buttonApplyPressed(ActionEvent actionEvent) {
        subtractor.backgroundModel.setMatchingThreshold( Integer.valueOf( textMatchingThreshold.getText() ) );
        subtractor.backgroundModel.setUpdateFactor( Integer.valueOf( textUpdateFactor.getText() ) );
        subtractor.backgroundModel.setRequiredMatches( Integer.valueOf( textRequiredMatches.getText() ) );
    }

    public void buttonScreenshotPressed(ActionEvent actionEvent) {
        ImageBin image = new ImageBin(SwingFXUtils.fromFXImage(imageViewDerivative2.getImage(),null));

        File folder = new File("shots");

        System.out.println(folder.exists());

        File[] files = folder.listFiles();

        File screenshot = new File("shots/" + (System.currentTimeMillis()) + ".jpg");

        try {
            ImageIO.write(image.toBufferedImage(),"jpg",screenshot);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<SceneObject> getObjects(int[][] image){

        int frameWidth = 50;
        int frameHeight = 50;
        int average = (frameHeight*frameWidth)/5;

        List<SceneObject> objectList = new ArrayList<SceneObject>();

        for (int i = 0; i < image[0].length - frameWidth; i+=30) {
            for (int j = 0; j < image.length - frameHeight; j+=30) {

                int localAverage = 0;

                for (int k = 0; k < frameWidth; k++) {
                    for (int l = 0; l < frameHeight; l++) {
                        int pixel = (image[j+l][i+k] & 0xFF);
                        if ( pixel != 0) {
                            localAverage += 1;
                        }
                    }
                }

                //localAverage = localAverage/(frameHeight*frameWidth);

                if (localAverage >= average){
                    objectList.add(new SceneObject(new Rectangle(j,i,frameWidth,frameHeight),localAverage));
                }

            }
        }

        return objectList;
    }

    //////////////////////////////////////////////////////////////////////////
    //Viola-Jones

    private void getPeople(List<SceneObject> objects, int[][] image){
        int[][] Template;

            File templatePath = new File("templates/5.jpg");
            BufferedImage BufTemp = null;
            try {
                BufTemp = ImageIO.read(templatePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Template = toRGBArray(BufTemp);
            int[][] binarizedImage = Binarize(image);
            int[][] binarizedTemplate = Binarize(Template);
            objects.addAll(catchObject(binarizedTemplate, binarizedImage));

    }

    /**
     * Looks for object at the image
     * @param template
     * @param image
     * @return first coordinates of object
     */
    public List<SceneObject> catchObject(int[][] template, int[][] image){
        int frameWidth = template[0].length;
        int frameHeight = template.length;

        List<SceneObject> objectList = new ArrayList<>();
        double result;

        for(int x = 0; x < image.length-template.length; x+=10){
            for(int y = 0; y < image[0].length-template[0].length; y+=10){
                result = getPercent(template, x, y, image);
                if(result >= 83){

                    objectList.add(new SceneObject(new Rectangle(y,x,frameWidth,frameHeight), (int)result));
                    //System.out.println("Percentage of similar: " + result);
                }
            }
        }
        return objectList;
    }

    /**
     * Compares template to part of image which has size of this template
     * @param template
     * @param x - start x-coordinate of image part
     * @param y - start y-coordinate of image part
     * @param image - the whole image
     * @return percent of coincidence between template and part of image
     */
    public double getPercent(int[][] template, int x, int y, int[][] image){
        double percent;
        int generalQuantity = template.length * template[0].length;
        int quantityOfSimilar = 0;

        for(int i = 0, m = x; i < template.length && m < template.length+x; i++, m++){
            for(int j = 0, n = y; j < template[0].length && n < template[0].length+y; j++, n++){
                if ((template[i][j] == 0 && image[m][n] == 0)) {
                    quantityOfSimilar++;
                } else if ((template[i][j] == 1 && image[m][n] == 1)) {
                    quantityOfSimilar++;
                }
            }
        }
        percent = ((double) quantityOfSimilar/(double) generalQuantity) * 100;
        return percent;
    }

    /**
     * Binarizes image in pixel array to 0 and 1 for the purpose of mapping objects</br>
     * not the same as imageBinarize()<br>
     *     also refreshes binary representation of image that can be accessed by calling <b>getImageBin()</b>
     *
     * @param array to binarize. The result of imageBinarize() method;
     * @return NEW array with 0 and 1
     */
    private int[][] Binarize(int[][] array){
        double BinarizationThreshold = 127;
        int[][] BinarizedImage = new int[array.length][array[0].length];
        int[][] BinarizedImageRGB = new int[array.length][array[0].length];

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                int pixel = array[i][j]&0xFF;
                if (pixel > BinarizationThreshold){
                    BinarizedImage[i][j]=1;
                    BinarizedImageRGB[i][j]=0xFFFFFF;
                } else {
                    BinarizedImage[i][j]=0;
                    BinarizedImageRGB[i][j]=0x000000;
                }
            }
        }
        return BinarizedImage;
    }

    /**
     * Turns Buffered Image into RGB-array
     * @param image - buffered image
     * @return RGB-array
     */
    private int[][] toRGBArray(BufferedImage image){

        int rgbArray[][] = new int[image.getHeight()][image.getWidth()];

        for (int i=0; i < rgbArray.length; i++)
            for (int j=0; j < rgbArray[0].length; j++)
            {
                rgbArray[i][j] = image.getRGB(j,i);
            }
        return rgbArray;
    }

}
