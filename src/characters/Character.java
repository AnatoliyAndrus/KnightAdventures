package characters;

import gameobject.GameObject;
import main.GamePanel;

import java.awt.*;
import java.awt.image.*;

public class Character extends GameObject {

    public int speed;

    public BufferedImage up1, up2, up3, left1, left2, left3, right1, right2, right3;

    public boolean collisionOnX = false;
    public boolean collisionOnY = false;

    public boolean isDead;

    public int maxHP;
    public int HP;
    public int armor;

    public Character(GamePanel gp) {
        super(gp);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g2d) {

        BufferedImage image = switch (direction) {
            case "up" -> imageNum == 1 ? up1 : imageNum == 2 ? up2 : up3;
            case "down" -> imageNum == 1 ? down1 : imageNum == 2 ? down2 : down3;
            case "left" -> imageNum == 1 ? left1 : imageNum == 2 ? left2 : left3;
            case "right" -> imageNum == 1 ? right1 : imageNum == 2 ? right2 : right3;
            default -> null;
        };

        g2d.drawImage(image, screenX, screenY, null);
    }

    public void updateImage() {
        imageCount++;
        if(imageCount >= gp.FPS/5) {
            imageNum = (imageNum == 1 ? 2 : imageNum == 2 ? 3 : 1);
            imageCount = 0;
        }
    }

    public void receiveDamage() {

    }
}
