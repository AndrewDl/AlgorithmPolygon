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
/*       //&&a[x][y]==0

       //////////////////////////////////////////////////////////
       //Andrews govnokod
       int r = 1;

       int sum = 0;

//       for (int i = x-r; i < x+r; i++) {
//           for (int j = y-r; j < y+r; j++) {
//               sum += a[i][j] & 0xFF;
//           }
//       }
//       sum -= a[x][y]&0xFF;


       sum += a[x-1][y]&0xFF + a[x+1][y]&0xFF + a[x][y-1]&0xFF + a[x][y+1]&0xFF;


       float result = (float) sum / 4;

       return result;
        ///////////////////////////////////////////////////////////////////////
*/
       if(a[x][y]==a[x-1][y]&&a[x][y]!=0)count++;
       if(a[x][y]==a[x][y-1]&&a[x][y]!=0)count++;
       if(a[x][y]==a[x+1][y]&&a[x][y]!=0)count++;
       if(a[x][y]==a[x][y+1]&&a[x][y]!=0)count++;
     //  System.out.println(count);
      if(count>=3){
      //   System.out.println("tok");
            return false;
      }
      else return true;

   }
   public ImageBin AvarageFilter(ImageBin image){
       //ImageBin image2 = image;
       int[][] originalImage = image.toPixelArray();

       int width = originalImage.length;
       int height = originalImage[0].length;
       //System.out.println("width="+width+"height"+height+"original="+image.getHeight());
       int[][] resultingImage = new int[width][height];

       for(int x=2; x<image.getWidth()-2; x++){
           for(int y=2; y<image.getHeight()-2; y++){
              // int[][] pixelSet = image2.toPixelArray();
               resultingImage[x][y]=originalImage[x][y];
               if(getSumm(originalImage, x, y)){
                   resultingImage[x][y]=0;
                   // image.toPixelArray()[x][y]=0;
                       //image.toBufferedImage().setRGB(x,y,255);
                     //  image.toPixelArray()[x][y] = 0xFFFFFF;
               }else if (getSumm(originalImage, x, y)){
                   resultingImage[x][y]=0xFFFFFF;
                     //  image.toPixelArray()[x][y]=0xFFFFFF;
                      // image.toBufferedImage().setRGB(x,y,0);
                     //  image.toPixelArray()[x][y]=0;
               }
              // System.out.println("valueOfInputImage="+image.toPixelArray()[x][y]+"valueOfResulting="+resultingImage[x][y]+"vaueOfCopy"+originalImage[x][y]);
              // System.out.println("x="+x+"y"+y);
           }
       }
       ImageBin result = new ImageBin(resultingImage);
//return image;
       return result;
   }
}
