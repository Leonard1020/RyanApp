import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
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
    Random random;

    public static void main(String[] args){
        new MainGameLoop();
    }

    public MainGameLoop(){
        DisplayManager.createDisplay();
        loader = new Loader();
        random = new Random();
        entities = new ArrayList<Entity>();
        terrains = new ArrayList<Terrain>();

        /**
        entities.add(createEntity(createModel("shack", true, false), random.nextFloat() * 100 - 50, -20, 0, 1.75f));
        entities.add(createEntity(createModel("reelmower", true, false), random.nextFloat() * 100 - 50, -20, 0, 1));
        entities.add(createEntity(createModel("wheelbarrow", true, false), random.nextFloat() * 100 - 50, -20, 0, 1));
        entities.add(createEntity(createModel("shed", true, false), random.nextFloat() * 100 - 50, -20, 0, 1));
        entities.add(createEntity(createModel("desk", true, false), random.nextFloat() * 100 - 50, -20, 0, 1.15f));
        entities.add(createEntity(createModel("shovel", true, false), random.nextFloat() * 100 - 50, -20, 0, .75f));
        entities.add(createEntity(createModel("fence", true, false), random.nextFloat() * 100 - 50, -40, -90, 1));
        */

        entities.add(createEntity(createModel("shack", true, false), 30, -20, 0, 1.75f));
        entities.add(createEntity(createModel("reelmower", true, false), -20, -20, 0, 1));
        entities.add(createEntity(createModel("wheelbarrow", true, false), -10, -20, 0, 1));
        entities.add(createEntity(createModel("shed", true, false), 10, -20, 0, 1));
        entities.add(createEntity(createModel("desk", true, false), 0, -20, 0, 1.15f));
        entities.add(createEntity(createModel("shovel", true, false), -5, -20, 0, .75f));
        entities.add(createEntity(createModel("fence", true, false), random.nextFloat() * 100 - 50, -40, -90, 1));

        createWorld();

        //////////////////////PLAYER/////////////////////////
        TexturedModel playerModel = createModel("midpolyPerson", false, false);
        Player player = new Player(playerModel, new Vector3f(0, 0, -10), 0, 0, 0, 1.1f);

        Light light = new Light(new Vector3f(10000, 40000, 30000), new Vector3f(1, 1, 1));
        Camera camera = new Camera(player);

        MasterRenderer renderer = new MasterRenderer();
        while(!Display.isCloseRequested()){
            camera.move();
            player.move();
            renderer.processEntity(player);
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

    private Entity createEntity(TexturedModel model, float xPosition, float zPosition, float rotation, float scale){
        return new Entity(model, new Vector3f(xPosition, 0, zPosition), 0, rotation, 0, scale);
    }

    private TexturedModel createModel(String object, boolean transparency, boolean fakeLight) {
        RawModel model;
        try {
            ModelData data = OBJFileLoader.loadOBJ(object);
            model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        } catch (ArrayIndexOutOfBoundsException e) {
            model = OBJLoader.loadObjModel(object, loader);
        }
        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture(object + "Texture")));
        staticModel.getTexture().setHasTransparency(transparency);
        staticModel.getTexture().setUseFakeLighting(fakeLight);
        return staticModel;
    }

    private void createWorld(){
        loadTerrain("grass", "thingrass", "dirt", "gravel", "blendMap");
        TexturedModel tree = createModel("treewithleaves", false, false);
        TexturedModel lptree = createModel("lowpolytree", false, false);
        TexturedModel grass = createModel("grass", true, true);
        TexturedModel fern = createModel("fern", true, true);
        for (int i = 0; i < 2000; i++){
            if (i < 5){
                entities.add(createEntity(tree, random.nextFloat()*600-300, random.nextFloat()*600-300, 0, 2));
            } else if (i < 50){
                entities.add(createEntity(fern, random.nextFloat()*1500-750, random.nextFloat()*1500-750, 0, 1));
            } else if (i < 1000){
                Entity e = createEntity(lptree, random.nextFloat()*1500-750, random.nextFloat()*1500-750, 0, 1);
                e.setRotY(random.nextFloat() * 1000);
                e.setScale(random.nextFloat() * 1.2f);
                if (e.getScale() < .3f){
                    e.setScale(e.getScale() + .4f);
                }
                entities.add(e);
            } else {
                entities.add(createEntity(grass, random.nextFloat()*1500-750, random.nextFloat()*1500-750, 0, 1));
                entities.add(createEntity(grass, random.nextFloat()*1500-750, random.nextFloat()*1500-750, 0, 1));
            }
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
