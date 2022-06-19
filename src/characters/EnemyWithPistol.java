package characters;

import main.GamePanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class EnemyWithPistol extends Character{

    //FOR PLAYER TO STAND
    int standFrames = 0;
    int framesToMove = 0;

    double playerDistance;

    public EnemyWithPistol(GamePanel gp, int col, int row) {
        super(gp);

        screenX = gp.squareSize * col;
        screenY = gp.squareSize * row;

        setDefaultParameters();
        setEnemyImages();
    }

    public void setDefaultParameters() {
        speed = 2;

        // COLLISION SQUARE OF THE PLAYER
        areaOfCollision = new Rectangle(8, 16, 32, 32);
        defaultCollisionAreaX = 8;
        defaultCollisionAreaY = 16;

        direction = "down";
    }

    public void setEnemyImages() {

        try {
            up1 = ImageIO.read(new File("resources/images/enemyWithPistol/alien_up_1.png"));
            up2 = ImageIO.read(new File("resources/images/enemyWithPistol/alien_up_2.png"));
            up3 = ImageIO.read(new File("resources/images/enemyWithPistol/alien_up_3.png"));
            down1 = ImageIO.read(new File("resources/images/enemyWithPistol/alien_down_1.png"));
            down2 = ImageIO.read(new File("resources/images/enemyWithPistol/alien_down_2.png"));
            down3 = ImageIO.read(new File("resources/images/enemyWithPistol/alien_down_3.png"));
            left1 = ImageIO.read(new File("resources/images/enemyWithPistol/alien_left_1.png"));
            left2 = ImageIO.read(new File("resources/images/enemyWithPistol/alien_left_2.png"));
            left3 = ImageIO.read(new File("resources/images/enemyWithPistol/alien_left_3.png"));
            right1 = ImageIO.read(new File("resources/images/enemyWithPistol/alien_right_1.png"));
            right2 = ImageIO.read(new File("resources/images/enemyWithPistol/alien_right_2.png"));
            right3 = ImageIO.read(new File("resources/images/enemyWithPistol/alien_right_3.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        //PLAYER MOVES
        framesToMove++;

        if (framesToMove == gp.FPS * 2 || direction.equals("toPlayer")) {
            framesToMove = 0;

            switch ((int) (Math.random() * 4 + 1)) {
                case 1 -> direction = "up";
                case 2 -> direction = "down";
                case 3 -> direction = "left";
                case 4 -> direction = "right";
            }
        }

        playerDistance = Math.sqrt(Math.pow(Math.abs(gp.player.screenX - screenX), 2)
                + Math.pow(Math.abs(gp.player.screenY - screenY), 2));

        System.out.println(playerDistance);

        if (playerDistance < gp.squareSize * 3 && playerDistance > (double) gp.squareSize / 2) {
            direction = "toPlayer";
        }

        collisionOnX = false;
        collisionOnY = false;

        // CHECK COLLISION OF THE MAP
        gp.colViewer.checkMapCollision(this);
        //  CHECK COLLISION OF OBJECTS
        int index = gp.colViewer.checkObjectCollision(this, false);

        //HORIZONTAL MOVEMENT
        if (!collisionOnX) {
            if (direction.equals("left")) {
                screenX -= speed;
            }
            if (direction.equals("right")) {
                screenX += speed;
            }
            if (direction.equals("toPlayer")) {
                screenX += (((gp.player.screenX - screenX) / playerDistance) * speed);
            }
        }
        //VERTICAL MOVEMENT
        if(!collisionOnY) {
            if (direction.equals("up")) {
                screenY -= speed;
            }
            if (direction.equals("down")) {
                screenY += speed;
            }
            if (direction.equals("toPlayer")) {
                screenY += (((gp.player.screenY - screenY) / playerDistance) * speed);
            }
            updateImage();
        }
        //PLAYER STANDS STILL
        else {
            standFrames ++;
            if(standFrames >= gp.FPS/3) {
                imageNum = 1;
            }
        }
    }

    private void updateImage() {
        imageCount++;
        if(imageCount >= gp.FPS/5) {
            imageNum = (imageNum == 1 ? 2 : imageNum == 2 ? 3 : 1);
            imageCount = 0;
        }
    }
}
