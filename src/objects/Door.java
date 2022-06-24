package objects;
import main.GamePanel;
import java.awt.*;

public class Door extends StaticObject {

    public Door(GamePanel gp, String relatedRoom, String initialDoorState, String folderName) {
        super(gp, folderName);

        switch (folderName) {
            case "door_horizontal" -> {
                addImage("door_horizontal_1");
                addImage("door_horizontal_2");
                addImage("door_horizontal_3");
                addImage("door_horizontal_4");
                addImage("door_horizontal_5");
                addImage("door_horizontal_6");
                addImage("door_horizontal_7");
                addImage("door_horizontal_8");
                addImage("door_horizontal_9");
                addImage("door_horizontal_10");

                this.defaultCollisionAreaX = 0;
                this.defaultCollisionAreaY = gp.squareSize;
                this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, gp.squareSize * 3, gp.squareSize);
            }
            case "door_vertical" -> {
                addImage("door_vertical_1");
                addImage("door_vertical_2");
                addImage("door_vertical_3");
                addImage("door_vertical_4");
                addImage("door_vertical_5");
                addImage("door_vertical_6");
                addImage("door_vertical_7");
                addImage("door_vertical_8");
                addImage("door_vertical_9");
                addImage("door_vertical_10");

                this.defaultCollisionAreaX = 0;
                this.defaultCollisionAreaY = gp.squareSize;
                this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, gp.squareSize, gp.squareSize * 3);
            }
        }

        if(initialDoorState.equals("closed")) {
            noAnimation = setImage("objects/" + name + "/" + name + "_10");
            this.collision = true;
        }
        else {
            noAnimation = setImage("objects/" + name + "/" + name + "_1");
            this.collision = false;
        }

        animation = StaticObject.NO_ANIMATION;
        framesToChangeSprite = 1;

        this.relatedRoom = relatedRoom;
    }
}
