package objects;
import main.GamePanel;
import java.awt.*;

public class Door extends StaticObject{

    public String relatedRoom;
    public boolean unlocked;

    public Door(GamePanel gp, String relatedRoom) {
        super(gp, "door");

        this.down1 = setImage("objects/doors/door_1");
        this.down2 = setImage("objects/doors/door_2");
        this.down3 = setImage("objects/doors/door_3");

        this.relatedRoom = relatedRoom;
        this.collision = false;
        this.unlocked = true;
        this.defaultCollisionAreaX = 0;
        this.defaultCollisionAreaY = 0;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 48, 48);
    }
}
