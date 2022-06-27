/**
 * @author Anatolii Andrusenko, Vladislav Marchenko, Andrii Sulimenko
 *
 * @version 1.0
 *
 * class of torches
 */
package objects;

import main.GamePanel;

import java.awt.*;

public class Torch extends StaticObject {

    /**
     * constructor
     * @param gp game panel
     * @param folderName name of folder (to define which kind of torch is it)
     */
    public Torch(GamePanel gp, String folderName){
        super(gp, folderName);

        switch (folderName) {
            case "torch_block" -> {
                this.collision = true;
                addImage("torch_block_1");
                addImage("torch_block_2");
                addImage("torch_block_3");
                addImage("torch_block_4");
                noAnimation = setImage("objects/torch_block/torch_block_1");

                defaultCollisionAreaX = 0;
                defaultCollisionAreaY = 10;
                areaOfCollision = new Rectangle(0, 10, 48, 38);
            }
            case "torch_left" -> {
                this.collision = false;
                addImage("torch_left_1");
                addImage("torch_left_2");
                addImage("torch_left_3");
                noAnimation = setImage("objects/torch_left/torch_left_noAnimation");

                defaultCollisionAreaX = 0;
                defaultCollisionAreaY = -17;
                areaOfCollision = new Rectangle(0, 8, 144, 42);
            }
            case "torch_right" -> {
                this.collision = false;
                addImage("torch_right_1");
                addImage("torch_right_2");
                addImage("torch_right_3");
                noAnimation = setImage("objects/torch_right/torch_right_noAnimation");

                defaultCollisionAreaX = -96;
                defaultCollisionAreaY = -17;
                areaOfCollision = new Rectangle(-96, 8, 144, 42);
            }
        }

        animation = StaticObject.ANIMATION_CONTINUOUSLY;
        framesToChangeSprite = 10;
    }

}
