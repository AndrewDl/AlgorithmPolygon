package model.imageProcessing;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Class represents an area that handles cases of income and outcome people
 * Created by Andrew on 01.10.2016.
 */
public class SceneArea {
    private Rectangle2D Bounds;

    public SceneArea(Point2D Point1, Point2D Point2){
        setBounds(Point1,Point2);
    }

    public SceneArea(double X1, double Y1, double X2, double Y2){
        setBounds(new Point((int)X1,(int)Y1),new Point((int)X2,(int)Y2));
    }

    public Rectangle2D getBounds() {
        return Bounds;
    }

    public void setBounds(Point2D Point1, Point2D Point2) {
        double width = Math.abs( Point2.getX() - Point1.getX() );
        double height = Math.abs( Point2.getY() - Point1.getY() );

        double X;
        double Y;

        if (Point1.getX() < Point2.getX()) X = Point1.getX();
        else X = Point2.getX();

        if (Point1.getY() < Point2.getY()) Y = Point1.getY();
        else Y = Point2.getX();

        Bounds = new Rectangle2D.Double(X, Y, width, height );
    }
}
