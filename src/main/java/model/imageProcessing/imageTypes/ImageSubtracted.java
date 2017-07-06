package model.imageProcessing.imageTypes;

import java.awt.image.BufferedImage;

/**
 * This class represents an image received by subtraction of pixel values from two images <br>
 *     Actually the image is stored in int[][] array, where each element represents pixel value in RGB <br>
 *         This class is used for logical separation of image types that are used by <b>ImageProcessor</b> <br>
 *             technically all <b>NVImages</b> are int[][] arrays
 *
 * Created by Andrew on 20.04.2017.
 */
public class ImageSubtracted extends NVImage {

    /**
     * Set's a Subtracted image from a pixel array. <br>
     *     <b><i><u>BE CAREFUL!</u></i></b> if you use this constructor, please ensure that the pixel <br>
     *         array contains pixel values of subtracted image <br>
     *              There are no checks in the constructor to ensure that the image a <b>result of subtraction</b>. <br>
     *                  The <u>programmer</u> is responsible for this
     *
     * @param pixelArray array of pixels with RGB codes. <u>Subtracted images only!</u>
     */
    public ImageSubtracted(int[][] pixelArray){
        int[][] result = pixelArray.clone();
        for (int i = 0; i < pixelArray.length ; i++) {
            result[i] = pixelArray[i].clone();
        }
        super.Image = result;
    }

    /**
     * Set's a Subtracted image from BufferedImage. <br>
     *     <b><i><u>BE CAREFUL!</u></i></b> if you use this constructor, please ensure that the <i>image</i> <br>
     *         contains pixel values of subtracted image <br>
     *              There are no checks in the constructor to ensure that the image a <b>result of subtraction</b>. <br>
     *                  The <u>programmer</u> is responsible for this
     *
     * @param image subtracted image
     */
    public ImageSubtracted(BufferedImage image){
        super.Image = toRGBArray(image);
    }

    /**
     * Copying constrictor for Subtracted image
     * @param image Subtracted image to copy
     */
    public ImageSubtracted(ImageSubtracted image){
        super.Image = image.Image;
    }
}