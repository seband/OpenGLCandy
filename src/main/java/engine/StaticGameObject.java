package engine;

import engine.GameObject;
import engine.model.Model;

public class StaticGameObject extends GameObject {

    /**
     * Static game object
     * @param m base model
     */
    public StaticGameObject(Model m){
        super(m);
    }

    /**
     * Static game object does nothing on update
     */
    @Override
    public void update() {

    }
}
