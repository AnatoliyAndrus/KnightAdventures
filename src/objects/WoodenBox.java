package objects;

import main.GamePanel;

import java.awt.*;

public class WoodenBox extends StaticObject {

    public WoodenBox(GamePanel gp){
        super(gp, "woodenBox");

        addImage("woodenBox_1");
        noAnimation = setImage("objects/woodenBox/woodenBox_1");
        animation = StaticObject.NO_ANIMATION;
        framesToChangeSprite = 60;

        this.collision = true;
        this.defaultCollisionAreaX = 0;
        this.defaultCollisionAreaY = 10;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 48, 38);
    }
}
