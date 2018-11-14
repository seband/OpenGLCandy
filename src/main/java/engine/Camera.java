package engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Transform transform = new Transform();
    private Matrix4f projectionMatrix, viewMatrix;
    private float fov, aspectRatio, near, far;

    public Camera(float fov, float aspectRatio, float near, float far){
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.near = near;
        this.far = far;
        this.projectionMatrix = new Matrix4f().perspective(fov, aspectRatio, near, far);
        this.viewMatrix = new Matrix4f().identity();
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    public float getFov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }


    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setPosition(Vector3f position){
        this.transform.position = position;
    }
    public Vector3f getPosition(){
        return transform.position;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    /**
     * Set lookAt based on vector
     * @param v point to look at
     */
    public void setLookAt(Vector3f v){
        viewMatrix = new Matrix4f().setLookAt(transform.position, v, transform.position.cross(v).normalize());
    }
}
