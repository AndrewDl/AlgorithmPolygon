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
    private List<BufferedImage> cards;

    private Random r = new Random(System.currentTimeMillis());

    private int frameSize = 40;
    private int matchingThreshold = 20;
    private int requiredMatches = 2;
    private int updateFactor = 16;

    public BackgroundModel(int sampleSize, ImageGray baseImage){
        cards = new ArrayList<>();
        initCards(sampleSize, baseImage);
    }

    private void initCards(int sampleSize,ImageGray baseImage){
        cards.add(baseImage.toBufferedImage());

        int width = baseImage.getWidth();
        int height = baseImage.getHeight();

        for (int i = 1; i < sampleSize; i++){
            BufferedImage newSample = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {

                    int xshift = r.nextInt(frameSize) - frameSize /2 ;
                    int yshift = r.nextInt(frameSize) - frameSize /2 ;

                    int x1 = x + xshift > width ? x - xshift : Math.abs(x + xshift) ;
                    int y1 = y + yshift > height ? y - yshift : Math.abs(y + yshift) ;

                    int pixel = cards.get(i).getRGB(x1,y1);

                    newSample.setRGB(x,y,pixel);
                }
            }
        }
    }

    private boolean mathPixel(int x, int y, int color){

        int matches = 0;

        int pixel = color & 0xFF;

        for (int i = 0; i < cards.size(); i++) {
            BufferedImage currentCard = cards.get(i);

            if( Math.abs(pixel - (currentCard.getRGB(x,y) & 0xFF)) > matchingThreshold){
                matches++;
                if (matches > requiredMatches) return true;
            }
        }

        return false;
    }

    private boolean update(int x, int y, int color){

        if ( r.nextInt(100) < (1/updateFactor * 100) ){


            int xshift = r.nextInt(2) - 1 ;
            int yshift = r.nextInt(2) - 1 ;

            int width = cards.get(0).getWidth();
            int height = cards.get(0).getWidth();

            int x1 = x + xshift > width ? x - xshift : Math.abs(x + xshift) ;
            int y1 = y + yshift > height ? y - yshift : Math.abs(y + yshift) ;

            cards.get(r.nextInt(20)).setRGB(x,y,color);
            cards.get(r.nextInt(20)).setRGB(x1,y1,color);

        }

        return false;
    }

    private int getRandPixel(){
        //TODO: complete this
        return 0;
    }
}
