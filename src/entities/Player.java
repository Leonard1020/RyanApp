package entities;

import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import terrain.Terrain;

/**
 * Created by leonardj on 11/8/2014.
 */
public class Player extends Entity{

    private static final float WALK_SPEED = 40;
    private static final float SPRINT_SPEED = 60;
    private static final float CROUCH_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -80;
    private static final float JUMP_POWER = 25;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private boolean isInAir = false;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(Terrain terrain){
        checkInputs();
        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float)(distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float)(distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx, 0, dz);
        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
        if (super.getPosition().y < terrainHeight){
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = terrainHeight;
        }
    }

    private void jump(){
        if (!isInAir){
            upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkInputs(){
        if (Keyboard.isKeyDown(Keyboard.KEY_W)){
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                currentSpeed = SPRINT_SPEED;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
                currentSpeed = CROUCH_SPEED;
            } else {
            currentSpeed = WALK_SPEED;
            }
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
                currentSpeed = -CROUCH_SPEED;
            } else {
                currentSpeed = -WALK_SPEED;
            }
        } else {
            currentSpeed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)){
            currentTurnSpeed = TURN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)){
            currentTurnSpeed = -TURN_SPEED;
        } else {
            currentTurnSpeed = 0;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            jump();
        }
    }
}
