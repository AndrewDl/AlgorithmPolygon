package model.imageProcessing;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;

/**
 * Created by Andrew on 09/26/16.
 */
public class FXUtils {
    public static BufferedImage fromFXImage (Image wi){

        BufferedImage bufferedImage = new BufferedImage((int)wi.getWidth(),(int)wi.getHeight(),BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < bufferedImage.getWidth() ; i++) {
            for (int j = 0; j < bufferedImage.getHeight() ; j++) {
                int pixel = wi.getPixelReader().getArgb(i,j);

                bufferedImage.setRGB(i,j,pixel);
            }
        }

        return bufferedImage;
    }

    public static WritableImage toFXImage (BufferedImage bufferedImage){

        WritableImage wr = null;

        if (bufferedImage != null) {
            wr = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    pw.setArgb(x, y, bufferedImage.getRGB(x, y));
                }
            }
        }

        return wr;
    }
}
