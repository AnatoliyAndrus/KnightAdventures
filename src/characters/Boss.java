package characters;

import bullets.Bullet;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Boss extends Character {

    public final static int NO_ACTION = 1;
    public final static int AIR_ATTACK = 2;
    public final static int APPROACH = 3;
    public final static int STRAFING = 4;

    public int mode;
    public int modeTimer;
    public int framesToChangeSprite;
    public int framesToCount;
    public int requiredReloadingFrames;

    public boolean configured;

    ArrayList<BufferedImage> images;
    BufferedImage noAnimation;

    public Boss(GamePanel gp) {
        super(gp);
        images = new ArrayList<>();

        setDefaultParameters();
        setImages();
    }

    @Override
    public void update() {
        // NO ACTION MODE
        if (mode == NO_ACTION) {
            imageNum = -1;
            mode = STRAFING;
        }
        // AIR ATTACK
        else if (mode == AIR_ATTACK) {
            speed = gp.FPS/30;
            if (imageNum < 0) imageNum = 0;

            //MAIN PART OF THE MODE
            modeTimer--;
            if (modeTimer == 0) mode = (int) (Math.random() * 2 + 2);
        }
        // APPROACH MODE
        else if (mode == APPROACH) {
            if (imageNum < 0) imageNum = 0;

            //MAIN PART OF THE MODE
            modeTimer--;
            if (modeTimer == 0) mode = (int) (Math.random() * 2 + 2);
        }
        // STRAFING MODE
        else if (mode == STRAFING) {
            if (!configured) {
                speed = gp.FPS / 10;
                modeTimer = 600;
                requiredReloadingFrames = 20;

                if (imageNum < 0) imageNum = 0;
                if (screenX > gp.squareSize * 11) direction = "right";
                else direction = "left";
                configured = true;
            }
            //MAIN PART OF THE MODE
            if (screenX < gp.squareSize * 2.5) direction = "right";
            if (screenX > gp.maxScreenWidth - gp.squareSize * 5.5) direction = "left";

            if (direction.equals("right")) screenX += speed;
            if (direction.equals("left")) screenX -= speed;

            framesToCount++;

            if(framesToCount == requiredReloadingFrames) {

                gp.bullets.add(new Bullet(gp, "enemy_bullet",
                        2, 2,
                        6, 6,
                        screenX + areaOfCollision.width / 2 - 5, screenY + areaOfCollision.height,
                        gp.player.screenX + 19, gp.player.screenY + 30, false));

                gp.playSound(2);
                framesToCount = 0;
            }

            modeTimer--;
            if (modeTimer == 0 && false) {
                direction = "down";
                mode = (int) (Math.random() * 2 + 2);
                configured = false;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        if(imageNum == -1) g2d.drawImage(noAnimation, screenX, screenY, null);
        else {
            imageFrames++;
            if(imageFrames == framesToChangeSprite) {
                imageFrames = 0;
                imageNum++;
                if(imageNum == images.size()) imageNum = 0;
            }

            g2d.drawImage(images.get(imageNum), screenX, screenY, null);
        }
    }

    @Override
    public void receiveDamage() {
        if(HP > 0) {
            HP--;
        }
        if (HP == 0){
            isDead = true;
        }
    }

    public void setDefaultParameters() {
        mode = NO_ACTION;
        framesToChangeSprite = 5;

        screenX = gp.squareSize * 11;
        screenY = gp.squareSize * 3;
        speed = 0;

        // COLLISION SQUARE OF THE PLAYER
        areaOfCollision = new Rectangle(0, 24, 144, 72);
        defaultCollisionAreaX = 0;
        defaultCollisionAreaY = 24;

        maxHP = 20;
        HP = maxHP;
        name = "boss";
    }

    public void setImages() {
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

        noAnimation = setImage("boss/door_horizontal_10");
    }

    public void addImage(String imageName) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("resources/images/boss/" + imageName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        images.add(image);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
