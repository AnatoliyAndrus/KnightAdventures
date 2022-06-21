package objects;

import main.GamePanel;

import java.awt.*;

public class Shop extends StaticObject {

    public Shop(GamePanel gp) {
        super(gp, "shop");

        this.down1 = setImage("objects/shop");

        this.collision = true;
        this.defaultCollisionAreaX = 15;
        this.defaultCollisionAreaY = 30;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 308, 175);
    }
}