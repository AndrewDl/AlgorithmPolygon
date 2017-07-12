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
import model.imageProcessing.NewSubtraction.NewBGSubtractor;
import model.imageProcessing.imageTypes.ImageBin;
import model.imageProcessing.imageTypes.ImageGray;
import model.imageProcessing.imageTypes.NVImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    private ImageView imageViewOriginal;
    @FXML
    private ImageView imageViewDerivative1;
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

        sliderMatchingThreshold.setValue(20);
        sliderRequiredMatches.setValue(2);
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
        CameraImageJavaCV camera = new CameraImageJavaCV("rtsp://admin:skymallcamera8@46.219.14.78:30002/h264/ch01/sub/av_stream");

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
                    NVImage result = subtractor.getSubtractedImage(new ImageGray(camImage),null);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            imageViewOriginal.setImage(SwingFXUtils.toFXImage(camImage,null));
                            imageViewDerivative1.setImage(SwingFXUtils.toFXImage(result.toBufferedImage(),null));
                        }
                    });
                    fps = fps+1;

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
