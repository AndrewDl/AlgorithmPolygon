package model.imageProcessing;

import java.awt.geom.Line2D;

/**
 * Created by Sergiy on 10/10/2016.
 */
public class SceneLine {

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private int In = 0;
    private int Out = 0;

    public String getLocation() {
        return location;
    }

    private String location = "defaultLocation";
    private String LineName;

    @Override
    public String toString(){
        return LineName;
    }

    public Line2D getLine(){
        return new Line2D.Double(x1,y1,x2,y2);
    }

    public void setLine(Line2D line){
        x1 = (int)line.getX1();
        y1 = (int)line.getY1();
        x2 = (int)line.getX2();
        y2 = (int)line.getY2();
    }
    public void setLine(int x1,int y1,int x2,int y2){
        this.x1= x1;
        this.y1= y1;
        this.x2= x2;
        this.y2= y2;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public String getName(){
        return location;
    }

    public String getLineName() {return LineName; }

    public int getIn() {
        return In;
    }

    public void setIn(int in) {
        In = in;
    }

    public int getOut() {
        return Out;
    }

    public void addCount(int in, int out){

        if (in>=0 && out >=0) {
            In += in;
            Out += out;
        }
    }


    public void setOut(int out) {
        Out = out;
    }
}
