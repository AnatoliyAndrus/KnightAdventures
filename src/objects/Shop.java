/**
 * @author Anatolii Andrusenko, Vladislav Marchenko, Andrii Sulimenko
 *
 * @version 1.0
 *
 * class for shop.
 */
package objects;

import main.GamePanel;

import java.awt.*;

public class Shop extends StaticObject {

    /**
     * constructor
     * @param gp game panel
     */
    public Shop(GamePanel gp) {
        super(gp, "shop");

        addImage("shop_1");
        noAnimation = setImage("objects/shop/shop_1");
        animation = StaticObject.NO_ANIMATION;
        framesToChangeSprite = 60;

        this.collision = true;
        this.defaultCollisionAreaX = 15;
        this.defaultCollisionAreaY = 30;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 308, 175);
    }
}