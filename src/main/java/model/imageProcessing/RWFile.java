package model.imageProcessing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Sergiy on 2/10/2017.
 */
public class RWFile {
    /**
     * Read buffered image from input location.
     * @param fName image location path
     */
    //TODO add format of fName
    public BufferedImage readBufferedImage(String fName){
        File imageFile = new File(fName);

        BufferedImage bImage = null;
        try {
            bImage = ImageIO.read(imageFile);
            return bImage;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("-- CAN'T READ FILE --");
        }
        return null;
    }

    /**
     * Read buffered image from input location.
     * @param fName image location path
     */


    /**
     * Read buffered image from input location.
     * @param fName image location path
     * @return pixel array of the  image
     */
    public int[][] readBufferedArrayImage(String fName){
        File imageFile = new File(fName);
        int[][] image = new int[1][1];
        BufferedImage bImage = null;
        try {
            bImage = ImageIO.read(imageFile);
            image = new int[bImage.getWidth()][bImage.getHeight()];
            for (int i = 0; i < image.length ; i++) {
                for (int j = 0; j < image[0].length; j++) {
                    Color c = new Color(bImage.getRGB(i,j));
                    if(c.getRed()>125)
                        image[i][j]=1;
                    else
                        image[i][j]=0;
                }
            }
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("-- CAN'T READ FILE --");
        }
        return image;
    }

    /**
     *
     * @param bufImg BufferedImage to be stored
     * @param fName name of file
     * @param format file format without dot (.) before format name
     */
    public void writeBufferedImage(BufferedImage bufImg, String fName, String format){
        File outputfile = new File(fName+"."+format);
        try {
            ImageIO.write(bufImg, format, outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





