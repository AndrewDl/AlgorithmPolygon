package model.imageProcessing;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Sergiy on 7/20/2017.
 */
public class Web {

    public int[][] mul; // ��� ����� ������� ������������������ �������
    public int[][] weight; // ������ ��� �������� �����
    public int[][] input; // ������� ����������
    public int limit = 5000; // ����� - ������ ����������������, ��� �������� ��������
    public int sum ; // ��� �������� ����� ���������������� ��������

    private int height;
    private int width;

    public Web(int height, int width,int[][] inP) // ������ �������� ��� �������� �������
    {
        this.height=height;
        this.width=width;
        //weight = new int[height][width]; // ������������ � �������� ������� (����� ������)
        mul = new int[height][ width];
        input = new int[height][ width];
        input = inP; // �������� ������� ������

        //weight_load();
    }
    public void mul_w()
    {
        for (int i = 0; i <height; i++)
        {
            for (int j = 0; j <width; j++) // ��������� �� ������� ������
            {
                mul[i][ j] = input[i][j]*weight[i][j]; // �������� ��� ������ (0 ��� 1) �� ��� ����������� ��� � ��������� � ������.
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
     * ����� ��� � �����
     * @param path ���� � �����
     * @return ������� ������ ���
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
