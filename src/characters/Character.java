/**
 * @author Anatolii Andrusenko, Vladislav Marchenko, Andrii Sulimenko
 *
 * @version 1.0
 *
 * class of characters, is inherited by player, enemies
 */
package characters;

import gameobject.GameObject;
import main.GamePanel;

import java.awt.*;
import java.awt.image.*;

public class Character extends GameObject {

    //SPEED OF CHARACTER
    public int speed;

    //IMAGES OF CHARACTER
    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    //DIRECTION OF CHARACTER
    public String direction = "down";

    //COLLISION PARAMETERS
    public boolean collisionOnX = false;
    public boolean collisionOnY = false;
    //CHECKS IF CHARACTER IS DEAD
    public boolean isDead;
    //MAX HP OF CHARACTER
    public int maxHP;
    //HP OF CHARACTER
    public int HP;
    //ARMOR OF CHARACTER
    public int armor;

    //IS CHARACTER INVINCIBLE AT THE MOMENT
    public boolean isInvincible;

    /**
     * constructor
     * @param gp game panel
     */
    public Character(GamePanel gp) {
        super(gp);
    }

    /**
     * method which updates character
     */
    @Override
    public void update() {

    }

    /**
     * method to draw character
     * @param g2d
     */
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

    /**
     * method to update image of character
     */
    public void updateImage() {
        imageFrames++;
        if(imageFrames >= gp.FPS/5) {
            imageNum = (imageNum == 0 ? 1 : imageNum == 1 ? 2 : 0);
            imageFrames = 0;
        }
    }

    /**
     * method for character to receive damage
     * @param damage
     */
    public void receiveDamage(int damage) {

    }
}
