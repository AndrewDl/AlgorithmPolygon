package view;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    private ImageView imageViewOriginal;

    public void initialize(URL location, ResourceBundle resources) {
        imageViewOriginal.setImage(SwingFXUtils.toFXImage(new BufferedImage(10,10,1),null));
    }
}
