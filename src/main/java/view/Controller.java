package view;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.cameras.CameraConnectionIssueException;
import model.cameras.CameraImageJavaCV;
import model.imageProcessing.NewSubtraction.BackgroundModel;
import model.imageProcessing.NewSubtraction.NewBGSubtractor;
import model.imageProcessing.imageTypes.ImageGray;
import model.imageProcessing.imageTypes.NVImage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    private ImageView imageViewOriginal;
    @FXML
    private ImageView imageViewDerivative1;
    @FXML
    private Label labelFPS;

    private int fps = 0;

    public void initialize(URL location, ResourceBundle resources) {
        imageViewOriginal.setImage(SwingFXUtils.toFXImage(new BufferedImage(10,10,1),null));

        CameraImageJavaCV camera = new CameraImageJavaCV("rtsp://admin:skymallcamera7@46.219.14.78:30001/h264/ch01/sub/av_stream");

        //BufferedImage initialImage = null;

        Thread t = new Thread(() -> {
            BufferedImage initialImage = null;
            try {
                initialImage = camera.getBufferedImage();
            } catch (CameraConnectionIssueException e) {
                e.printStackTrace();
            }
            NewBGSubtractor subtractor = new NewBGSubtractor(new ImageGray(initialImage));

            try {
                initialImage = camera.getBufferedImage();
            } catch (CameraConnectionIssueException e) {
                e.printStackTrace();
            }
            while (true){
                try {
                    BufferedImage camImage = camera.getBufferedImage();
                    NVImage result = subtractor.getSubtractedImage(new ImageGray(camImage),null);
                    imageViewOriginal.setImage(SwingFXUtils.toFXImage(camImage,null));
                    imageViewDerivative1.setImage(SwingFXUtils.toFXImage(result.toBufferedImage(),null));
                    fps = fps+1;

                } catch (CameraConnectionIssueException e) {
                    e.printStackTrace();
                }

            }
        });



        Timer timerFPS = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        int localFPS = fps;
                        labelFPS.setText(String.valueOf(localFPS));

                        fps = 0;
                    }
                });

            }
        });

        timerFPS.start();

        t.start();

    }
}
