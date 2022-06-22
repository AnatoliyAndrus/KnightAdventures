package objects;

import main.GamePanel;

import java.awt.*;

public class Torch extends StaticObject {

    public Torch(GamePanel gp, String folderName){
        super(gp, folderName);

        switch (folderName) {
            case "torch_block" -> {
                this.collision = true;
                addImage("torch_block_1");
                addImage("torch_block_2");
                addImage("torch_block_3");
                addImage("torch_block_4");
                noAnimation = setImage("objects/torch_block/torch_1");

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
                defaultCollisionAreaY = 8;
                areaOfCollision = new Rectangle(0, 8, 144, 40);
            }
            case "torch_right" -> {
                this.collision = false;
                addImage("torch_right_1");
                addImage("torch_right_2");
                addImage("torch_right_3");
                noAnimation = setImage("objects/torch_right/torch_right_noAnimation");

                defaultCollisionAreaX = -96;
                defaultCollisionAreaY = 8;
                areaOfCollision = new Rectangle(-96, 8, 144, 40);
            }
        }

        isAnimated = StaticObject.ANIMATION_CONTINUOUSLY;
        framesToChangeSprite = 10;
    }

}
