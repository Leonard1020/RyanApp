package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by leonardj on 10/3/14.
 */
public class Entity {

    private TexturedModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;

    private int textureIndex = 0;

    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale){
        super();
        this.model = model;
        this.position = position;
        this.rotZ = rotZ;
        this.rotY = rotY;
        this.rotX = rotX;
        this.scale = scale;
    }

    public Entity(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale){
        super();
        this.model = model;
        textureIndex = index;
        this.position = position;
        this.rotZ = rotZ;
        this.rotY = rotY;
        this.rotX = rotX;
        this.scale = scale;
    }

    public float getTextureXOffSet(){
        int column = textureIndex % model.getTexture().getNumOfRows();
        return (float)column / (float)model.getTexture().getNumOfRows();
    }

    public float getTextureYOffSet(){
        int row = textureIndex / model.getTexture().getNumOfRows();
        return (float)row / (float)model.getTexture().getNumOfRows();
    }

    public void increasePosition(float dx, float dy, float dz){
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz){
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
