package models;

/**
 * Created by leonardj on 10/3/14.
 */
public class RawModel {

    private int vaoID;
    private int vertexCount;

    public  RawModel(int vaoID, int vertexCount){
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }
}
