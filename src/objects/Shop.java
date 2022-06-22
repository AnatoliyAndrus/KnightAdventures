package objects;

import main.GamePanel;

import java.awt.*;

public class Shop extends StaticObject {

    public Shop(GamePanel gp) {
        super(gp, "shop");

        addImage("shop_1");
        noAnimation = setImage("objects/shop/shop_1");
        isAnimated = StaticObject.NO_ANIMATION;
        framesToChangeSprite = 60;

        this.collision = true;
        this.defaultCollisionAreaX = 15;
        this.defaultCollisionAreaY = 30;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 308, 175);
    }
}