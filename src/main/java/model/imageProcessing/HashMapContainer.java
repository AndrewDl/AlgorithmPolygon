package model.imageProcessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Andrew on 23.03.2017.
 * This class is a wrapper for HashMap<Long, SceneObject> <br>
 *     it incapsulates data about all existing SceneObjects and methods to work with them
 */
public class HashMapContainer {
    private final Map<Long, SceneObject> Objects;

    public HashMapContainer(){
        Objects = new HashMap<>();
    }

    public HashMapContainer(Map<Long, SceneObject> collection){
        Objects = collection;
    }

    public SceneObject getByKey(long Key){
        return  Objects.get(Key);
    }

    public void put(SceneObject object){
        Objects.put(object.getID(),object);
    }

    /**
     * Analog BUT NOT overriding of putAll method
     * @param Objects
     */
    public void putAll(HashMap<Long, SceneObject> Objects){
        this.Objects.putAll(Objects);
    }

    /**
     * Can be used to getByKey the link on contained HashMap with objects
     * @return link on contained HashMap
     */
    public Map<Long, SceneObject> getMap(){
        return Objects;
    }

    /**
     * This method is to getByKey list of objects ID that are parents of
     * @param ID
     * @return
     */
    public List<Long> getParentIDs(long ID){
        List<Long> IDs = new ArrayList<>();

        SceneObject currentObject = Objects.get(ID);

        IDs.add(currentObject.getID());

        if (currentObject.getParentID() != null) {
            IDs.addAll(getParentIDs(currentObject.getParentID()));
        }

        return IDs;
    }

    /**
     * This method is to getByKey list of objects ID that are parents of
     * @param ID
     * @return
     */
    public List<SceneObject> getParentList(long ID){
        List<SceneObject> Parents = new ArrayList<>();

        SceneObject currentObject = Objects.get(ID);

        Parents.add(currentObject);

        if (currentObject.getParentID() != null) {
            Parents.addAll(getParentList(currentObject.getParentID()));
        }

        return Parents;
    }

    /**
     * Finds all elements with given generation.
     * @param generation generation to look for
     * @return list with SceneObjects of given generation
     */
    public List<SceneObject> objectsByGeneration(int generation){
        List<SceneObject> sceneObjects = new ArrayList<>();

        for (SceneObject object : Objects.values()){
            if (object.getGeneration() == generation) sceneObjects.add(object);
        }

        return sceneObjects;
    }

    /**
     * Removes object with given ID and all of its parents.
     * @param ID identificator of object
     */
    public void remove(long ID){
        List<Long> toRemove = getParentIDs(ID);
        for (Long id : toRemove)
            Objects.remove(id);
    }


}
