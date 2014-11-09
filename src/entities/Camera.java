package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import java.awt.event.MouseListener;

/**
 * Created by leonardj on 10/3/14.
 */
public class Camera {

    private Vector3f position = new Vector3f(0, 4, 10);
    private float pitch;
    private float yaw;
    private float roll;

    public void move(){
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)){
            position.z -= 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
            position.z += 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            position.x -= 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
            position.x += 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
            position.y += 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
            position.y -= 0.5f;
        }
        if (Mouse.isButtonDown(0)){
            yaw += (double)Mouse.getDX();
            pitch -= (double) Mouse.getDY();
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
