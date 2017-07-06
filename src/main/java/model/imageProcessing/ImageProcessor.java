package model.imageProcessing;

import model.ProfileParameters;
import model.imageProcessing.imageTypes.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class contains instruments to process images using motion detector </br>
 * Motion detector is based on background/foreground algorithm
 *
 * Created by Andrew on 24.04.2016.
 */
public class ImageProcessor {

    /**
     * area filtration threshold in pixels
     */
    private int ObjectArea = 1200;

    final int OPTIMAL_BINARIZED_AREA = 13;

    private double BinarizationThreshold = 0x20;

    private ImageRGB imageRGB = null;
    private ImageGray imageGray = null;
    private ImageBin imageBin = null;
    private ImageSubtracted imageSubtraction = null;

    public Map<Integer, Integer> objectMap = new HashMap<>();

    private ProfileParameters parameters;
    /**
     * Constructs a new object that can make operations with the image
     * @param image to operate with
     */
    public ImageProcessor(BufferedImage image, ProfileParameters parameters){
        imageRGB = new ImageRGB(image);
        this.parameters = parameters;
    }

    public ImageProcessor(int[][] image, ProfileParameters parameters){
        imageRGB = new ImageRGB(image);
        this.parameters = parameters;
    }

    /**
     * This method return image that was stored in the ImageProcessor entity
     * @return
     */
    public BufferedImage getImage() {
        return imageRGB.toBufferedImage();
    }

    /**
     * This method return grayscale image that was stored in the ImageProcessor entity
     * @return
     */
    public ImageGray getImageGray() {
        return imageGray;
    }
    /**
     * This method return binary image that was stored in the ImageProcessor entity
     * @return
     */
    public ImageBin getImageBin() { return imageBin; }

    /**
     * This method return image after subtraction that was stored in the ImageProcessor entity
     * @return
     */
    public ImageSubtracted getImageAfterSubtraction() { return imageSubtraction; }

    /**
     * This method return int[][] Array that represent image, stored in the ImageProcessor entity
     * @return
     */
    public ImageRGB getImageRGB() {
        return imageRGB;
    }


    public void setObjectArea(int objectArea){
        if (objectArea >= 0) {
            this.ObjectArea = objectArea;
        }
    }

    public int getObjectArea(){
        return this.ObjectArea;
    }

    /**
     * Set's threshold value for binarization algorithm
     * @param binarizationThreshold value of threshold
     */
    public void setBinarizationThreshold(double binarizationThreshold){
        this.BinarizationThreshold = binarizationThreshold;
    }

    /**
     * Converts image to grey color scheme </br>
     * Converted image can be get with getImageGray() method
     */
    public void imageToGray(){

        int[][] pixelArray = imageRGB.toPixelArray();

        for (int i = 0; i <pixelArray[0].length ; i++) {
            Color c = new Color(pixelArray[pixelArray.length/2][i]);
            if(c.getRed() != c.getGreen() && c.getBlue()!=c.getRed()){
                imageGray = imageToGray(imageRGB);
                return;
            }
        }
        imageGray = new ImageGray(imageRGB.toPixelArray());

    }

    /**
     * Converts image to grey color scheme
     * @param imageRGB color image to convert
     * @return image in grey
     */
    private ImageGray imageToGray(ImageRGB imageRGB){

        int[][] pixelArray = imageRGB.toPixelArray();

        int width = pixelArray.length;
        int height = pixelArray[0].length;

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++){

                int pixel = pixelArray[i][j];
                int pixelRED = (int)( 0.2125 * ((pixel>>16) & 0xFF) );
                int pixelGREEN = (int)( 0.7152 * ((pixel>>8) & 0xFF) );
                int pixelBLUE = (int)( 0.0722 * ((pixel>>0) & 0xFF) );

                pixelArray[i][j] = (pixelRED + pixelGREEN + pixelBLUE) << 16;
                pixelArray[i][j] |= (pixelRED + pixelGREEN + pixelBLUE) << 8;
                pixelArray[i][j] |= (pixelRED + pixelGREEN + pixelBLUE) << 0;
            }

        return new ImageGray(pixelArray);
    }

    /**
     * Threshold binarization of Color image in the ImageProcessor entity
     * if pixel value > threshold - white
     * else - black
     * @param threshold binarization threshold
     */
    @Deprecated
    private void imageBinarize(int threshold){
        imageBin = imageBinarize(imageRGB,threshold);
    }

    /**
     * Threshold binarization of image
     * if pixel value > threshold - white
     * else - black
     * @param image for binarization
     * @param threshold binarization threshold
     * @return binarized Image
     */
    @Deprecated
    private ImageBin imageBinarize(NVImage image, int threshold){

        int[][] pixelArray = image.toPixelArray();

        int width = pixelArray.length;
        int height = pixelArray[0].length;

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++){

                if ( (pixelArray[i][j]&0xFFFFFF)>threshold){
                    pixelArray[i][j] = 0xFFFFFF;
                }
                else{
                    pixelArray[i][j] = 0x000000;
                }
            }
        return new ImageBin(pixelArray);
    }

    /**
     * Method makes image larger
     * @param rate coeficient to make the image larger
     * @param rate
     */
    private void scaleLarger(int rate){

        int[][] pixelArray = imageGray.toPixelArray();

        int width = pixelArray.length;
        int height = pixelArray[0].length;
        int[][] scaleResult = new int[width*rate][height*rate];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = pixelArray[i][j]&0xFF;

                for (int k = 0; ( (k < rate) && ( i*rate+k+1 < scaleResult.length) ) ; k++) {
                    for (int l = 0; ( (l < rate) && ( j*rate+l+1 < scaleResult[0].length) ) ; l++) {
                        scaleResult[i*rate+k+1][j*rate+l+1] = pixel<<0 | pixel<<8 | pixel<<16;
                    }
                }
            }
        }
        imageGray = new ImageGray(scaleResult);
    }

    /**
     * @param rate coeficient to make the image smaller
     * Method makes image smaller
     */
    private void scaleSmaller(int rate){
        int[][] pixelArray = imageRGB.toPixelArray();

        int width = pixelArray.length;
        int height = pixelArray[0].length;

        int[][] scaleResult = new int[width/rate][height/rate];
        for (int i = 0; i < width; i+=rate) {
            for (int j = 0; j < height; j+=rate) {
                int average = 0;
                for (int k = i; (k < i+rate) && (k<width); k++) {
                    for (int l = j; (l < j+rate) && (l<height); l++) {
                        average += (pixelArray[k][l] & 0xFF);
                    }
                }
                average = average/(rate*rate);
                if ((i/rate < scaleResult.length) && (j/rate < scaleResult[0].length))
                scaleResult[i/rate][j/rate] = average<<0 | average<<8 | average << 16;
            }
        }
        imageRGB = new ImageRGB(scaleResult);
    }

    /**
     * Lowers quality of the image by averaging
     * @param rate coeficient to make the image smaller
     *
     */
    public void averageFilter(int rate){
        scaleSmaller(rate);
        //scaleLarger(rate);
    }

    /**
     * Converts Buffered image to RGB array
     * @param image image to convert
     * @return RGB array
     *//*
    public static int[][] toRGBArray(BufferedImage image){

        int rgbArray[][] = new int[image.getWidth()][image.getHeight()];

        for (int i=0; i < image.getWidth(); i++)
            for (int j=0; j < image.getHeight(); j++)
            {
                rgbArray[i][j] = image.getRGB(i,j);
            }
        return rgbArray;
    }*/

    /**
     * Converts RGB array to Buffered image
     * @param rgbArray RGB array
     * @return BufferedImage
     */
    /*public static BufferedImage toBufferedImage(int[][] rgbArray){

        int width = rgbArray.length;
        int height = rgbArray[0].length;

        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                image.setRGB(i,j,rgbArray[i][j]);
        return image;
    }*/

    /*/**
     * Converts JavaFX Image to BufferedImage
     * @param img image
     * @return BufferedImage
     */
    /*public static BufferedImage toBufferedImage(Image img){


        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return  bimage;
    }*/

    /**
     * This method finds and returns objects, that were found on the image<br>
     *     <br>
     *     <u><b>Attention!</b></u> <br>
     *     To make this method work properly, ensure that for this Image processor entity was called method <b>imageToGray()</b>;
     * @param background - background to subtract from the image
     * @return SceneObjects, found on the image
     */
    public HashMap<Long,SceneObject> getSceneObjects(ImageGray background) {

        HashMap<Long,SceneObject> objects = new HashMap<>();
        //int[][] backgroundNew;

        if (background.getWidth() != imageGray.getWidth() || background.getHeight() != imageGray.getHeight()) {
            //if images are different - don't make analysis
            return objects;
            //backgroundNew = imageGray.toPixelArray();

        }

        //time probe
        long m2 = System.currentTimeMillis();


        //int[][] foregroundNew = imageGray.toPixelArray();

        imageSubtraction = subtract(background,imageGray);

        colorAdder(parameters.getMinValueColorAdd(),
                                                parameters.getMaxValueColorAdd(),
                                                parameters.getColorAdd());

        int[][] imageForMaping = Bradley_threshold(imageSubtraction,this.BinarizationThreshold,OPTIMAL_BINARIZED_AREA);
        //binarizationQualityIncrease(imageBin);


        /**
         * зона де не ігнорується підрахунок. Запхнути потім в метод і зробити гарно і функціонально.
         * можна підвищити швидкодію,я кщо не робити в масиві непотрібні пікселі нулями,
         * а в mapObjects() мепити тільки необхідні пікселі
         */
        for (int i = 0; i < imageForMaping.length; i++) {
            for (int j = 0; j < imageForMaping[0].length; j++) {
                if(i>parameters.getNotIgnoreRectX()&&i<(parameters.getNotIgnoreRectX()+parameters.getNotIgnoreRectWidth())&&
                        j>parameters.getNotIgnoreRectY()&&j<(parameters.getNotIgnoreRectY()+parameters.getNotIgnoreRectHeight()))
                {
                    //do nothing
                }else{
                    imageForMaping[i][j]=0;
                }
            }
        }

        imageForMaping = mapObjects(imageForMaping);

        //System.out.println("Number of objects: " + objectMap.size());
        for (int label : objectMap.keySet()) {
            long m1 = System.currentTimeMillis();

            int Area = objectMap.get(label);

            //TODO:rework don't filter area here
            if (Area > this.ObjectArea){

                Rectangle bounds = getRectangle(imageForMaping,label,1);

                SceneObject obj = new SceneObject(bounds,0);
                objects.put(obj.getID(),obj);
            }
        }

        //System.out.println("Finish object listing in: " + (System.currentTimeMillis() - m2 ) + "ms");
        return objects;

    }

    private void binarizationQualityIncrease(int[][] imageForMaping) {
        int temp=0;

        for (int i = 0; i <imageForMaping.length ; i+=10) {
            for (int j = 0; j < imageForMaping[0].length; j+=10) {
                for (int k = i; k < i+10; k++) {
                    for (int l = j; l < j+10; l++) {
                        temp+=imageForMaping[k][l];
                    }
                }
                if(temp/100.0>0.6){
                    for (int k = 0; k < i+10; k++) {
                        for (int l = 0; l < j+10; l++) {
                            imageForMaping[k][l]=0;
                        }
                    }
                }
            }
        }
    }


    /**
     * Method calculates area of pixel group with given label
     *
     * @param array where to calculate area
     * @param label label of the group. Any integer value
     * @return
     */
    private int getArea(int[][] array, int label){
        int result = 0;

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j]==label) result++;
            }
        }
        return result;
    }

    /**
     * Binarizes image in pixel array to 0 and 1 for the purpose of mapping objects</br>
     * not the same as imageBinarize()<br>
     *     also refreshes binary representation of image that can be accessed by calling <b>getImageBin()</b>
     *
     * @param array to binarize. The result of imageBinarize() method;
     * @return NEW array with 0 and 1
     */
    @Deprecated
    private int[][] Binarize(int[][] array){

        int[][] BinarizedImage = new int[array.length][array[0].length];
        int[][] BinarizedImageRGB = new int[array.length][array[0].length];

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                int pixel = array[i][j]&0xFF;
                if (pixel > this.BinarizationThreshold){
                    BinarizedImage[i][j]=1;
                    BinarizedImageRGB[i][j]=0xFFFFFF;
                } else {
                    BinarizedImage[i][j]=0;
                    BinarizedImageRGB[i][j]=0x000000;
                }
            }
        }
        imageBin = new ImageBin(BinarizedImageRGB);

        return BinarizedImage;
    }

    /**
     * Adaptive algorithm for 2 dimension imageRGB array<br>
     * also refreshes binary representation of image that can be accessed by calling <b>getImageBin()</b>
     * @param sourceImage Source image. 2 dimension array.
     * @param threshold threshold for binarization (most often values are 0.9 - 1.2)
     * @param side
     * @return binarization image array (2 dimension)
     */
    private int[][] Bradley_threshold(NVImage sourceImage, double threshold, int side) {

        int[][] sourceImagePixels = sourceImage.toPixelArray();

        int[][] res = new int[sourceImagePixels.length][sourceImagePixels[0].length];
        int[][] BinarizedImageRGB = new int[sourceImagePixels.length][sourceImagePixels[0].length];
        int[][] integral_image;
        int sum;

        //тут так нада
        int hside = side - side/2;

        int pixels = side*side;

        //рассчитываем интегральное изображение (в гугле можно посмотреть что это)
        // Почитати про це можна тут:
        // https://m.habrahabr.ru/post/278435/
        integral_image = toIntegralImage(sourceImagePixels);

        //находим границы для локальных областей
        /**
         * Знаходимо координати меж квадрату, що буде бінаризуватися
         */
        for (int i = 0; i < sourceImagePixels.length; i++) {

            for (int j = 0; j < sourceImagePixels[0].length; j++) {

                int sA = -1;
                int sB = -1;
                int sC = -1;
                int sD = -1;

                int ai = (i - hside);
                if(ai<0)
                    sA=0;

                int aj = (j - hside);
                if(aj<0)
                    sA=0;

                int bi = (i - hside);
                if(bi<0)
                    sB=0;

                int bj = (j + hside-1);
                if(bj> sourceImagePixels[0].length-1)
                    bj = sourceImagePixels[0].length-1;

                int ci = (i + hside-1);
                if(ci> sourceImagePixels.length-1)
                    ci = sourceImagePixels.length-1;

                int cj = (j - hside);
                if(cj<0)
                    sC=0;

                int di = (i + hside-1);
                if(di> sourceImagePixels.length-1)
                    di = sourceImagePixels.length-1;

                int dj = (j + hside-1);
                if(dj> sourceImagePixels[0].length-1)
                    dj = sourceImagePixels[0].length-1;

                if(sA!=0){
                    sA = integral_image[ai][aj];
                }
                if(sB!=0){
                    sB = integral_image[bi][bj];
                }
                if(sC!=0){
                    sC = integral_image[ci][cj];
                }
                if(sD!=0){
                    sD = integral_image[di][dj];
                }

                /**
                 * розраховуємо по формулі суму значень пікселів квадрата, що на даному етапі бінаризується.
                 * Робиться це за допомогою інтегрального зображення. Почитати про це можна тут:
                 * https://m.habrahabr.ru/post/278435/
                 */
                sum = sA + sD - sC - sB;

                /**
                 * Щоб позбавитися від рамок тут слід поставити умови при яких
                 * в залежності від того якого розміру квадрат, що бінаризується, подальше ділення робиться
                 * на відповідну кількість пікселів.
                 */
                /*if(i>hside && j>hside){
                    pixels=side*side;
                }else{
                    if(i<=hside && j>hside){
                        pixels = (i+hside)*side;
                    }else{
                        if(i>hside && j<=hside){
                            pixels = side*(j+hside);
                        }
                    }
                }*/


                /**
                 * Знаходимо середнє значення яскравості пікселів у квадраті, що бінаризується і множимо на значення
                 * порогу бінаризації. Далі, в залежності від результату бінаризуємо зображення.
                 *
                 */
                if ( (sourceImagePixels[i][j])<sum / pixels*threshold){
                    res[i][j] = 0;
                    BinarizedImageRGB[i][j]=0x000000;
                }
                else{
                    res[i][j] = 1;
                    BinarizedImageRGB[i][j]=0xFFFFFF;
                }
            }

        }
        imageBin = new ImageBin(BinarizedImageRGB);
        return res;
    }

    /**
     * Converts image array to integral image array
     * @param srcArr source image array
     * @return integral image array
     */
    private int[][] toIntegralImage(int[][] srcArr){

        int [][]integral_image;
        int sum;

        integral_image = new int[srcArr.length][srcArr[0].length];

        for (int i = 0; i < srcArr.length; i++) {
            sum = 0;
            for (int j = 0; j < srcArr[0].length; j++) {
                sum += srcArr[i][j];
                if (i==0)
                    integral_image[i][j] = sum;
                else
                    integral_image[i][j] = integral_image[i-1][j] + sum;
            }
        }
        return integral_image;
    }


    /**
     * Increases pixels brightness whose values are in a given interval.
     * @param min minimum value of the interval
     * @param max maximum value of the interval
     * @param adder multiplier of the pixels values
     * @return image after processing
     */
    private void colorAdder(int min, int max, int adder){
        int[][] pixelArray = imageSubtraction.toPixelArray();
        for (int i=0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[0].length; j++) {
                Color colorSRC = new Color(pixelArray[i][j]);

                if (colorSRC.getBlue() > min && colorSRC.getBlue() < max) {
                    Color colorNew = new Color((int) (colorSRC.getRed() + adder), (int) (colorSRC.getGreen() + adder), (int) (colorSRC.getBlue() + adder));
                    pixelArray[i][j] = colorNew.getRGB();
                } else {
                    pixelArray[i][j] = colorSRC.getRGB();
                }
            }
        }
        imageSubtraction = new ImageSubtracted(pixelArray);
    }

    //:TODO більш детальний опис методу плз
    /**
     * Метод об’єднує групи по значенням
     * @param mapArray масив
     * @return масив з групами значень, де елементи кожної групи мають однакові значення
     */
    private int[][] mapObjects(int[][] mapArray)
    {

        //writeBufferedImage(toBufferedImage(mapArray),"binTest.jpg", "jpg");
        int kn;
        int km;

        int A;
        int B;
        int C;

        int cur = 0;

        // Цикл по пикселям изображения
        for (int i = 0; i < mapArray.length; i++){
            for (int j = 0; j < mapArray[0].length; j++){
                kn = j - 1;
                if (kn <= 0){
                    kn = 1;
                    B = 0;
                }
                else{
                    B = mapArray[i][kn];
                }
                km = i - 1;
                if (km <=0){
                    km = 1;
                    C = 0;
                }
                else {
                    C = mapArray[km][j];
                }
                A = mapArray[i][j];
                if (A == 0){
                    // Если в текущем пикселе пусто - то ничего не делаем
                }else if( (B == 0)&&(C == 0) ){
                    cur = cur + 1;
                    mapArray[i][j] = cur;
                    //якщо такого ключа ще нема

                    if (!objectMap.containsKey(cur) ) {
                        objectMap.put(cur, 1);
                    } else {
                        //якщо такий ключ уже є то враховуємо, що він має більшу площу
                        int area = objectMap.get(cur);
                        area++;
                        objectMap.put(cur, area);
                    }
                    //objectList.add(cur); //додати ідентифікатор в список
                }else if( (B != 0)&&(C == 0) ){
                    mapArray[i][j] = B;
                    int area = objectMap.get(B);
                    area++;
                    objectMap.put(B, area);
                }else if( (B == 0)&&(C != 0) ){
                    mapArray[i][j] = C;
                    int area = objectMap.get(C);
                    area++;
                    objectMap.put(C, area);
                }else if( (B != 0)&&(C != 0) ){

                    if (B == C){
                        mapArray[i][j] = B;
                        int area = objectMap.get(B);
                        area++;
                        objectMap.put(B, area);
                    }else{
                        mapArray[i][j] = B;
                        int area = objectMap.get(B);
                        area++;
                        objectMap.put(B, area);
                        mapArray = substituteC(mapArray, C, B);
                        //все що було С замінити на B
                    }
                }
            }
        }
        return mapArray;
    }

    /**
     * Метод заміняє Всі С в масиві на В
     * @param array масив
     * @param C значення С
     * @param B значення В
     * @return замінений масив
     */
    private int [][] substituteC(int[][] array, int C, int B)
    {
        int h = array.length;
        int w = array[0].length;

        int[][] result = array.clone();
        for (int i = 0; i < array.length ; i++) {
            result[i] = array[i].clone();
        }

        //якщо ключа В нема - додати його
        if ( !objectMap.containsKey(B) ){
            objectMap.put(B,1);
        }

        //i скласти їхні площі B та С
        int areaC = objectMap.get(C);
        int areaB = objectMap.get(B);
        int res = areaC + areaB;
        objectMap.put(B,res);

        //якщо ключ С є - видалити його
        if ( objectMap.containsKey(C) )
            objectMap.remove(C);

        //System.out.println("Substitution of " + C + " by " + B);

        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++){
                if ( result[i][j] == C ) result[i][j] = B;
            }
        return result;
    }

    /**
     * Знаходить початкову і кінцеву точку заданої групи
     * @param array масив
     * @param label мітка групи
     * @return
     */
    private Rectangle getRectangle(int[][] array, int label, int coef)
    {
        int h = array.length;
        int w = array[0].length;

        Rectangle result = new Rectangle();

        int minimalW = w;
        int minimalH = h;

        int maximalW = 0;
        int maximalH = 0;


        for (int i = 0; i < h; i++)
        {
            for (int j = 0; j < w; j++)
            {
                if ( (array[i][j] == label) && (minimalH > i) ) minimalH = i;
                if ( (array[i][j] == label) && (minimalW > j) ) minimalW = j;

                if ( (array[i][j] == label) && (maximalH < i) ) maximalH = i;
                if ( (array[i][j] == label) && (maximalW < j) ) maximalW = j;
            }
        }

        result.x = minimalW  * coef;
        result.y = minimalH * coef;

        result.width = (maximalW * coef - result.x) ;
        result.height = (maximalH * coef - result.y) ;

        return result;
    }

    /**
     * Рекомендується подавати інтенсивності бінаризованих зображень
     * @param BaseImage матриця інтенсивностей базового зображення
     * @param DistortedImage матриця інтенсивностей зміненого зображення
     * @return різниця інтенсивностей
     * */
    private ImageSubtracted subtract(ImageGray BaseImage, ImageGray DistortedImage)
    {
        int[][] pixelsBaseImage = BaseImage.toPixelArray();
        int[][] pixelsDistortedImage = DistortedImage.toPixelArray();

        int[][] result = new int[pixelsBaseImage.length][pixelsBaseImage[0].length];
        Color cBase;
        Color cDistorted;
        Color res;
        for (int i = 0; i < result.length; i++){
            for (int j=0; j < result[0].length; j++){
                cBase =   new Color(pixelsBaseImage[i][j]);
                cDistorted = new Color(pixelsDistortedImage[i][j]);
                int diff = Math.abs(cDistorted.getRed()-cBase.getRed());
                res = new Color(diff,diff,diff);
                result[i][j]=res.getRGB();
            }
        }
        return new ImageSubtracted(result);
    }

    /**
     * Puts patches to the new background from the previous
     * @param prevBackground
     * @param newBackground
     * @param objectMap
     * @return
     */
    public BufferedImage patchBackground(NVImage prevBackground, NVImage newBackground, HashMapContainer objectMap){

        int[][] prevBackgroundRGB = prevBackground.toPixelArray();
        int[][] newBackgroundRGB = newBackground.toPixelArray();

        List<SceneObject> objects = objectMap.objectsByGeneration(1);

        for(int k=0;k<objects.size();k++){

            boolean substitute = true;

            List<SceneObject> parents = objectMap.getParentList(objects.get(k).getID());

            Point objectPoint = objects.get(k).getBounds().getLocation();
            Point parentPoint;

            if (parents.size() > parameters.getAcceptObjectRate()) {
                for (int i = 0; i < parameters.getAcceptObjectRate(); i++) {
                    parentPoint = parents.get(i).getBounds().getLocation();

                    if ((objectPoint.x < (parentPoint.x + parameters.getAcceptRadius())) && (objectPoint.x > (parentPoint.x - parameters.getAcceptRadius())) &&
                            (objectPoint.y < (parentPoint.y + parameters.getAcceptRadius())) && (objectPoint.y > (parentPoint.y - parameters.getAcceptRadius()))) {
                        substitute = false;
                    }
                    else {
                        substitute = true;
                        break;
                    }
                }
            }

            if((substitute)){
                for (int i = objects.get(k).getBounds().x; i < objects.get(k).getBounds().x + objects.get(k).getBounds().width; i++) {
                    for (int j = objects.get(k).getBounds().y; j < objects.get(k).getBounds().y + objects.get(k).getBounds().height; j++) {
                        newBackgroundRGB[j][i] = prevBackgroundRGB[j][i];
                    }
                }

            }
        }

        //ImageRGB is used here as an universal container for image.
        //even if the image is gray or binary it's pixel values have RGB colors
        BufferedImage result = new ImageRGB(newBackgroundRGB).toBufferedImage();
        return result;
    }


}
