package objects;

import main.GamePanel;

import java.awt.*;

public class Torch extends StaticObject {

    public Torch(GamePanel gp){
        super(gp, "torch");

        this.down1 = setImage("objects/torch");

        this.collision = true;
        this.defaultCollisionAreaX = 0;
        this.defaultCollisionAreaY = 10;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 48, 38);
    }
}
