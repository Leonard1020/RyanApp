package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import java.awt.event.MouseListener;

/**
 * Created by leonardj on 10/3/14.
 */
public class Camera {

    private Vector3f position = new Vector3f(0, 4, 0);
    private float pitch;
    private float yaw;
    private float roll;

    public void move(){
        if (Keyboard.isKeyDown(Keyboard.KEY_W)){
            position.z -= 0.25f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)){
            position.z += 0.25f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)){
            position.x -= 0.25f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)){
            position.x += 0.25f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            position.y += 0.25f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            position.y -= 0.25f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)){
            yaw += 1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)){
            yaw -= 1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_2)){
            pitch -= 1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F)){
            pitch += 1;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
