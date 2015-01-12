package gui;

import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;

/**
 * Created by leonardj on 1/11/2015.
 */
public class GUIShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/gui/GUIVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/gui/GUIFragmentShader.txt";

    private int location_transformationMatrix;

    public GUIShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
