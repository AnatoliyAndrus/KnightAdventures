package objects;

import main.GamePanel;

import java.awt.*;

public class WoodenBox extends StaticObject {

    public WoodenBox(GamePanel gp){
        super(gp, "woodenBox");

        this.down1 = setImage("objects/box");

        this.collision = true;
        this.defaultCollisionAreaX = 0;
        this.defaultCollisionAreaY = 10;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 48, 38);
    }
}
