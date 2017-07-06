package model.imageProcessing.imageTypes;

import java.awt.image.BufferedImage;

/**
 * This class represents a Binary image<br>
 *     Actually the image is stored in int[][] array, where each element represents pixel value in RGB <br>
 *         In the case of binarized image RGB values well be 0xFFFFFF(white) or 0x000000(black) <br>
 *             This class is used for logical separation of image types that are used by <b>ImageProcessor</b> <br>
 *                 technically all <b>NVImages</b> are int[][] arrays
 *
 * Created by Andrew on 20.04.2017.
 */
public class ImageBin extends NVImage {

    /**
     * Set's a Binary image from a pixel array. <br>
     *     <b><i><u>BE CAREFUL!</u></i></b> if you use this constructor, please ensure that the pixelArray contains <u>binary</u> image <br>
     *         There are no checks in the constructor to ensure that the image is binary. <br>
     *             The <u>programmer</u> is responsible for this
     *
     * @param pixelArray array of pixels with RGB codes. <u>Gray scale colors only!</u>
     */
    public ImageBin(int[][] pixelArray){
        int[][] result = pixelArray.clone();
        for (int i = 0; i < pixelArray.length ; i++) {
            result[i] = pixelArray[i].clone();
        }
        super.Image = result;
    }

    /**
     * Set's a Binary image from BufferedImage. <br>
     *     <b><i><u>BE CAREFUL!</u></i></b> if you use this constructor, please ensure that that the <i>image</i> <br>
     *         contains pixels of <u>binary</u> image <br>
     *              There are no checks in the constructor to ensure that the image is binary. <br>
     *                  The <u>programmer</u> is responsible for this
     *
     * @param image buffered image that contain binary image
     */
    public ImageBin(BufferedImage image){
        super.Image = toRGBArray(image);
    }

    /**
     * Copying constrictor for binary image
     * @param image Binary image to copy
     */
    public ImageBin(ImageBin image){
        super.Image = image.Image;
    }
}

