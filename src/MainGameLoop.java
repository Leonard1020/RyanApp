import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by leonardj on 10/3/14.
 */
public class MainGameLoop {

    private List<Entity> entities;
    private List<Terrain> terrains;
    private Loader loader;

    public static void main(String[] args){
        new MainGameLoop();
    }

    public MainGameLoop(){
        DisplayManager.createDisplay();
        loader = new Loader();
        entities = new ArrayList<Entity>();
        terrains = new ArrayList<Terrain>();

        addEntity("shack", true, false, 1.5f, false);
        addEntity("reelmower", false, false, 1, false);
        addEntity("wheelbarrow", true, false, 1, false);
        addEntity("shed", true, false, 1, true);
        addEntity("desk", true, false, 1.15f, true);
        addEntity("shovel", false, false, .75f, true);
        addEntity("fence", true, true, 1, true);

        addEntity("treewithleaves", false, false, 2, false);
        addEntity("lowpolytree", false, false, 1, true);
        addEntity("grass", true, true, 1, true);
        addEntity("fern", true, true, 1, true);

        loadTerrain("grass", "thingrass", "dirt", "gravel", "blendMap");

        Light light = new Light(new Vector3f(10000, 40000, 30000), new Vector3f(1, 1, 1));
        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer();
        while(!Display.isCloseRequested()){

            camera.move();

            for (Terrain terrain : terrains){
                renderer.processTerrain(terrain);
            }

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

    private void addEntity(String object, boolean transparency, boolean fakeLight, float scale, boolean useOBJConverter){
        Random random = new Random();
        RawModel model;
        if (useOBJConverter){
            ModelData data = OBJFileLoader.loadOBJ(object);
            model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        } else {
            model = OBJLoader.loadObjModel(object, loader);
        }
        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture(object + "Texture")));
        staticModel.getTexture().setHasTransparency(transparency);
        staticModel.getTexture().setUseFakeLighting(fakeLight);
        if (object.equals("treewithleaves")){
            for (int i = 0; i < 5; i++){
                entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*600-300, 0, random.nextFloat()*600-300), 0, 0, 0, scale));
            }
        } else if (object.equals("lowpolytree")){
            for (int i = 0; i < 500; i++){
                Entity e = new Entity(staticModel, new Vector3f(random.nextFloat()*1500-750, 0, random.nextFloat()*1500-750), 0, 0, 0, scale);
                e.setRotY(random.nextFloat() * 100);
                e.setScale(random.nextFloat() * 1.2f);
                if (e.getScale() < .3f){
                    e.setScale(e.getScale() + .4f);
                }
                entities.add(e);
            }
        } else if (object.equals("grass")){
            for (int i = 0; i < 4000; i++){
                entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*1500-750, 0, random.nextFloat()*1500-750), 0, 0, 0, scale));
            }
        } else if (object.equals("fern")){
            for (int i = 0; i < 100; i++){
                entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*1500-750, 0, random.nextFloat()*1500-750), 0, 0, 0, scale));
            }
        } else {
            entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*100-50, 0, -20), 0, 0, 0, scale));
        }
    }

    private void loadTerrain(String background, String r, String g, String b, String blend){
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture(background));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture(r));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture(g));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture(b));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture(blend));

        terrains.add(new Terrain(0, -1, loader, texturePack, blendMap));
        terrains.add(new Terrain(-1, -1, loader, texturePack, blendMap));
        terrains.add(new Terrain(0, 0, loader, texturePack, blendMap));
        terrains.add(new Terrain(-1, 0, loader, texturePack, blendMap));
    }
}
