package characters;

import main.GamePanel;
import main.KeyRecorder;
import main.UI;
import objects.StaticObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Character {

    public BufferedImage heart1, heart2, heart3, heart4, shield;

    KeyRecorder keyR;
    UI ui;

    int standFrames = 0;

    public boolean scriptsAreActive;

    public String lastBulletDirection;
    public int lastBulletFrames = 0;

    public boolean hasTorch;

    public boolean isInvincible;
    public int invincibleFrames = 0;

    public boolean isReloading;
    public int reloadingFrames;
    public int requiredReloadingFrames = 24;

    public String interactedObjectName = "";

    int index = -1;

    public Player(GamePanel gp, KeyRecorder keyR) {
        super(gp);

        this.keyR = keyR;
        this.ui = gp.ui;

        setDefaultParameters();
        setPlayerImages(false);
    }

    public void setDefaultParameters() {
        screenX = gp.squareSize * (gp.maxCols - 3) / 2;
        screenY = gp.squareSize * (gp.maxRows - 3) / 2;
        speed = gp.FPS/20;

        // COLLISION SQUARE OF THE PLAYER
        areaOfCollision = new Rectangle(8, 24, 32, 24);
        defaultCollisionAreaX = 8;
        defaultCollisionAreaY = 24;

        maxHP = 15;
        HP = maxHP;
        armor = 5;
        name = "player";
    }

    public void setPlayerImages(boolean torch) {

        String path;
        if (torch) path = "playerWithTorch";
        else path = "player";

        up1 = setImage(path + "/knight_up_1");
        up2 = setImage(path + "/knight_up_2");
        up3 = setImage(path + "/knight_up_3");
        down1 = setImage(path + "/knight_down_1");
        down2 = setImage(path + "/knight_down_2");
        down3 = setImage(path + "/knight_down_3");
        left1 = setImage(path + "/knight_left_1");
        left2 = setImage(path + "/knight_left_2");
        left3 = setImage(path + "/knight_left_3");
        right1 = setImage(path + "/knight_right_1");
        right2 = setImage(path + "/knight_right_2");
        right3 = setImage(path + "/knight_right_3");
        heart1 = setImage("objects/heart1");
        heart2 = setImage("objects/heart2");
        heart3 = setImage("objects/heart3");
        heart4 = setImage("objects/heart4");
        shield = setImage("objects/shield");
    }

    @Override
    public void update() {

        scriptsAreActive = checkForActiveScripts();

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
            // CHECK COLLISION OF STATIC OBJECTS
            if (gp.keyR.up || gp.keyR.down || gp.keyR.left || gp.keyR.right) {
                index = gp.colViewer.checkObjectCollision(this, true);
            }
            int tmp = index;
            //INTERACTING STATIC OBJECTS
            if (tmp >= 0) {
                interactObject(tmp);
            }
            // CHECK COLLISION WITH ENEMIES
            gp.colViewer.checkCharacterCollision(this, true);

            //STOPPING PLAYER DURING DOORS ANIMATION
            if(scriptsAreActive) {
                collisionOnX = true;
                collisionOnY = true;
                direction = "down";
                imageNum = 0;
                lastBulletFrames = 0;
            } else {
                updateImage();

                if(lastBulletFrames > 0) {
                    lastBulletFrames--;
                    direction = lastBulletDirection;
                }
            }

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
                resetObjectsTimers();
                gp.roomManager.setCurrentRoom(gp.roomManager.currentRoom.downRoom.name);
                screenY = 0;
            }
            else if(screenY < 0) {
                gp.bullets = new ArrayList<>();
                resetObjectsTimers();
                screenY = gp.squareSize * (gp.maxRows - 3);
                gp.roomManager.setCurrentRoom(gp.roomManager.currentRoom.upRoom.name);
            }
            else if(screenX < 0) {
                gp.bullets = new ArrayList<>();
                resetObjectsTimers();
                screenX = gp.squareSize * (gp.maxCols - 3);
                gp.roomManager.setCurrentRoom(gp.roomManager.currentRoom.leftRoom.name);
            }
            else if(screenX > gp.squareSize * (gp.maxCols - 3)) {
                gp.bullets = new ArrayList<>();
                resetObjectsTimers();
                screenX = 0;
                gp.roomManager.setCurrentRoom(gp.roomManager.currentRoom.rightRoom.name);
            }

            speed = tmpSpeed;
        }
        //PLAYER STANDS STILL
        else {
            standFrames ++;

            if(standFrames >= gp.FPS/3) {
                imageNum = 0;
            }
        }

        //INVINCIBILITY TIME
        if(isInvincible) {
            invincibleFrames++;
            if(invincibleFrames == gp.FPS * 0.5) {
                isInvincible = false;
                invincibleFrames = 0;
            }
        }

        //RELOADING TIME
        if(isReloading) {
            reloadingFrames++;
            if(reloadingFrames == requiredReloadingFrames) {
                isReloading = false;
                reloadingFrames = 0;
            }
        }
    }

    private void interactObject(int index) {
        if (index != -1) {

            //INTERACTING DIFFERENT OBJECTS
            switch (gp.roomManager.currentRoom.staticObjects.get(index).name) {
                case "woodenBox" -> {
                    interactedObjectName = "woodenBox";
                    gp.ui.makeScreenHint("This is just a box hehe", 150);
                }
                case "shop" -> {
                    interactedObjectName = "shop";
                    //ADD DOTA SOUND
                    int phrase = (int) (Math.random() * 3) + 1;

                    switch (phrase){
                        case 1 -> {
                            gp.roomManager.currentRoom.staticObjects.get(index).interactingFrames = 150;
                            gp.ui.makeScreenHint("Seller: Welcome to the Secret shop!#(press F)", 150);
                            gp.playSound(3);
                        }
                        case 2 -> {
                            gp.roomManager.currentRoom.staticObjects.get(index).interactingFrames = 150;
                            gp.ui.makeScreenHint("Seller: What're you buying?#(press F)", 150);
                            gp.playSound(4);
                        }
                        case 3 -> {
                            gp.roomManager.currentRoom.staticObjects.get(index).interactingFrames = 150;
                            gp.ui.makeScreenHint("Seller: Ho ho! You found me!#(press F)", 150);
                            gp.playSound(5);
                        }
                    }
                }
                case "torch_left", "torch_right" -> {
                    if(gp.roomManager.currentRoom.name.equals("dungeon")) {
                        interactedObjectName = "dungeonTorches";
                        gp.roomManager.currentRoom.staticObjects.get(0).interactingFrames = 120;
                        gp.roomManager.currentRoom.staticObjects.get(1).interactingFrames = 120;

                        if(!hasTorch) {
                            gp.ui.makeScreenHint("Light up your torch here...#(press F)", 120);
                        } else {
                            gp.ui.makeScreenHint("Light is locked here forever...#(press F)", 120);
                        }

//                        if (!gp.player.hasTorch) {
//                            gp.player.hasTorch = true;
//                            gp.player.setPlayerImages(true);
//                            gp.roomManager.currentRoom.staticObjects.get(0).setAnimation(StaticObject.NO_ANIMATION);
//                            gp.roomManager.currentRoom.staticObjects.get(1).setAnimation(StaticObject.NO_ANIMATION);
//                        } else {
//                            gp.player.hasTorch = false;
//                            gp.player.setPlayerImages(false);
//                            gp.roomManager.currentRoom.staticObjects.get(0).setAnimation(StaticObject.ANIMATION_CONTINUOUSLY);
//                            gp.roomManager.currentRoom.staticObjects.get(1).setAnimation(StaticObject.ANIMATION_CONTINUOUSLY);
//                        }
                    }
                }
            }
        }
    }

    public void resetObjectsTimers() {
        for (StaticObject obj: gp.roomManager.currentRoom.staticObjects) {
            obj.interactingFrames = 0;
            obj.isInteracted = false;
            interactedObjectName = "";
        }
    }

    @Override
    public void receiveDamage() {
        if(armor > 0){
            armor--;
        }
        else if (HP > 0){
            HP--;
        }

        //DEATH
        if (HP == 0){
            System.exit(0);
        }
    }

    private boolean checkForActiveScripts() {
        return (gp.roomManager.currentRoom.doorsOpeningNow > 0) || (gp.roomManager.currentRoom.doorsClosingNow > 0) ;
    }
}
