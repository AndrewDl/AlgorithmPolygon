package model.imageProcessing;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Sergiy on 7/20/2017.
 */
public class Web {

    public int[][] mul; // Тут будем хранить отмасштабированные сигналы
    public int[][] weight; // Массив для хранения весов
    public int[][] input; // Входная информация
    public int limit = 5000; // Порог - выбран экспериментально, для быстрого обучения
    public int sum ; // Тут сохраним сумму масштабированных сигналов

    private int height;
    private int width;

    public Web(int height, int width,int[][] inP) // Задаем свойства при создании объекта
    {
        this.height=height;
        this.width=width;
        //weight = new int[height][width]; // Определяемся с размером массива (число входов)
        mul = new int[height][ width];
        input = new int[height][ width];
        input = inP; // Получаем входные данные

        //weight_load();
    }
    public void mul_w()
    {
        for (int i = 0; i <height; i++)
        {
            for (int j = 0; j <width; j++) // Пробегаем по каждому аксону
            {
                mul[i][ j] = input[i][j]*weight[i][j]; // Умножаем его сигнал (0 или 1) на его собственный вес и сохраняем в массив.
            }
        }
    }

    public void Sum()
    {
        sum = 0;
        for (int i = 0; i <height; i++)
        {
            for (int j = 0; j <width; j++)
            {
                sum += mul[i][ j];
            }
        }
    }
    public boolean Rez()
    {
        if (sum >= limit)
            return true;
        else return false;
    }

    private void weight_load(){
//        for (int i = 0; i <weight.length ; i++) {
//            for (int j = 0; j < weight[0].length; j++) {
//                weight[i][j] = new Random().nextInt(5)+1;
//                //weight[i][j] = 3;
//            }
//        }
        readFile("TestHuman.txt");

    }

    public void incW(int[][] inP)
    {
        for (int i = 0; i <height; i++)
        {
            for (int j = 0; j <width; j++)
            {
                weight[i][ j] += inP[i][ j];
            }
        }
//        try {
//            WriteFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void decW(int[][] inP)
    {
        for (int i = 0; i <height; i++)
        {
            for (int j = 0; j <width; j++)
            {
                weight[i][ j] -= inP[i][ j];
            }
        }
//        try {
//            WriteFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void WriteFile() throws IOException
    {

        FileWriter filewriter = new FileWriter(new File("TestHuman.txt"));

        for (int i=0;i<weight.length;++i) {
            for (int j = 0; j < weight[0].length; ++j)
                filewriter.write(weight[i][j] + "\n");
            //filewriter.write("\n");
        }
        filewriter.flush();
    }

    /**
     * зчитує дані з файлу
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
