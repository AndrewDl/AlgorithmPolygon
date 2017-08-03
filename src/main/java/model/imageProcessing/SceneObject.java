package model.imageProcessing;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by Andrew on 24.08.2016.
 * This os used to represent any object that appears on the scene.
 */
public class SceneObject{
    //TODO: Finish this and make some comments
    private Rectangle Bounds;
    private int percent;
    private Long ParentID = null;
    private Long ID;

    private ArrayList<String> passedLines;
    public ArrayList<String> getPassedLines() {
        return passedLines;
    }
    public void setPassedLines(ArrayList<String> passedLines) {
        this.passedLines = passedLines;
    }

    private long BornTime = 0;

    private boolean passed = false;
    private int Generation = 0;
    private boolean remove = false;

    public SceneObject( Rectangle bounds, int Percent){
        percent = Percent;
        Bounds = bounds;
        BornTime = new Date().getTime();

        Random r = new Random();
        ID = r.nextLong();
        passedLines = new ArrayList<>();
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    /**
     * Makes a clone of given instance
     * @param sceneObject instance to clone
     */
    public SceneObject( SceneObject sceneObject ){
        this.Bounds = sceneObject.getBounds();
        this.percent = sceneObject.getPercent();
        this.ParentID = sceneObject.getParentID();
        this.ID = sceneObject.getID();
        this.BornTime = sceneObject.BornTime;
        this.passed = sceneObject.passed;
        this.Generation = sceneObject.Generation;
        this.passedLines = sceneObject.getPassedLines();
    }

    public SceneObject(){
        this.Bounds = new Rectangle();
        this.percent = 0;
        this.ParentID = null;

        Random r = new Random();
        ID = r.nextLong();

        this.BornTime = new Date().getTime();
        this.passed = false;
        passedLines = new ArrayList<>();
    }

    public long getBornTime(){
        return BornTime;
    }

    public int getGeneration(){
        return this.Generation;
    }

    public void incGeneration(){
        Generation = Generation<2 ? Generation+1 : Generation;
    }

    public Rectangle getBounds() {
        return Bounds;
    }

    public void setBounds(Rectangle bounds) {
        Bounds = bounds;
    }

    public Long getID(){
        return ID;
    }

    public Long getParentID() {
        return ParentID;
    }

    public void setParent(SceneObject parent) {
        ParentID = parent.ID;

        if (this.Generation == 0) this.Generation = 1;
        parent.Generation = 2;

        //if parent passes the line - set this also passed
        this.setPassedLines(parent.getPassedLines());
    }

    public Point getTrackPoint(){
        int x = Bounds.x + (int)(Bounds.getWidth());
        int y = Bounds.y + (int)(Bounds.getHeight()/2);

        return new Point(x,y);
    }


    public boolean isPassed(String lineLoc) {
        for (int i = 0; i <passedLines.size() ; i++) {
             if(passedLines.get(i)==lineLoc){
                passed=true;
                }
        }
        return passed;
    }

    public void addPassedLine(String passedLine) {
        this.passed = passed;
        this.passedLines.add(passedLine);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof SceneObject))return false;

        //if ID of objects are equal - objects are equal

        SceneObject testObj = (SceneObject) obj;
        if (testObj.getID() == this.getID()) return true;

        return false;
    }

    public int getPercent(){
        return percent;
    }

    public void setPercent(int Percent){
        percent = Percent;
    }
}
