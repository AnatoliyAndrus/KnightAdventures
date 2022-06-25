package objects;

import main.GamePanel;

import java.awt.*;

public class BossDoor extends StaticObject{
    public BossDoor(GamePanel gp, String initialState) {
        super(gp, "boss_door");

        if ("closed".equals(initialState)) {
            this.collision = true;
            this.noAnimation = setImage("objects/boss_door/boss_door_1");
        } else {
            this.collision = false;
            this.noAnimation = setImage("objects/boss_door/boss_door_10");
        }

        addImage("boss_door_1");
        addImage("boss_door_2");
        addImage("boss_door_3");
        addImage("boss_door_4");
        addImage("boss_door_5");
        addImage("boss_door_6");
        addImage("boss_door_7");
        addImage("boss_door_8");
        addImage("boss_door_9");
        addImage("boss_door_10");

        this.defaultCollisionAreaX = 0;
        this.defaultCollisionAreaY = 0;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, gp.squareSize * 3, gp.squareSize);

        animation = StaticObject.NO_ANIMATION;
        framesToChangeSprite = 26;
    }
}
