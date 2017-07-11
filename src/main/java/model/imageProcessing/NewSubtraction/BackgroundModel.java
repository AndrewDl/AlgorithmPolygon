package model.imageProcessing.NewSubtraction;

import model.imageProcessing.imageTypes.ImageGray;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andrew on 07/06/17.
 */
public class BackgroundModel {
    private List<int[][]> cards;

    private final int width;
    private final int height;

    Random r;// = new Random(System.currentTimeMillis());

    private int frameSize = 1;
    private int matchingThreshold = 20;
    private int requiredMatches = 10;
    private int updateFactor = 16;

    public BackgroundModel(ImageGray baseImage, int sampleSize){
        cards = new ArrayList<>();

        int[][] array = baseImage.toPixelArray();

        width = array.length;
        height = array[0].length;

        r = new Random(System.currentTimeMillis());
        initCards(sampleSize, baseImage);
    }

    private void initCards(int sampleSize,ImageGray baseImage){
        cards.add(baseImage.toPixelArray());

        int width = baseImage.getWidth();
        int height = baseImage.getHeight();

        for (int i = 1; i < sampleSize; i++){
            int[][] newSample = new int[width][height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    newSample[x][y] = getRandPixel(cards.get(0),x,y);
                }
            }

            cards.add(newSample);
        }
    }

    public int getMatchingThreshold(){return matchingThreshold;}

    public void setMatchingThreshold(int matchingThreshold){this.matchingThreshold = matchingThreshold;}

    public int getRequiredMatches(){return requiredMatches;}

    public void setRequiredMatches(int requiredMatches){this.requiredMatches = requiredMatches;}

    public int getUpdateFactor(){return updateFactor;}

    public void setUpdateFactor(int updateFactor){this.updateFactor = updateFactor;}

    public boolean matchPixel(int x, int y, int color){
        //long t1 = System.currentTimeMillis();

        int matches = 0;

        int pixel = color & 0xFF;

        for (int i = 0; i < cards.size(); i++) {
            int[][] currentCard = cards.get(i);

            if( Math.abs(pixel - (currentCard[x][y] & 0xFF)) > matchingThreshold){
                matches++;
                if (matches > requiredMatches) return true;
            }
        }

        //long t2 = System.currentTimeMillis();
        //System.out.println("matching in: " + (t2-t1));
        return false;
    }

    public boolean update(int x, int y, int color){

        //long t1 = System.currentTimeMillis();

        //Random r = new Random(System.currentTimeMillis());

        if ( r.nextInt(100) < (1/(float)updateFactor * 100) ){



            int xshift = r.nextInt(1);// == 0 ? 1 : -1; //= r.nextInt(frameSize) - frameSize /2 ;
            int yshift = r.nextInt(1);// == 0 ? 1 : -1; //r.nextInt(frameSize) - frameSize /2 ;

            int x1 = x + xshift >= width ? x - xshift : x + xshift ;
            int y1 = y + yshift >= height ? y - yshift : y + yshift ;

            cards.get(r.nextInt(cards.size()))[x][y] = color;
            cards.get(r.nextInt(cards.size()))[x1][y1] = color;

        }

        //long t2 = System.currentTimeMillis();

        //System.out.println("update in: " + (t2-t1));

        return false;
    }

    public int getRandPixel(int[][] image, int x, int y){
        //TODO: complete this

        int xshift = r.nextInt(1);// == 0 ? 1 : -1; //= r.nextInt(frameSize) - frameSize /2 ;
        int yshift = r.nextInt(1);// == 0 ? 1 : -1; //r.nextInt(frameSize) - frameSize /2 ;

        int x1 = x + xshift >= width ? x - xshift : x + xshift ;
        int y1 = y + yshift >= height ? y - yshift : y + yshift ;

        int rgb = image[x1][y1];

        return rgb;

    }
}
