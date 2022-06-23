package objects;
import main.GamePanel;
import java.awt.*;

public class Door extends StaticObject {

    public Door(GamePanel gp, String relatedRoom, String initialDoorState) {
        super(gp, "door");

        addImage("door_opened");
        addImage("door_1");
        addImage("door_closed");
        if(initialDoorState.equals("closed")) {
            noAnimation = setImage("objects/door/door_closed");
            this.collision = true;
        }
        else {
            noAnimation = setImage("objects/door/door_opened");
            this.collision = false;
        }
        isAnimated = StaticObject.NO_ANIMATION;
        framesToChangeSprite = 60;

        this.relatedRoom = relatedRoom;
        this.defaultCollisionAreaX = 0;
        this.defaultCollisionAreaY = 0;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 48, 48);
    }
}
