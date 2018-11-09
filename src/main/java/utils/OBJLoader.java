package utils;
import engine.model.Face;
import engine.model.Model;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.*;

public class OBJLoader {
    public static Model loadModel(int program, File file) throws IOException, FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        Model m = new Model();
        String line;
        while ((line = br.readLine()) != null) {
            String[] split = line.split(" ");
            switch (split[0]) {
              case "o":
              //TODO: Add new model
              break;
              case "v": // Vertex v v1 v2 v3
                    m.vertices.add(new Vector3f(Float.valueOf(split[1]), Float.valueOf(split[2]), Float.valueOf(split[3])));
                    break;
                case "vt": // Texture vt vt1 vt2
                    //TODO: Add textures
                    //m.textureCoordinates.add(new Vector2f(Float.valueOf(split[1]), Float.valueOf(split[2])));
                    break;
                case "vn": // Normal vn vn1 vn2 vn3
                    m.normals.add(new Vector3f(Float.valueOf(split[1]), Float.valueOf(split[2]), Float.valueOf(split[3])));

                    break;
                case "f": // Face [f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3 ...]
                    for (int i = 1; i < split.length-2; i++) {
                        Face f = new Face();
                        String[] firstGroup = split[1].split("/");
                        String[] secondGroup = split[i+1].split("/");
                        String[] thirdGroup = split[i+2].split("/");
                        if(firstGroup.length > 0){
                            f.vertices.x = Integer.parseInt(firstGroup[0])-1;
                            f.vertices.y = Integer.parseInt(secondGroup[0])-1;
                            f.vertices.z = Integer.parseInt(thirdGroup[0])-1;
                        }

                        if(firstGroup.length > 1 && firstGroup[1].length() > 0){
                            f.textureCoords.x = Integer.parseInt(firstGroup[1])-1;
                            f.textureCoords.y = Integer.parseInt(secondGroup[1])-1;
                            f.textureCoords.z = Integer.parseInt(thirdGroup[1])-1;
                        }
                        if(firstGroup.length > 2 && firstGroup[2].length() > 0) {
                            f.normals.x = Integer.parseInt(firstGroup[2]) - 1;
                            f.normals.y = Integer.parseInt(secondGroup[2]) - 1;
                            f.normals.z = Integer.parseInt(thirdGroup[2]) - 1;
                        }

                        m.faces.add(f);
                    }
                    /*
                    *
                        String[] values = split[i+1].split("/");
                        if(values[0].length() > 0)
                            f.vertices.setComponent(i, Integer.parseInt(values[0])-1);
                        if(values.length == 2){
                            if(values[1].length() > 0)
                                f.normals.setComponent(i, Integer.parseInt(values[1])-1);
                        }else{
                            if(values[1].length() > 0)
                                f.textureCoords.setComponent(i, Integer.parseInt(values[1])-1);
                            if(values[2].length() > 0)
                                f.normals.setComponent(i, Integer.parseInt(values[2])-1);
                        }
                       */
                    break;

                default: // Not supported
                    break;
            }
        }
        br.close();
        m.GenerateBuffers(program);
        return m;
    }
}
