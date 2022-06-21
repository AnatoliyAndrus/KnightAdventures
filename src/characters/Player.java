package characters;

import main.GamePanel;
import main.KeyRecorder;

import javax.imageio.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Character {

    KeyRecorder keyR;

    //FOR PLAYER TO STAND
    int standFrames = 0;

    public boolean hasTorch;

    public boolean isInvincible;
    public int invincibleFrames = 0;

    public Player(GamePanel gp, KeyRecorder keyR) {
        super(gp);

        this.keyR = keyR;

        setDefaultParameters();
        setPlayerImages(false);
    }

    public void setDefaultParameters() {
        screenX = gp.squareSize * (gp.maxCols - 3) / 2;
        screenY = gp.squareSize * (gp.maxRows - 3) / 2;
        speed = gp.FPS/20;

        // COLLISION SQUARE OF THE PLAYER
        areaOfCollision = new Rectangle(8, 16, 32, 32);
        defaultCollisionAreaX = 8;
        defaultCollisionAreaY = 16;

        maxHP = 15;
        HP = maxHP;
        armor = 5;

        direction = "down";
    }

    public void setPlayerImages(boolean torch) {

        String path;
        if (torch) {
            path = "playerWithTorch";
        } else path = "player";
        try {
            up1 = ImageIO.read(new File("resources/images/" + path + "/knight_up_1.png"));
            up2 = ImageIO.read(new File("resources/images/" + path + "/knight_up_2.png"));
            up3 = ImageIO.read(new File("resources/images/" + path + "/knight_up_3.png"));
            down1 = ImageIO.read(new File("resources/images/" + path + "/knight_down_1.png"));
            down2 = ImageIO.read(new File("resources/images/" + path + "/knight_down_2.png"));
            down3 = ImageIO.read(new File("resources/images/" + path + "/knight_down_3.png"));
            left1 = ImageIO.read(new File("resources/images/" + path + "/knight_left_1.png"));
            left2 = ImageIO.read(new File("resources/images/" + path + "/knight_left_2.png"));
            left3 = ImageIO.read(new File("resources/images/" + path + "/knight_left_3.png"));
            right1 = ImageIO.read(new File("resources/images/" + path + "/knight_right_1.png"));
            right2 = ImageIO.read(new File("resources/images/" + path + "/knight_right_2.png"));
            right3 = ImageIO.read(new File("resources/images/" + path + "/knight_right_3.png"));
            heart1 = ImageIO.read(new File("resources/images/objects/heartImg/heart1.png"));
            heart2 = ImageIO.read(new File("resources/images/objects/heartImg/heart2.png"));
            heart3 = ImageIO.read(new File("resources/images/objects/heartImg/heart3.png"));
            heart4 = ImageIO.read(new File("resources/images/objects/heartImg/heart4.png"));
            shield = ImageIO.read(new File("resources/images/objects/shield.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        //PLAYER MOVES
        if(keyR.up || keyR.down || keyR.left || keyR.right) {

            int tmpSpeed = speed;
            if (keyR.up && keyR.left || keyR.up && keyR.right || keyR.down && keyR.left || keyR.down && keyR.right) {
                speed = gp.FPS/30;
            }

            if (keyR.up) {
                direction = "up";
            }
            if (keyR.down) {
                direction = "down";
            }
            if (keyR.left) {
                direction = "left";
            }
            if (keyR.right) {
                direction = "right";
            }

            collisionOnX = false;
            collisionOnY = false;

            // CHECK COLLISION OF THE MAP
            gp.colViewer.checkMapCollision(this);
            // CHECK COLLISION OF OBJECTS
            int index = gp.colViewer.checkObjectCollision(this, true);
            interactObject(index);
            // CHECK COLLISION WITH ENEMIES
            gp.colViewer.checkCharacterCollision(this, true);

            //HORIZONTAL MOVEMENT
            if (!collisionOnX) {
                if (keyR.left) {
                    screenX -= speed;
                }
                if (keyR.right) {
                    screenX += speed;
                }
            }
            //VERTICAL MOVEMENT
            if(!collisionOnY) {
                if (keyR.up) {
                    screenY -= speed;
                }
                if (keyR.down) {
                    screenY += speed;
                }
            }

            //MOVING BETWEEN ROOMS
            if(screenY > gp.squareSize * (gp.maxRows - 3)) {
                gp.bullets = new ArrayList<>();
                gp.roomManager.setCurrentRoom(gp.roomManager.currentRoom.downRoom.name);
                screenY = 0;
            }
            if(screenY < 0) {
                gp.bullets = new ArrayList<>();
                gp.roomManager.setCurrentRoom(gp.roomManager.currentRoom.upRoom.name);
                screenY = gp.squareSize*(gp.maxRows - 3);
            }
            if(screenX < 0) {
                gp.bullets = new ArrayList<>();
                gp.roomManager.setCurrentRoom(gp.roomManager.currentRoom.leftRoom.name);
                screenX = gp.squareSize*(gp.maxCols - 3);
            }
            if(screenX > gp.squareSize * (gp.maxCols - 3)) {
                gp.bullets = new ArrayList<>();
                gp.roomManager.setCurrentRoom(gp.roomManager.currentRoom.rightRoom.name);
                screenX = 0;
            }

            updateImage();

            speed = tmpSpeed;
        }
        //PLAYER STANDS STILL
        else {

            standFrames ++;

            if(standFrames >= gp.FPS/3) {
                imageNum = 1;
            }
        }

        if(isInvincible) {
            invincibleFrames++;
            if(invincibleFrames == gp.FPS * 2) {
                isInvincible = false;
                invincibleFrames = 0;
            }
        }

        if (gp.roomManager.currentRoom.name.equals("dungeon") && !hasTorch) {
            hasTorch = true;
            setPlayerImages(true);
        } else if (!gp.roomManager.currentRoom.name.equals("dungeon") && hasTorch) {
            hasTorch = false;
            setPlayerImages(false);
        }

    }

    private void interactObject(int index) {
        if (index != -1) {

            switch (gp.roomManager.currentRoom.gameObjects.get(index).name) {
                case "woodenBox":
                    gp.roomManager.setCurrentRoom("dungeon");
                    System.out.println("box");
                    break;
                case "shop":
                    System.out.println("shop");
                    break;
            }

        }
    }

    @Override
    public void receiveDamage() {

        //ADD SOUNDS LATER + ANIMATIONS
        if(armor > 0){
            armor--;
        }
        else if (HP > 0){
            HP--;
        }

        if (HP == 0){
            System.exit(0);
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
