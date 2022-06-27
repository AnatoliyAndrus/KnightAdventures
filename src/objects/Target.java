/**
 * @author Anatolii Andrusenko, Vladislav Marchenko, Andrii Sulimenko
 *
 * @version 1.0
 *
 * class of targets. Targets are part of attack of the boss
 */
package objects;

import main.GamePanel;

import java.awt.*;

public class Target extends StaticObject{

    /**
     * target
     * @param gp game panel
     * @param screenX x position of target
     * @param screenY y position of target
     */
    public Target(GamePanel gp, int screenX, int screenY) {
        super(gp, "target");

        this.screenX = screenX;
        this.screenY = screenY;

        addImage("target_1");
        addImage("target_2");
        addImage("target_3");
        noAnimation = setImage("objects/target/target_1");
        animation = StaticObject.ANIMATION_CONTINUOUSLY;
        framesToChangeSprite = 30;

        this.collision = false;
        this.defaultCollisionAreaX = 0;
        this.defaultCollisionAreaY = 0;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 48, 48);
    }
}
