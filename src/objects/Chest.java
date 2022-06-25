package objects;

import main.GamePanel;

import java.awt.*;

public class Chest extends StaticObject{

    public Chest(GamePanel gp) {
        super(gp, "chest");

        addImage("chest_1");
        addImage("chest_2");
        addImage("chest_3");
        addImage("chest_4");
        noAnimation = setImage("objects/chest/chest_1");
        animation = StaticObject.NO_ANIMATION;
        framesToChangeSprite = 20;

        this.collision = true;
        this.defaultCollisionAreaX = 0;
        this.defaultCollisionAreaY = 10;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 48, 38);
    }
}
