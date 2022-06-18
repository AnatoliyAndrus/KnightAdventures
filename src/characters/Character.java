package characters;

import java.awt.*;
import java.awt.image.*;

public class Character {

    public int screenX;
    public int screenY;
    public int speed;

    BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    public String direction;

    //FOR SPRITE CHANGES
    int imageCount = 0;
    int imageNum = 1;

    //FOR COLLISION
    public Rectangle areaOfCollision;
    public int defaultCollisionAreaX;
    public int defaultCollisionAreaY;
    public boolean collisionOnX = false;
    public boolean collisionOnY = false;
}
