package model;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import model.imageProcessing.SceneObject;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by Andrew on 07/05/17.
 */
public interface IObjectDetector {

    /**
     * Method returns the list of objects that were detected on the mainImage
     * @param mainImage image on which we search objects
     * @param additionalImage additional image. Can be used if it is needed. Otherwise should be null;
     * @return list of detected objects on the image
     */
    List<SceneObject> getObjects(@NotNull BufferedImage mainImage, @Nullable BufferedImage additionalImage);

}
