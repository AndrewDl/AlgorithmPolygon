package model.imageProcessing.NewSubtraction;

import model.imageProcessing.imageTypes.ImageBin;

/**
 * Created by Laimi on 13.07.2017.
 */
public class EnchAvgPixelFill extends AvgPixelFill {
    public boolean getSummPlus(int[][] a, int x, int y){
        int count = 0;
        int r=2;
       for (int i = x-r; i < x+r; i++) {
           for (int j = y-r; j < y+r; j++) {
               if(a[x][y]==a[i][j])count++;
               if(x==i&&y==j)count--;
           }
       }

        if(count>=3){
            return false;
        }
        else return true;
    }
    public ImageBin EnchAvarageFilter(ImageBin image){
        int[][] originalImage = image.toPixelArray();
        int width = originalImage.length;
        int height = originalImage[0].length;
        int[][] resultingImage = new int[width][height];
        for(int x=2; x<image.getWidth()-2; x++){
            for(int y=2; y<image.getHeight()-2; y++){
                resultingImage[x][y]=originalImage[x][y];
                if(getSummPlus(originalImage, x, y)){
                    resultingImage[x][y]=0;
                }else if (getSummPlus(originalImage, x, y)){
                    resultingImage[x][y]=0xFFFFFF;
                }
            }
        }
        ImageBin result = new ImageBin(resultingImage);

        return result;
    }
}
