package model.imageProcessing;

/**
 * Created by Sergiy on 7/31/2017.
 */
public class NeuralObject {
    private int xPos;
    private int yPos;
    private int height;
    private int width;
    private int objectWeight;
    private int deleteRadius;
    private boolean remove = false;
    private boolean ignore = false;

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }


    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public NeuralObject(int xPos, int yPos, int height, int width, int objectWeight, int deleteRadius) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.height = height;
        this.width = width;
        this.objectWeight = objectWeight;
        this.deleteRadius = deleteRadius;
    }


    public int getDeleteRadius() {
        return deleteRadius;
    }

    public void setDeleteRadius(int deleteRadius) {
        this.deleteRadius = deleteRadius;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getObjectWeight() {
        return objectWeight;
    }

    public void setObjectWeight(int objectWeight) {
        this.objectWeight = objectWeight;
    }

}
