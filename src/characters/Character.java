package characters;

import gameobject.GameObject;
import main.GamePanel;

import java.awt.*;
import java.awt.image.*;

public class Character extends GameObject {

    public int speed;

    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    public String direction = "down";

    public boolean collisionOnX = false;
    public boolean collisionOnY = false;

    public boolean isDead;

    public int maxHP;
    public int HP;
    public int armor;

    public boolean isInvincible;

    public Character(GamePanel gp) {
        super(gp);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g2d) {

        BufferedImage image = switch (direction) {
            case "up" -> imageNum == 0 ? up1 : imageNum == 1 ? up2 : up3;
            case "down" -> imageNum == 0 ? down1 : imageNum == 1 ? down2 : down3;
            case "left" -> imageNum == 0 ? left1 : imageNum == 1 ? left2 : left3;
            case "right" -> imageNum == 0 ? right1 : imageNum == 1 ? right2 : right3;
            default -> null;
        };

        g2d.drawImage(image, (int)screenX, (int)screenY, null);
    }

    public void updateImage() {
        imageFrames++;
        if(imageFrames >= gp.FPS/5) {
            imageNum = (imageNum == 0 ? 1 : imageNum == 1 ? 2 : 0);
            imageFrames = 0;
        }
    }

    public void receiveDamage() {

    }
}
