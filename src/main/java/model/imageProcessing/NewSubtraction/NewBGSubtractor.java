package model.imageProcessing.NewSubtraction;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import model.imageProcessing.ISubtractor;
import model.imageProcessing.imageTypes.ImageGray;
import model.imageProcessing.imageTypes.NVImage;

import java.awt.image.BufferedImage;

/**
 * Created by Andrew on 07/06/17.
 */
public class NewBGSubtractor implements ISubtractor {

    BufferedImage foreground = null;

    @Override
    public NVImage getSubtractedImage(@NotNull ImageGray foreground, @Nullable ImageGray background) {
        BackgroundModel backgroundModel = new BackgroundModel(20,foreground);

        this.foreground = foreground.toBufferedImage();



        return null;
    }
}
