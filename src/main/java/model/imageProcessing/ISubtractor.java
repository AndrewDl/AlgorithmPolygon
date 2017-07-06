package model.imageProcessing;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import model.imageProcessing.imageTypes.ImageGray;
import model.imageProcessing.imageTypes.NVImage;

import java.awt.image.BufferedImage;

/**
 * Created by Andrew on 07/06/17.
 */
public interface ISubtractor {

    NVImage getSubtractedImage(@NotNull ImageGray foreground, @Nullable ImageGray background);

}
