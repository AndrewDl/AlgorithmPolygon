package model.imageProcessing.NewSubtraction;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import model.imageProcessing.ISubtractor;
import model.imageProcessing.imageTypes.ImageBin;
import model.imageProcessing.imageTypes.ImageGray;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Andrew on 07/06/17.
 */
public class NewBGSubtractor implements ISubtractor {

    BufferedImage image;
    BackgroundModel backgroundModel;

    private NewBGSubtractor(){}

    public NewBGSubtractor(ImageGray initialImage){
         backgroundModel = new BackgroundModel(initialImage, 20);
         image = new BufferedImage(initialImage.getWidth(),initialImage.getHeight(),BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public ImageBin getSubtractedImage(@NotNull ImageGray foreground, @Nullable ImageGray background) {

        //init model
        for (int x = 0; x < foreground.getWidth(); x++) {
            for (int y = 0; y < foreground.getHeight(); y++) {
                //long t1 = System.currentTimeMillis();
                if(backgroundModel.matchPixel(x,y,foreground.toPixelArray()[x][y])){
                    image.setRGB(x,y,0xFFFFFF);
                    backgroundModel.update(x,y,foreground.toPixelArray()[x][y]);
                } else {
                    image.setRGB(x,y, 0);
                }
                //long t2 = System.currentTimeMillis();
                //System.out.println("Updated row in: " + (t2-t1));

            }
            //System.out.println(x + "//");
        }

        return new ImageBin(image);
    }
}
