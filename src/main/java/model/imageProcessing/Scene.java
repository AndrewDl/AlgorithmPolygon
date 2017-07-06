package model.imageProcessing;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class represents scene</br>
 * This class is designed to contain information about objects on the scene </br>
 * Also it contains methods to handle this information
 *
 *
 * Created by Andrew on 09/10/16.
 */
public class Scene {

    private HashMapContainer ObjectPool = new HashMapContainer();

    private int MergeRadius;

    private List<SceneObject> deadObjects = new ArrayList<>();

    public void setMergeRadius(int mergeRadius){
        MergeRadius = mergeRadius;
    }

    public Map<Long, SceneObject> getObjects(){
        return ObjectPool.getMap();
    }

    public void addObjects(HashMap<Long,SceneObject> newObjects){
        ObjectPool.putAll(newObjects);
        deadObjects = mapDependencies();
    }

    /**
     * Method finds dependencies between objects and finds relations within 0s and 1st generation of objects
     * @return objects that have no descendants
     */
    private List<SceneObject> mapDependencies(){
        List<SceneObject> ListNew = ObjectPool.objectsByGeneration(0);
        List<SceneObject> ListOld = ObjectPool.objectsByGeneration(1);

        HashMap<SceneObject,SceneObject> pairs = new HashMap<>();

        int indexOld = 0;
        int indexNew = 0;
        //for every old object
        for (int i = 0; i < ListOld.size(); i++) {
            ListOld.get(i).incGeneration();
            //if Old object is not merged
            if ( !pairs.containsValue(ListOld.get(i)) ) {
                //double minDistance = getDistance(ListOld.getByKey(i), ListNew.getByKey(0));
                double minDistance = 65535;
                //i+1
                for (int j = 0; j < ListNew.size(); j++) {
                    //if (ListNew.get(i).getGeneration() == 0) ListNew.get(i).incGeneration();
                    //if New object is not merged
                    if ( !pairs.containsKey(ListNew.get(j)) ) {
                        if (minDistance > getDistance(ListOld.get(i), ListNew.get(j))) {
                            //new minimum found
                            minDistance = getDistance(ListOld.get(i), ListNew.get(j));
                            //save indexes with minimal distance
                            indexOld = i;
                            indexNew = j;
                        }
                    } else {
                        double currentDistance = getDistance(ListOld.get(i), ListNew.get(j));
                        if (minDistance > currentDistance) {
                            minDistance = currentDistance;
                            //if currentDistance less than distance between paired objects - replace pairs.
                            SceneObject oldObject = pairs.get(ListNew.get(j));
                            if (currentDistance < getDistance( oldObject, ListNew.get(j) ) ){
                                //save indexes with minimal distance
                                indexOld = i;
                                indexNew = j;
                            }
                        }
                    }
                }

                //indexOld contains index of old object index in ListOld
                //indexNew contains index of old object index in ListNew

                if (minDistance < MergeRadius ) {
                    if (pairs.containsKey(ListNew.get(indexNew)))
                        pairs.replace(ListNew.get(indexNew), ListOld.get(indexOld));
                    else
                        pairs.put(ListNew.get(indexNew), ListOld.get(indexOld));

                    ListNew.get(indexNew).setParent(ListOld.get(indexOld));
                    //ListOld.remove(indexOld);
                }
            }
        }

        for (SceneObject object : ListNew){
            if (object.getGeneration() == 0) object.incGeneration();
            for (int i = 0; i < ListOld.size() ; i++) {
                if ( ListOld.get(i).getID() == object.getParentID() ){
                    ListOld.remove(i);
                }
            }
        }

/*
        for (SceneObject object : ListNew)
            if (object.getGeneration() == 0) object.incGeneration();/*
        /*for (SceneObject object : ListOld)
            object.incGeneration();*/
/*
        for (SceneObject object : ListOld){
            ObjectPool.remove(object.getID());
        }
*/
        return ListOld;
    }

    /**
     * Method calculates distances between two objects on the screen
     * @param Obj1 first object
     * @param Obj2 second object
     * @return distance in pixels between objects
     */
    private double getDistance(SceneObject Obj1, SceneObject Obj2){

        double result = 0;

        //getByKey position
        Point Point1 = Obj1.getBounds().getLocation();
        Point Point2 = Obj2.getBounds().getLocation();

        double A = Point1.getY()-Point2.getY();
        double B = Point1.getX()-Point2.getX();

        result = Math.sqrt( Math.pow(A,2)+Math.pow(B,2) );

        return result;
    }

    public List<SceneObject> getDeadObjects() {
        return deadObjects;
    }

    public void resetMap(){
        ObjectPool = new HashMapContainer();
    }
}
