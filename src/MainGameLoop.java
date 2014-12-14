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

    private String heightMap = "heightMap";
    private String blendMap = "blendMap";
    private List<Entity> entities = new ArrayList<Entity>();
    private Terrain terrain;
    private Loader loader = new Loader();
    private Random random = new Random();

    public static void main(String[] args){
        new MainGameLoop();
    }

    public MainGameLoop(){
        DisplayManager.createDisplay();
        createWorld();

        entities.add(createEntity(createModel("shack", 1, true, false), 30, -20, 0, 1.75f));
        entities.add(createEntity(createModel("reelmower", 1, true, false), -20, -20, 0, 1));
        entities.add(createEntity(createModel("wheelbarrow", 1, true, false), -10, -20, 0, 1));
        entities.add(createEntity(createModel("shed", 1, true, false), 10, -20, 0, 1));
        entities.add(createEntity(createModel("desk", 1, true, false), 0, -20, 0, 1.15f));
        entities.add(createEntity(createModel("shovel", 1, true, false), -5, -20, 0, .75f));
        entities.add(createEntity(createModel("fence", 1, true, false), random.nextFloat() * 100 - 50, -40, -90, 1));

        //////////////////////PLAYER/////////////////////////
        TexturedModel playerModel = createModel("midpolyPerson", 1, false, false);
        Player player = new Player(playerModel, new Vector3f(0, 0, -10), 0, 0, 0, 1.1f);

        ///////////////LIGHTING AND CAMERA///////////////////
        Light light = new Light(new Vector3f(10000, 40000, 30000), new Vector3f(1, 1, 1));
        Camera camera = new Camera(player);

        MasterRenderer renderer = new MasterRenderer();
        while(!Display.isCloseRequested()){
            camera.move();
            player.move(terrain);

            renderer.processEntity(player);
            renderer.processTerrain(terrain);
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
        float yPosition = terrain.getHeightOfTerrain(xPosition, zPosition);
        return new Entity(model, new Vector3f(xPosition, yPosition, zPosition), 0, rotation, 0, scale);
    }

    private Entity createEntity(TexturedModel model, int textureNum, float xPosition, float zPosition, float rotation, float scale){
        float yPosition = terrain.getHeightOfTerrain(xPosition, zPosition);
        return new Entity(model, textureNum, new Vector3f(xPosition, yPosition, zPosition), 0, rotation, 0, scale);
    }

    private TexturedModel createModel(String object, int textureRows, boolean transparency, boolean fakeLight) {
        RawModel model;
        try {
            ModelData data = OBJFileLoader.loadOBJ(object);
            model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        } catch (ArrayIndexOutOfBoundsException e) {
            model = OBJLoader.loadObjModel(object, loader);
        }
        ModelTexture texture = new ModelTexture(loader.loadTexture(object + "Texture"));
        texture.setNumOfRows(textureRows);
        TexturedModel staticModel = new TexturedModel(model, texture);
        staticModel.getTexture().setHasTransparency(transparency);
        staticModel.getTexture().setUseFakeLighting(fakeLight);
        return staticModel;
    }

    private void createWorld(){
        loadTerrain("grass", "thingrass", "dirt", "gravel", blendMap);
        TexturedModel tree = createModel("treewithleaves", 1, false, false);
        TexturedModel lptree = createModel("lowpolytree", 1, false, false);
        TexturedModel grass = createModel("grass", 4, true, true);
        TexturedModel fern = createModel("fern", 2, true, true);
        for (int i = 0; i < 2000; i++){
            float x = random.nextFloat() * 1500 - 750;
            float z = random.nextFloat() * 1500 - 750;
            float rotation = random.nextFloat() * 360;
            if (i < 10){
                entities.add(createEntity(tree, x, z, rotation, 2));
            } else if (i < 100){
                entities.add(createEntity(fern, random.nextInt(4), x, z, rotation, 1));
            } else if (i < 1000){
                Entity e = createEntity(lptree, x, z, rotation, 1);
                e.setScale(random.nextFloat() * 1.2f);
                if (e.getScale() < .3f){
                    e.setScale(e.getScale() + .4f);
                }
                entities.add(e);
            } else {
                entities.add(createEntity(grass, 0, x, z, rotation, 1.2f));
                x = random.nextFloat() * 1500 - 750;
                z = random.nextFloat() * 1500 - 750;
                entities.add(createEntity(grass, 4, x, z, rotation, 1.2f));
                if (i%7 == 0){
                    x = random.nextFloat() * 1500 - 750;
                    z = random.nextFloat() * 1500 - 750;
                    entities.add(createEntity(grass, 1, x, z, rotation, 1.5f));
                }
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

        terrain = new Terrain(-.5f, -.5f, loader, texturePack, blendMap, heightMap);
    }
}
