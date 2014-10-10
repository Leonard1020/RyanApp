import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
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
        shackTexture.setShineDamper(5);
        shackTexture.setReflectivity(.05f);

        RawModel treeModel = OBJLoader.loadObjModel("treewithleaves", loader);
        TexturedModel staticTreeModel = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("treeTexture")));
        ModelTexture treeTexture = staticTreeModel.getTexture();
        treeTexture.setShineDamper(5);
        treeTexture.setReflectivity(.1f);

        RawModel reelModel = OBJLoader.loadObjModel("reelmower", loader);
        TexturedModel staticReelModel = new TexturedModel(reelModel, new ModelTexture(loader.loadTexture("reelmowerTexture")));
        ModelTexture reelTexture = staticReelModel.getTexture();
        reelTexture.setShineDamper(5);
        reelTexture.setReflectivity(.1f);

        RawModel shedModel = OBJLoader.loadObjModel("shed", loader);
        TexturedModel staticShedModel = new TexturedModel(shedModel, new ModelTexture(loader.loadTexture("shedTexture")));
        ModelTexture shedTexture = staticShedModel.getTexture();
        shedTexture.setShineDamper(0);
        shedTexture.setReflectivity(0);

        RawModel deskModel = OBJLoader.loadObjModel("desk", loader);
        TexturedModel staticDeskModel = new TexturedModel(deskModel, new ModelTexture(loader.loadTexture("oldwood")));
        ModelTexture deskTexture = staticDeskModel.getTexture();
        deskTexture.setShineDamper(0);
        deskTexture.setReflectivity(0);

        RawModel shovelModel = OBJLoader.loadObjModel("shovel", loader);
        TexturedModel staticShovelModel = new TexturedModel(shovelModel, new ModelTexture(loader.loadTexture("shovelTexture")));
        ModelTexture shovelTexture = staticDeskModel.getTexture();
        shovelTexture.setShineDamper(0);
        shovelTexture.setReflectivity(0);

        Entity shackEntity = new Entity(staticShackModel, new Vector3f(5, -3, -40), 0, 0, 0, 1);
        Entity treeEntity = new Entity(staticTreeModel, new Vector3f(10, -3, -30), 0, 0, 0, 1);
        Entity reelEntity = new Entity(staticReelModel, new Vector3f(0, -3, -15), 0, 0, 0, 1);
        Entity shedEntity = new Entity(staticShedModel, new Vector3f(-10, -3, -30), 0, 0, 0, .65f);
        Entity deskEntity = new Entity(staticDeskModel, new Vector3f(-10, -3, -20), 0, 0, 0, 1);
        Entity shovelEntity = new Entity(staticShovelModel, new Vector3f(0, -3, -20), 0, 0, 0, 1);

        Light light = new Light(new Vector3f(0, 0, -10), new Vector3f(1, 1, 1));
        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer();
        while(!Display.isCloseRequested()){
            shackEntity.increaseRotation(0, -.1f, 0);
            treeEntity.increaseRotation(0, -.1f, 0);
            //reelEntity.increaseRotation(0, .1f, 0);
            //shedEntity.increaseRotation(0, -.1f, 0);
            deskEntity.increaseRotation(0, -.1f, 0);
            shovelEntity.increaseRotation(0, -.1f, 0);

            renderer.processEntity(shackEntity);
            renderer.processEntity(treeEntity);
            //renderer.processEntity(reelEntity);
            //renderer.processEntity(shedEntity);
            renderer.processEntity(deskEntity);
            renderer.processEntity(shovelEntity);

            camera.move();
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
