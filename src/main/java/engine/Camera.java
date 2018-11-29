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
    private double yAngle = Math.PI/2.0f;
    private double xAngle = 0;
    private float speed = 0.005f;

    /**
     * Camera used for handling visualizing the 3D-world with proper perspective
     * @param fov
     * @param aspectRatio
     * @param near
     * @param far
     */
    public Camera(float fov, float aspectRatio, float near, float far){
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.near = near;
        this.far = far;
        this.projectionMatrix = new Matrix4f().perspective(fov, aspectRatio, near, far);
        updateViewMatrix();
    }

    /**
     * Get camera's transform
     * @return
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * Set camera's transform
     * @param transform
     */
    public void setTransform(Transform transform) {
        this.transform = transform;
        updateViewMatrix();
    }
    private void updateViewMatrix(){

        Vector3f forward = new Vector3f(
                this.transform.position.x + (float) Math.cos(yAngle),
                this.transform.position.y + (float) Math.tan(xAngle),
                this.transform.position.z + (float) -Math.sin(yAngle));
        this.viewMatrix = new Matrix4f().lookAt(transform.position, forward, up);
    }
    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    /**
     * Get camera's fov
     * @return fov-value
     */
    public float getFov() {
        return fov;
    }

    /**
     * Set camera's fov
     * @param fov
     */
    public void setFov(float fov) {
        this.fov = fov;
    }

    /**
     * Get camera's aspect ratio
     * @return
     */
    public float getAspectRatio() {
        return aspectRatio;
    }

    /**
     * Set camera's aspect ratio
     * @param aspectRatio
     */
    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    /**
     * Get camera's projection matrix
     * @return
     */
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    /**
     * Set camera's position
     * @param position
     */
    public void setPosition(Vector3f position){
        this.transform.position = position;
        updateViewMatrix();
    }

    /**
     * Get camera's position
     * @return
     */
    public Vector3f getPosition(){
        return transform.position;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    /**
     * Set camera's view matrix
     * @param viewMatrix
     */
    public void setViewMatrix(Matrix4f viewMatrix) {
        this.viewMatrix = viewMatrix;
    }

    /**
     * Set lookAt based on vector
     * @param v point to look at
     */
    public void setLookAt(Vector3f v){
        viewMatrix = new Matrix4f().lookAt(transform.position, v, up);
    }

    /**
     * Get an orthographic projection matrix
     * @return orthographic projection matrix
     */
    public Matrix4f getOrtho(){
        float tempNear = 10.0f;
        float tempFar = 80.0f;
        float height = (float)Math.tan((double)fov / 2.0) * (tempFar + tempNear) / 4.0f;
        float left = -height * aspectRatio;
        float right = height * aspectRatio;
        float bottom = -height;
        float top = height;
        return new Matrix4f().ortho(left,right,bottom,top,tempNear,tempFar);
    }

    /**
     * Update function (called once before each render)
     * @param deltaT
     */
    void update(float deltaT) {
        speed = InputHandler.keyDown(GLFW.GLFW_KEY_LEFT_SHIFT)? 0.001f : 0.01f;
        float relativeSpeed = speed*deltaT;
        if(InputHandler.keyDown(GLFW.GLFW_KEY_A))
            this.transform.position.add(new Vector3f(relativeSpeed*(float)Math.sin(yAngle),0,relativeSpeed*(float)Math.cos(yAngle)));
        if(InputHandler.keyDown(GLFW.GLFW_KEY_D))
            this.transform.position.sub(new Vector3f(relativeSpeed*(float)Math.sin(yAngle),0,relativeSpeed*(float)Math.cos(yAngle)));

        xAngle-=InputHandler.getMousePos().y*deltaT*0.0003f;
        yAngle+=InputHandler.getMousePos().x*deltaT*0.0003f;

        if(InputHandler.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL))
            this.transform.position.sub(new Vector3f(0,relativeSpeed,0));

        if(InputHandler.keyDown(GLFW.GLFW_KEY_SPACE))
            this.transform.position.add(new Vector3f(0,relativeSpeed,0));

        if(InputHandler.keyDown(GLFW.GLFW_KEY_W))
            this.transform.position.add(new Vector3f(relativeSpeed*(float)Math.cos(yAngle),relativeSpeed*(float)Math.tan(xAngle),-relativeSpeed*(float)Math.sin(yAngle)));

        if(InputHandler.keyDown(GLFW.GLFW_KEY_S))
            this.transform.position.sub(new Vector3f(relativeSpeed*(float)Math.cos(yAngle),relativeSpeed*(float)Math.tan(xAngle),-relativeSpeed*(float)Math.sin(yAngle)));

        updateViewMatrix();
    }
}
