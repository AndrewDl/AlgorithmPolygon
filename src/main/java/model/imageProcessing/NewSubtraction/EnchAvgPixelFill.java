package model.imageProcessing.NewSubtraction;

import model.imageProcessing.imageTypes.ImageBin;

/**
 * Created by Laimi on 13.07.2017.
 */
/**
 * Radius can be changed now and used as new parameter.
 * Also counter can be used as additional parameter on UI.
 * Probably the best values for radius between 2 and 5 included.
 */
public class EnchAvgPixelFill extends AvgPixelFill {
    public boolean getSummPlus(int[][] a, int x, int y){
         int count = 0;
        int r=3;//radius
       for (int i = x-r; i < x+r; i++) {
           for (int j = y-r; j < y+r; j++) {
//               if(x<=0||y<=0){ j+=r; i+=r; x+=r; y+=r; }
              if(x+r>=a.length||y+r>=a[0].length)break;
               if(x-r<=0||y-r<=0)break;
               if(a[x][y]==a[i][j])count++;
               if(x==i&&y==j)count--;
           }
           if(x+r>=a.length||y+r>=a[0].length)break;
           if(x-r<=0||y-r<=0)break;
       }

        if(count>=3){
            return false;
        }
        else return true;
    }
    /**
     * filter wich changes lonely 0 and 1 pixels to color of their neigbors
     */
    public ImageBin EnchAvarageFilter(ImageBin image){
        int[][] originalImage = image.toPixelArray();
        int width = originalImage.length;
        int height = originalImage[0].length;
        int[][] resultingImage = new int[width][height];
        for(int x=0; x<image.getWidth(); x++){
            for(int y=0; y<image.getHeight(); y++){
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
