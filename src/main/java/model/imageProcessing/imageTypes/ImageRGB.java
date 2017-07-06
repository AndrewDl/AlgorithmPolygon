package model.imageProcessing.imageTypes;

import java.awt.image.BufferedImage;

/**
 * This class represents color image <br>
 *     Actually the image is stored in int[][] array, where each element represents pixel value in RGB <br>
 *         This class is used for logical separation of image types that are used by <b>ImageProcessor</b> <br>
 *             technically all <b>NVImages</b> are int[][] arrays
 *
 * Created by Andrew on 20.04.2017.
 */
public class ImageRGB extends NVImage {
    /**
     * Set's a color image from a pixel array. <br>
     *     this constructor works faster than ImageRGB(BufferedImage)
     *
     * @param pixelArray array of pixels with RGB codes.
     */
    public ImageRGB(int[][] pixelArray){
        int[][] result = pixelArray.clone();
        for (int i = 0; i < pixelArray.length ; i++) {
            result[i] = pixelArray[i].clone();
        }
        super.Image = result;
    }

    /**
     * Set's a color from BufferedImage. <br>
     *     this constructor works slower than ImageRGB(int[][])<br>
     *         because it needs to convert BufferedImage to appropriate data to store
     *
     * @param image buffered image
     */
    public ImageRGB(BufferedImage image){
        super.Image = toRGBArray(image);
    }

    /**
     * Copying constrictor for color image
     * @param image color image to copy
     */
    public ImageRGB(ImageRGB image){
        super.Image = image.Image;
    }
}
