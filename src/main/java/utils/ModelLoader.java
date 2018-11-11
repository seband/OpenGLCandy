package utils;

import engine.model.Model;
import org.lwjgl.assimp.*;
import org.lwjgl.assimp.Assimp;

import java.io.File;

public class ModelLoader {
    /**
     * Loads modelsfrom file
     * @param program for uploading
     * @param file containing models
     * @return models
     */
    public static Model loadModel(int program, File file, boolean flipUVs){
        AIScene scene;
        if(flipUVs)
            scene = Assimp.aiImportFile(file.toString(),
                    Assimp.aiProcess_Triangulate |
                            Assimp.aiProcess_GenSmoothNormals |
                            Assimp.aiProcess_FlipUVs |
                            Assimp.aiProcess_CalcTangentSpace
            );
        else
            scene = Assimp.aiImportFile(file.toString(),
                    Assimp.aiProcess_Triangulate |
                            Assimp.aiProcess_GenSmoothNormals |
                            Assimp.aiProcess_CalcTangentSpace
            );
        AIMesh mesh = AIMesh.create(scene.mMeshes().get(0));
        //Setup arrays
        float vertices[]	 = new float[mesh.mNumVertices() * 3];
        float textureCoords[]	 = new float[mesh.mNumVertices() * 2];
        float normals[]	 = new float[mesh.mNumVertices() * 3];
        int indices[]	 = new int[mesh.mNumFaces() * 3];

        //Array indexes
        int iv = 0; //vertex
        int it = 0; //texture
        int in = 0; //normal
        int ii = 0; //index

        //Populate arrays
        for(int v = 0; v < mesh.mNumVertices(); v++) {
            AIVector3D position = mesh.mVertices().get(v);
            AIVector3D normal = mesh.mNormals().get(v);
            vertices[iv++] = position.x();
            vertices[iv++] = position.y();
            vertices[iv++] = position.z();
            normals[in++] = normal.x();
            normals[in++] = normal.y();
            normals[in++] = normal.z();
            if(mesh.mTextureCoords(0) != null){
                AIVector3D texCoord = mesh.mTextureCoords(0).get(v);
                textureCoords[it++] = texCoord.x();
                textureCoords[it++] = texCoord.y();
            }
        }

        for(int f = 0; f < mesh.mNumFaces(); f++)
        {
            AIFace face = mesh.mFaces().get(f);
            for(int ind = 0; ind < face.mNumIndices(); ind++)
                indices[ii++] = face.mIndices().get(ind);
        }

        Model m = new Model(vertices, normals, textureCoords, indices);
        m.GenerateBuffers(program);
        return m;
    }
}