package engine;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowCloseCallback;

public class Camera {
    private Transform transform = new Transform();
    private Matrix4f projectionMatrix, viewMatrix;
    private float fov, aspectRatio, near, far;
    private Vector3f up = new Vector3f(0,1,0);
    private Vector3f forward = new Vector3f(0,0,1);
    double yAngle = Math.PI/2.0f;
    double xAngle = 0;
    public Camera(float fov, float aspectRatio, float near, float far){
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.near = near;
        this.far = far;
        this.projectionMatrix = new Matrix4f().perspective(fov, aspectRatio, near, far);
        updateViewMatrix();
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
        updateViewMatrix();
    }
    private void updateViewMatrix(){

        this.forward = new Vector3f(
                this.transform.position.x +(float) Math.cos(yAngle),
                this.transform.position.y +(float) Math.tan(xAngle),
                this.transform.position.z +(float) -Math.sin(yAngle));
        this.viewMatrix = new Matrix4f().lookAt(transform.position, forward, up);
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
        updateViewMatrix();
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
        viewMatrix = new Matrix4f().lookAt(transform.position, v, transform.position.cross(v).normalize());
    }

    float speed = 0.005f;
    public void update(float deltaT) {
        speed = InputHandler.keyDown(GLFW.GLFW_KEY_LEFT_SHIFT)? 0.01f : 0.001f;
        float relativeSpeed = speed*deltaT;
        if(InputHandler.keyDown(GLFW.GLFW_KEY_A))
            yAngle-=relativeSpeed;
        if(InputHandler.keyDown(GLFW.GLFW_KEY_D))
            yAngle+=relativeSpeed;

        if(InputHandler.keyDown(GLFW.GLFW_KEY_E))
            this.transform.position.add(new Vector3f(0,relativeSpeed,0));

        if(InputHandler.keyDown(GLFW.GLFW_KEY_Q))
            this.transform.position.sub(new Vector3f(0,relativeSpeed,0));

        if(InputHandler.keyDown(GLFW.GLFW_KEY_W))
            this.transform.position.add(new Vector3f(relativeSpeed*(float)Math.cos(yAngle),0,-relativeSpeed*(float)Math.sin(yAngle)));

        if(InputHandler.keyDown(GLFW.GLFW_KEY_S))
            this.transform.position.sub(new Vector3f(relativeSpeed*(float)Math.cos(yAngle),0,-relativeSpeed*(float)Math.sin(yAngle)));

        updateViewMatrix();
    }
}
