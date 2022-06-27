package objects;

import main.GamePanel;

import java.awt.*;

public class Lever extends StaticObject{

    public Lever(GamePanel gp) {
        super(gp, "lever");

        addImage("lever_1");
        addImage("lever_2");
        addImage("lever_3");
        addImage("lever_4");
        noAnimation = setImage("objects/lever/lever_1");
        animation = StaticObject.NO_ANIMATION;
        framesToChangeSprite = 8;

        this.collision = false;
        this.defaultCollisionAreaX = 0;
        this.defaultCollisionAreaY = 0;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 48, 48);
    }
}
