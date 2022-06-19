package characters;

import main.GamePanel;

import java.awt.*;
import java.awt.image.*;

public class Character {

    public int screenX;
    public int screenY;
    public int speed;
    GamePanel gp;

    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
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

    public Character(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2d) {

        BufferedImage image = switch (direction) {
            case "up" -> imageNum == 1 ? up1 : imageNum == 2 ? up2 : up3;
            case "down" -> imageNum == 1 ? down1 : imageNum == 2 ? down2 : down3;
            case "left" -> imageNum == 1 ? left1 : imageNum == 2 ? left2 : left3;
            case "right" -> imageNum == 1 ? right1 : imageNum == 2 ? right2 : right3;
            case "toPlayer" -> (gp.player.screenX - screenX > 0) ?
                    (imageNum == 1 ? right1 : imageNum == 2 ? right2 : right3) :
                    (imageNum == 1 ? left1 : imageNum == 2 ? left2 : left3);
            default -> null;
        };

        g2d.drawImage(image, screenX, screenY, null);
    }
}
