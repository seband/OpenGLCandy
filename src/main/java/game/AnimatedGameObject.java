package game;

import engine.GameObject;
import engine.model.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Main game object for testing
 */
public class AnimatedGameObject extends GameObject {
    public AnimatedGameObject(Model model) {
        super(model);
    }

    @Override
    public void update() {
        transform.rot = new Matrix4f().rotateY(0.005f).mul(transform.rot);
    }
}
