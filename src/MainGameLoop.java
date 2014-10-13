import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import terrain.Terrain;
import textures.ModelTexture;

/**
 * Created by leonardj on 10/3/14.
 */
public class MainGameLoop {

    public static void main(String[] args){
        DisplayManager.createDisplay();
        Loader loader = new Loader();

        RawModel shackModel = OBJLoader.loadObjModel("shack", loader);
        TexturedModel staticShackModel = new TexturedModel(shackModel, new ModelTexture(loader.loadTexture("shackTexture")));
        ModelTexture shackTexture = staticShackModel.getTexture();
        //shackTexture.setShineDamper(5);
        //shackTexture.setReflectivity(.05f);

        Entity shackEntity = new Entity(staticShackModel, new Vector3f(0, 0, -20), 0, 0, 0, 1);

        Terrain terrain = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2 = new Terrain(-1, 0, loader, new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain3 = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain4 = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));

        Light light = new Light(new Vector3f(300, 200, 200), new Vector3f(1, 1, 1));
        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer();
        while(!Display.isCloseRequested()){
            shackEntity.increaseRotation(0, .1f, 0);

            camera.move();

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processTerrain(terrain3);
            renderer.processTerrain(terrain4);
            renderer.processEntity(shackEntity);

            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
