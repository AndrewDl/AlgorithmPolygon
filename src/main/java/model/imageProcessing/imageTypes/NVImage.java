package model.imageProcessing.imageTypes;

import java.awt.image.BufferedImage;

/**
 * This abstract class represents a general image that can be used by ImageProcessor<br>
 *     Actually the image is stored in int[][] array, where each element represents pixel value in RGB <br>
 *         This class is used for extending by other image classes to create new image types that have to be logically different<br>
 *             and can be used by <b>ImageProcessor</b> <br>
     *             technically all <b>NVImages</b> are int[][] arrays
 *
 * Created by Andrew on 20.04.2017.
 */
public abstract class NVImage implements Cloneable{

    /**
     * An array for storing image
     */
    protected int[][] Image = null;

    /**
     * Represents internal data as a BufferedImage
     * @return internal data converted to BufferedImage
     */
    public BufferedImage toBufferedImage(){
        return toBufferedImage(Image);
    }

    /**
     * Converts RGB array to Buffered image
     * @param rgbArray RGB array
     * @return BufferedImage
     */
    protected BufferedImage toBufferedImage(int[][] rgbArray){

        int width = rgbArray.length;
        int height = rgbArray[0].length;

        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                image.setRGB(i,j,rgbArray[i][j]);
        return image;
    }

    /**
     * Represents internal image as an array of pixels
     * @return pixel array by <b>value</b>
     */
    public int[][] toPixelArray(){

        int[][] result = Image.clone();
        for (int i = 0; i < Image.length ; i++) {
            result[i] = Image[i].clone();
        }

        return result;
    }

    /**
     * Converts Buffered image to RGB array
     * @param image image to convert
     * @return RGB array
     */
    protected int[][] toRGBArray(BufferedImage image){

        int rgbArray[][] = new int[image.getWidth()][image.getHeight()];

        for (int i=0; i < image.getWidth(); i++)
            for (int j=0; j < image.getHeight(); j++)
            {
                rgbArray[i][j] = image.getRGB(i,j);
            }
        return rgbArray;
    }

    /**
     *
     * @return the width of the image
     */
    public int getHeight(){
        return Image.length;
    }

    /**
     *
     * @return the height of the image
     */
    public int getWidth(){
        return Image[0].length;
    }

    @Override
    public NVImage clone() {

        NVImage clone = null;

        try {
            clone = (NVImage) super.clone();
            int[][] result = Image.clone();
            for (int i = 0; i < Image.length ; i++) {
                result[i] = Image[i].clone();
            }
            clone.Image = result;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return clone;
    }

}
