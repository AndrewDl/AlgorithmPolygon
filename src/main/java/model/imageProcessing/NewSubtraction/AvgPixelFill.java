package model.imageProcessing.NewSubtraction;

import model.imageProcessing.imageTypes.ImageBin;

import javax.xml.bind.SchemaOutputResolver;

/**
 * Created by Laimi on 11.07.2017.
 */
public class AvgPixelFill {
   private int width;
   private int height;
   private boolean getSumm(int[][] a, int x, int y){
       int count = 0;
       //int summ = a[x-1][y-1] + a[x][y-1] + a[x+1][y-1] + a[x-1][y] + a[x][y] + a[x+1][y] + a[x-1][y+1] + a[x][y+1] + a[x+1][y+1];
       //System.out.println(a[x-1][y-1]+a[x][y-1]);
       if(a[x-1][y-1]==0xFFFFFF)count++;
       if(a[x][y-1]==0xFFFFF)count++;
       if(a[x+1][y-1]==0xFFFFFF)count++;
       if(a[x-1][y]==0xFFFFFF)count++;
       if(a[x][y]==0xFFFFFF)count++;
       if(a[x+1][y]==0xFFFFFF)count++;
       if(a[x-1][y+1]==0xFFFFFF)count++;
       if(a[x][y+1]==0xFFFFFF)count++;
       if(a[x+1][y+1]==0xFFFFFF)count++;
      if(count>=5)
       return true;
      else return false;
   }
   public ImageBin AvarageFilter(ImageBin image){
       ImageBin image2 = image;
       for(int x=1; x<image2.getWidth()-1; x++){
           for(int y=1; y<image2.getHeight()-1; y++){
               int[][] pixelSet = image2.toPixelArray();
                   if(getSumm(pixelSet, x, y)){
                       //image.toBufferedImage().setRGB(x,y,255);
                       image.toPixelArray()[x][y] = 0xFFFFFF;
               }else{
                      // image.toBufferedImage().setRGB(x,y,0);
                       image.toPixelArray()[x][y]=0;
               }
               //System.out.println("x="+x+"y"+y);
           }
       }

       return image;
   }
}
