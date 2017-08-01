package model.imageProcessing;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Sergiy on 7/31/2017.
 */
public class Weight {

    private String path;
    private int height;
    private int width;
    private int[][] weight;

    public int[][] getWeight() {
        return weight;
    }



    public Weight(String path, int height, int width){
        this.height =height;
        this.width = width;
        this.path = path;
        weight = new int[height][width];
        weight_load();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void weight_load(){
//        for (int i = 0; i <weight.length ; i++) {
//            for (int j = 0; j < weight[0].length; j++) {
//                weight[i][j] = new Random().nextInt(5)+1;
//                //weight[i][j] = 3;
//            }
//        }
        readFile(path);

    }

    public void save() throws IOException
    {

        FileWriter filewriter = new FileWriter(new File(path));

        for (int i=0;i<weight.length;++i) {
            for (int j = 0; j < weight[0].length; ++j)
                filewriter.write(weight[i][j] + "\n");
            //filewriter.write("\n");
        }
        filewriter.flush();
    }
    /**
     * Resize array with weights
     * @param pixels that will be resized
     * @param w1 - initial width
     * @param h1 - initial height
     * @param w2 - new width
     * @param h2 - new height
     * @return resized array
     */
    public int[][] resize(int[][] pixels,int w1,int h1,int w2,int h2) {
        int[][] temp = new int[h2][w2] ;
        double x_ratio = w1/(double)w2 ;
        double y_ratio = h1/(double)h2 ;
        double px, py ;
        for (int i=0;i<h2;i++) {
            for (int j=0;j<w2;j++) {
                px = Math.floor(j*x_ratio) ;
                py = Math.floor(i*y_ratio) ;
                temp[i][j] = pixels[(int)((py*w1)+px)/w1][(int)((py*w1)+px)%w1] ;
            }
        }
        return temp ;
    }

    /**
     * зчитує дані з файлу, походу повинен лежати в класі RWFile
     * @param path петь к файлу
     * @return повертає список слів
     */
    public void readFile(String path){
        ArrayList<String> s = new ArrayList<>();

        String str;

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i <weight.length ; i++) {
            for (int j = 0; j < weight[0].length; j++) {
                try {
                    weight[i][j]= Integer.parseInt(in.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
