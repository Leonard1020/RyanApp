package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by leonardj on 10/3/14.
 */
public class Camera {

    private float distanceFromPlayer = 40;
    private float angleAroundPlayer = 0;
    private float yOffest = 6;

    private Vector3f position = new Vector3f(0, 4, 10);
    private float pitch = 20;
    private float yaw = 0;
    private float roll;

    private Player player;

    public Camera(Player player){
        this.player = player;
    }

    public void move(){
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
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

    private void calculateCameraPosition(float horizontal, float vertical){
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float)(horizontal * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float)(horizontal * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + vertical + yOffest;
        if (position.y < 0){
            position.y = 0;
        }
    }

    private float calculateHorizontalDistance(){
        return (float)(distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance(){
        return (float)(distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateZoom(){
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        distanceFromPlayer -= zoomLevel;
        if (distanceFromPlayer < -1){
            distanceFromPlayer = -1;
        } else if (distanceFromPlayer > 150){
            distanceFromPlayer = 150;
        }
    }

    private void calculatePitch(){
        if(Mouse.isButtonDown(0)){
            float pitchChange = Mouse.getDY() * 0.3f;
            pitch -= pitchChange;
        }
    }

    private void calculateAngleAroundPlayer(){
        if(Mouse.isButtonDown(0)){
            float angleChange = Mouse.getDX() * 0.3f;
            angleAroundPlayer -= angleChange;
        }
    }
}
