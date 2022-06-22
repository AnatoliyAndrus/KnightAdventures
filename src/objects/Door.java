package objects;
import main.GamePanel;
import java.awt.*;

public class Door extends StaticObject {

    public boolean unlocked;

    public Door(GamePanel gp, String relatedRoom) {
        super(gp, "door");

        addImage("door_opened");
        addImage("door_1");
        addImage("door_closed");
        noAnimation = setImage("objects/door/door_opened");
        isAnimated = StaticObject.NO_ANIMATION;
        framesToChangeSprite = 20;

        this.relatedRoom = relatedRoom;
        this.collision = false;
        this.unlocked = true;
        this.defaultCollisionAreaX = 0;
        this.defaultCollisionAreaY = 0;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 48, 48);
    }
}
