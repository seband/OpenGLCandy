package engine.model;

public class SquareModel extends Model{
    public SquareModel(int program){
        super();
        this.vertices = new float[]{
                -1,-1,0,
                -1,1, 0,
                1,1, 0,
                1,-1, 0};
        this.textureCoords = new float[]{
                0, 0,
                0, 1,
                1, 1,
                1, 0};
        this.indicies = new int[] {0, 1, 2, 0, 2, 3};
        this.GenerateBuffers(program);
    }
}
