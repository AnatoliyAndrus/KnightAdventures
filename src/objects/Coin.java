package objects;

import main.GamePanel;

import java.awt.*;

public class Coin extends StaticObject{

    public Coin(GamePanel gp) {
        super(gp, "coin");

        addImage("coin_1");
        addImage("coin_2");
        addImage("coin_3");
        addImage("coin_4");
        addImage("coin_5");
        addImage("coin_6");
        addImage("coin_7");
        addImage("coin_8");
        addImage("coin_9");
        addImage("coin_10");
        addImage("coin_11");
        addImage("coin_12");
        addImage("coin_13");
        addImage("coin_14");
        addImage("coin_15");
        noAnimation = setImage("objects/coin/coin_1");
        animation = StaticObject.ANIMATION_ONCE;
        framesToChangeSprite = 2;

        this.collision = false;
        this.defaultCollisionAreaX = 0;
        this.defaultCollisionAreaY = 48;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 48, 48);
    }
}
