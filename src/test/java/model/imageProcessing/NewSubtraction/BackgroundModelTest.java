package model.imageProcessing.NewSubtraction;

import model.imageProcessing.imageTypes.ImageGray;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Andrew on 07/07/17.
 */
public class BackgroundModelTest {

        @Test
        public void initCardsTest_Manual(){
                BufferedImage testImage = null;
                try {
                    testImage = ImageIO.read(new File("D:\\Users\\Andrew\\Dropbox\\Projects\\+Java\\Projects\\AlgorithmPolygon\\src\\main\\resources\\background.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (testImage == null ){
                    System.out.println("null");
                    return;
                }

                BackgroundModel backgroundModel = new BackgroundModel(new ImageGray(testImage),20);

            System.out.println("lol");
        }
}
