package objects;

import main.GamePanel;

import java.awt.*;

public class Torch extends GameObject{

    public Torch(GamePanel gp){
        super(gp, "torch", "torch");

        this.collision = true;
        this.defaultCollisionAreaX = 0;
        this.defaultCollisionAreaY = 0;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 48, 48);
    }
}
