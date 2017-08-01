package View;

import model.imageProcessing.SceneObject;
import org.junit.Assert;
import org.junit.Test;
import view.Controller;

import java.util.List;


/**
 * Created by July on 31.07.2017.
 */
public class ControllerTest {
    @Test
    public void getPercent_checkingCorrectPercent_correctPercent(){
        int[][] templateL = {{1, 1, 1, 1},
                            {1, 0, 0, 1},
                            {1, 0, 0, 1},
                            {1, 0, 0, 1},
                            {1, 1, 1, 1}};
        int[][] image ={{0, 0, 1, 1},
                        {0, 1, 0, 1},
                        {0, 0, 0, 1},
                        {0, 1, 0, 1},
                        {0, 0, 1, 1}};
        Controller contr = new Controller();
        double result = contr.getPercent(templateL, 0, 0, image);
        Assert.assertEquals(90, result, 0);
    }

    @Test
    public void catchSquare_gettingObject_foundObject(){
        int[][] templateL = {{0, 0, 0, 0},
                            {0, 1, 1, 0},
                            {0, 1, 1, 0},
                            {0, 1, 1, 0},
                            {0, 0, 0, 0}};

        int[][] image =   { {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

        Controller contr = new Controller();
        List<SceneObject> objects = contr.catchObject(templateL, image);
        SceneObject object = new SceneObject();
        Assert.assertSame(11, objects.get(0).getBounds().x);
    }
}
