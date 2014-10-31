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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by leonardj on 10/3/14.
 */
public class MainGameLoop {

    public static void main(String[] args){
        new MainGameLoop();
    }

    public MainGameLoop(){
        DisplayManager.createDisplay();
        Loader loader = new Loader();

        /**
         * Shack Entity
         */
        RawModel shackModel = OBJLoader.loadObjModel("shack", loader);
        TexturedModel staticShackModel = new TexturedModel(shackModel, new ModelTexture(loader.loadTexture("shackTexture")));
        ModelTexture shackTexture = staticShackModel.getTexture();
        Entity shackEntity = new Entity(staticShackModel, new Vector3f(22, 0, -20), 0, 0, 0, 1.5f);

        /**
         * Desk Entity
         */
        RawModel deskModel = OBJLoader.loadObjModel("desk", loader);
        TexturedModel staticDeskModel = new TexturedModel(deskModel, new ModelTexture(loader.loadTexture("oldwood")));
        ModelTexture deskTexture = staticDeskModel.getTexture();
        staticDeskModel.getTexture().setHasTransparency(true);
        Entity deskEntity = new Entity(staticDeskModel, new Vector3f(7, 0, -20), 0, 0, 0, 1.15f);

        /**
         * Reel Mower Entity
         */
        RawModel reelMowerModel = OBJLoader.loadObjModel("reelmower", loader);
        TexturedModel staticReelMowerModel = new TexturedModel(reelMowerModel, new ModelTexture(loader.loadTexture("reelmowerTexture")));
        ModelTexture reelMowerTexture = staticReelMowerModel.getTexture();
        Entity reelMowerEntity = new Entity(staticReelMowerModel, new Vector3f(0, 0, -20), 0, 0, 0, 1);

        /**
         * Shed Entity
         */
        RawModel shedModel = OBJLoader.loadObjModel("shed", loader);
        TexturedModel staticShedModel = new TexturedModel(shedModel, new ModelTexture(loader.loadTexture("shedTexture")));
        ModelTexture shedTexture = staticShedModel.getTexture();
        staticShedModel.getTexture().setHasTransparency(true);
        //staticShedModel.getTexture().setUseFakeLighting(true);
        Entity shedEntity = new Entity(staticShedModel, new Vector3f(-13, 0, -20), 0, 0, 0, 1);

        /**
         * Shovel Entity
         */
        RawModel shovelModel = OBJLoader.loadObjModel("shovel", loader);
        TexturedModel staticShovelModel = new TexturedModel(shovelModel, new ModelTexture(loader.loadTexture("shovelTexture")));
        ModelTexture shovelTexture = staticShovelModel.getTexture();
        Entity shovelEntity = new Entity(staticShovelModel, new Vector3f(-3, 0, -20), 0, 0, 0, .75f);

        /**
         * Creates grass, trees, and ferns to fill the world
         */
        //tree
        RawModel treeModel = OBJLoader.loadObjModel("treewithleaves", loader);
        TexturedModel tree = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("treeTexture")));
        //lowpolytree
        RawModel lptreeModel = OBJLoader.loadObjModel("lowpolytree", loader);
        TexturedModel lptree = new TexturedModel(lptreeModel, new ModelTexture(loader.loadTexture("lowpolytreeTexture")));
        //grass
        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grass", loader), new ModelTexture(loader.loadTexture("grassTexture")));
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        //fern
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), new ModelTexture(loader.loadTexture("fern")));
        fern.getTexture().setHasTransparency(true);
        fern.getTexture().setUseFakeLighting(true);


        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        for (int i = 0; i < 2000; i++){
            entities.add(new Entity(grass, new Vector3f(random.nextFloat()*1500-750, 0, random.nextFloat()*1500-750), 0, 0, 0, 1));
            entities.add(new Entity(grass, new Vector3f(random.nextFloat()*1500-750, 0, random.nextFloat()*1500-750), 0, 0, 0, 1));
            if (i > 500){
                Entity e = new Entity(lptree, new Vector3f(random.nextFloat()*1500-750, 0, random.nextFloat()*1500-750), 0, 0, 0, 1);
                e.setRotY(random.nextFloat() * 100);
                e.setScale(random.nextFloat() * 1.2f);
                if (e.getScale() < .3f){
                    e.setScale(e.getScale() + .4f);
                }
                entities.add(e);
                //entities.add(new Entity(lptree, new Vector3f(random.nextFloat()*800-400, 0, random.nextFloat()*800-400), 0, 0, 0, 1));
            }
            if (i > 1995){
                entities.add(new Entity(tree, new Vector3f(random.nextFloat()*500-250, 0, random.nextFloat()*500-250), 0, 0, 0, 2));
            }
        }

        //adds the single entities to the list
        entities.add(shackEntity);
        entities.add(deskEntity);
        entities.add(reelMowerEntity);
        entities.add(shedEntity);
        entities.add(shovelEntity);

        Terrain terrain = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2 = new Terrain(-1, 0, loader, new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain3 = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain4 = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));

        Light light = new Light(new Vector3f(10000, 50000, 50000), new Vector3f(1, 1, 1));
        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer();
        while(!Display.isCloseRequested()){
            shovelEntity.increaseRotation(0, .2f, 0);
            reelMowerEntity.increaseRotation(0, .2f, 0);
            deskEntity.increaseRotation(0, .2f, 0);

            camera.move();

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processTerrain(terrain3);
            renderer.processTerrain(terrain4);

            for (Entity entity : entities){
                renderer.processEntity(entity);
            }

            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
