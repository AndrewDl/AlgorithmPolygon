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
import model.imageProcessing.NeuralObject;
import model.imageProcessing.NewSubtraction.AvgPixelFill;
import model.imageProcessing.NewSubtraction.NewBGSubtractor;
import model.imageProcessing.RWFile;
import model.imageProcessing.Web;
import model.imageProcessing.Weight;
import model.imageProcessing.imageTypes.ImageBin;
import model.imageProcessing.imageTypes.ImageGray;
import model.imageProcessing.imageTypes.NVImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    ArrayList<int[][]> images = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();

    public void initialize(URL location, ResourceBundle resources) {

        Weight weight = new Weight("TestHuman.txt", 80, 80);

        ArrayList<int[]> rects = new ArrayList<>();
        ArrayList<NeuralObject> objects = new ArrayList<>();


        sliderMatchingThreshold.setValue(40);
        sliderRequiredMatches.setValue(13);
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
        CameraImageJavaCV camera = new CameraImageJavaCV("rtsp://admin:skymallcamera1@109.251.217.182:30001/h264/ch01/sub/av_stream");

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
                try {
                    BufferedImage camImage = camera.getBufferedImage();
                    ImageBin result = subtractor.getSubtractedImage(new ImageGray(camImage),null);
                    AvgPixelFill filter = new AvgPixelFill();
                    BufferedImage filterResult = filter.AvarageFilter(result).toBufferedImage();


                    fps = fps+1;
//*****************************************************************************

                    images.add(result.clone().toPixelArray());
                    names.add("test1.jpg");
                    for (int i = 0; i < images.get(0).length ; i++) {
                        for (int j = 0; j < images.get(0)[0].length; j++) {
                            Color c = new Color(images.get(0)[i][j]);
                            if(c.getRed()>125)
                                images.get(0)[i][j]=1;
                            else
                                images.get(0)[i][j]=0;
                        }
                    }
                    objects.removeAll(objects);
                    BufferedImage bi = result.toBufferedImage();
                    Graphics g =bi.createGraphics();
                    int objHeight = weight.getWeight().length;
                    int objWidth = weight.getWeight()[0].length;
                    //System.out.println("*******" + objHeight + "*" + objWidth);


                    int[][] tempRect = new int[objHeight][objWidth];
                    for (int k = objHeight - 1; k < images.get(0).length; k += 10) {
                        for (int l = objWidth - 1; l < images.get(0)[0].length; l += 10) {
                            for (int i = 0 + k - (objHeight - 1); i < objHeight + k - (objHeight - 1); i++) {
                                for (int j = 0 + l - (objWidth - 1); j < objWidth + l - (objWidth - 1); j++) {
                                    tempRect[i - k + (objHeight - 1)][j - l + (objWidth - 1)] = images.get(0)[i][j];
                                }
                            }
                            //System.out.println(l+"///"+k);
                            Web NW1 = new Web(tempRect.length, tempRect[0].length, tempRect);
                            NW1.weight = weight.getWeight();


                            NW1.mul_w();
                            NW1.Sum();
                            if (NW1.Rez()) {
                                objects.add(new NeuralObject(k - (objHeight - 1), l - (objWidth - 1), objHeight, objWidth, NW1.sum, 40));

                                //System.out.println(" - True, Sum = " + NW1.sum);
                            } else {
                                //System.out.println(" - False, Sum = "+NW1.sum);

                            }
                        }
                    }
                    boolean repeat = false;

                    for (int j = 0; j < objects.size(); j++) {
                        for (int i = 0; i < objects.size(); i++) {
                            if (Math.sqrt(Math.pow(Math.abs(objects.get(i).getxPos() - objects.get(j).getxPos()), 2) +
                                    Math.pow(Math.abs(objects.get(i).getyPos() - objects.get(j).getyPos()), 2)) < 40 ) {
                                if (!(objects.get(i).isRemove() == true && objects.get(j).isRemove() == true)) {
                                    if (objects.get(i).getObjectWeight() > objects.get(j).getObjectWeight() && objects.get(j).isRemove()) {
                                        objects.get(j).setRemove(true);
                                        if (objects.get(i).isRemove() == true)
                                            objects.get(i).setRemove(false);

                                    } else {
                                        objects.get(i).setRemove(true);
                                        if (objects.get(j).isRemove() == true)
                                            objects.get(j).setRemove(false);
                                    }
                                }
                            }
                        }
                    }

                    for (int i = 0; i < objects.size(); i++) {

                        if(objects.get(i).isRemove()==false)
                            g.drawRect(objects.get(i).getxPos(), objects.get(i).getyPos(), objects.get(i).getWidth(), objects.get(i).getHeight());
                    }

                    Platform.runLater(() -> {
                        imageViewOriginal.setImage(SwingFXUtils.toFXImage(camImage,null));
                        imageViewDerivative1.setImage(SwingFXUtils.toFXImage(bi,null));
                        imageViewDerivative2.setImage(SwingFXUtils.toFXImage(filterResult,null));
                    });
                    images.remove(0);
                    names.remove(0);
                } catch (CameraConnectionIssueException e) {
                    e.printStackTrace();
                }

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
        ImageBin image = new ImageBin(SwingFXUtils.fromFXImage(imageViewDerivative1.getImage(),null));

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
}
