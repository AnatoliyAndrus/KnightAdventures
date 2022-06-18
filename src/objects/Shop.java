package objects;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Shop extends GameObject{

    public Shop(GamePanel gp) {
        super(gp, "shop", "shop");

        this.collision = true;
        this.defaultCollisionAreaX = 15;
        this.defaultCollisionAreaY = 30;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 308, 170);
    }
}



//336 240