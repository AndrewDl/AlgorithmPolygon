package model.imageProcessing.imageTypes;

import java.awt.image.BufferedImage;

/**
 * This class represents an image in Gray colors <br>
 *     Actually the image is stored in int[][] array, where each element represents pixel value in RGB <br>
 *         This class is used for logical separation of image types that are used by <b>ImageProcessor</b> <br>
 *             technically all <b>NVImages</b> are int[][] arrays
 *
 * Created by Andrew on 20.04.2017.
 */
public class ImageGray extends NVImage {

    /**
     * Set's a GrayImage from a pixel array. <br>
     *     <b><i><u>BE CAREFUL!</u></i></b> if you use this constructor, please ensure that the pixel array contains information is gray scale <br>
     *         There are no checks in the constructor to ensure that the image is gray. <br>
     *             The <u>programmer</u> is responsible for this
     *
     * @param pixelArray array of pixels with RGB codes. <u>Gray scale colors only!</u>
     */
    public ImageGray(int[][] pixelArray){
        int[][] result = pixelArray.clone();
        for (int i = 0; i < pixelArray.length ; i++) {
            result[i] = pixelArray[i].clone();
        }
        super.Image = result;
    }

    /**
     * Set's a GrayImage from BufferedImage. <br>
     *     <b><i><u>BE CAREFUL!</u></i></b> if you use this constructor, please ensure that the image is gray scale <br>
     *         There are no checks in the constructor to ensure that the image is gray. <br>
     *             The <u>programmer</u> is responsible for this
     *
     * @param image buffered image in gray scales
     */
    public ImageGray(BufferedImage image){
        super.Image = toRGBArray(image);
    }

    /**
     * Copying constrictor for Gray image
     * @param image Gray image to copy
     */
    public ImageGray(ImageGray image){
        super.Image = image.Image;
    }
}
