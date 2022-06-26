package characters;

import bullets.Bullet;
import main.GamePanel;
import main.KeyRecorder;
import main.UI;
import objects.StaticObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Character {

    public final static int DEFAULT = 1;
    public final static int SHOTGUN = 2;
    public final static int BURST = 3;
    public final static int BURST_SHOTGUN = 4;

    //boolean variables for type of shooting
    public boolean burstFire;
    public boolean shotgunFire;
    public boolean shooting;
    //frames for counting fire
    int shootingFrames;
    //coordinates of shooting
    public int finalBulletX;
    public int finalBulletY;

    public BufferedImage heart1, heart2, heart3, heart4, shield;

    KeyRecorder keyR;
    UI ui;

    int standFrames;

    public boolean scriptsAreActive;

    public String lastBulletDirection;
    public int lastBulletFrames;

    public boolean hasTorch;

    public boolean isInvincible;
    public int invincibleFrames;

    public boolean isReloading;
    public int reloadingFrames;
    public int requiredReloadingFrames;

    public String interactedObjectName;
    int index;

    public int backupArmor;
    public int backupCoinsAmount;

    public int coinsAmount;
    public boolean hasBossKey = true;

    public Player(GamePanel gp, KeyRecorder keyR) {
        super(gp);

        this.keyR = keyR;
        this.ui = gp.ui;

        setDefaultParameters();
        setPlayerImages(false);
        setFireMode(DEFAULT);
    }

    public void setDefaultParameters() {
        screenX = gp.squareSize * (gp.maxCols - 3) / 2.0;
        screenY = gp.squareSize * 10;
        speed = gp.FPS/20;

        // COLLISION SQUARE OF THE PLAYER
        areaOfCollision = new Rectangle(8, 24, 32, 24);
        defaultCollisionAreaX = 8;
        defaultCollisionAreaY = 24;

        maxHP = 3;
        HP = maxHP;
        armor = 0;
        name = "player";

        interactedObjectName = "";

        coinsAmount = gp.ui.armorPrice;
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

        if(scriptsAreActive) {
            direction = "down";
            imageNum = 0;
            if(gp.bullets.size()>0) {
                gp.bullets = new ArrayList<>();
            }
        }

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
            //INTERACTING STATIC OBJECTS
            if (index >= 0 && !scriptsAreActive) {
                interactObject(index);
            }
            // CHECK COLLISION WITH ENEMIES
            gp.colViewer.checkCharacterCollision(this, true);

            //STOPPING PLAYER DURING DOORS ANIMATION
            if(scriptsAreActive) {
                collisionOnX = true;
                collisionOnY = true;
                direction = "down";
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

        if(shooting){
            if(!burstFire){
                shootBullets();
                shooting=false;
            }else {
                if (shootingFrames == 0) {
                    shootBullets();
                }
                shootingFrames++;
                if (shootingFrames == 10) {
                    shootBullets();
                }
                if (shootingFrames == 20) {
                    shootBullets();
                    shooting = false;
                    shootingFrames = 0;
                }
            }
        }
    }

    private void interactObject(int index) {
        if (index != -1) {

            //INTERACTING DIFFERENT OBJECTS
            switch (gp.roomManager.currentRoom.staticObjects.get(index).name) {
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
                case "chest" -> {
                    interactedObjectName = "chest";
                    gp.roomManager.currentRoom.staticObjects.get(2).interactingFrames = 150;
                    if(!gp.roomManager.currentRoom.staticObjects.get(2).isOpened
                            && gp.roomManager.currentRoom.phase.equals("completed")) {
                        gp.ui.makeScreenHint("Open the chest#(press F)", 150);
                    } else if(!gp.roomManager.currentRoom.staticObjects.get(2).emptyChest
                            && gp.roomManager.currentRoom.phase.equals("completed")){
                        gp.ui.makeScreenHint("Pick up the key#(press F)", 150);
                    } else if (gp.roomManager.currentRoom.phase.equals("completed")){
                        gp.ui.makeScreenHint("This chest is empty...", 150);
                    }
                }
                case "boss_door" -> {
                    if (gp.roomManager.currentRoom.name.equals("castle")
                            && !gp.roomManager.currentRoom.staticObjects.get(3).isOpened) {
                        interactedObjectName = "boss_door";
                        gp.roomManager.currentRoom.staticObjects.get(3).interactingFrames = 150;
                        if (gp.player.hasBossKey) {
                            gp.ui.makeScreenHint("Enter boss room...#(press F)", 150);
                        } else {
                            gp.ui.makeScreenHint("You are not allowed to enter this room...", 150);
                        }
                    }
                }
                case "torch_left", "torch_right" -> {
                    if(gp.roomManager.currentRoom.name.equals("dungeon")) {
                        interactedObjectName = "dungeonTorches";
                        gp.roomManager.currentRoom.staticObjects.get(0).interactingFrames = 120;
                        gp.roomManager.currentRoom.staticObjects.get(1).interactingFrames = 120;

                        if(!hasTorch) {
                            gp.ui.makeScreenHint("Light up your torch here#(press F)", 120);
                        } else {
                            gp.ui.makeScreenHint("Light is locked here forever...#(press F)", 120);
                        }
                    }
                }
                case "lever" -> {
                    switch(gp.roomManager.currentRoom.name) {
                        case "ruins" -> {
                            if(gp.roomManager.currentRoom.phase.equals("ruins unique phase")
                                    && gp.roomManager.currentRoom.staticObjects.get(2).animation != StaticObject.ANIMATION_ONCE) {
                                if(armor == 5) {
                                    interactedObjectName = "lever";
                                    gp.roomManager.currentRoom.staticObjects.get(2).interactingFrames = 150;
                                    gp.ui.makeScreenHint("Start the journey...#(press F)", 150);
                                } else {
                                    gp.roomManager.currentRoom.staticObjects.get(2).interactingFrames = 150;
                                    gp.ui.makeScreenHint("You should buy armor before you go...#Visit the shop out there", 150);
                                }
                            }
                        }

                        case "finalMap" -> {
                            if(gp.roomManager.currentRoom.phase.equals("initial")) {
                                interactedObjectName = "lever";
                                gp.roomManager.currentRoom.staticObjects.get(0).interactingFrames = 150;
                                gp.ui.makeScreenHint("Boss is coming...#(press F)", 150);
                            }
                        }
                    }
                }
                case "coin" -> {
                    coinsAmount++;
                    gp.playSound(12);
                    gp.roomManager.currentRoom.staticObjects.remove(index);
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
        if (HP <= 0){
            gp.currentState = gp.gameOverState;
            gp.ui.optionNum = 1;
        }
    }

    /**
     * method made by Anatolii
     * player shoots bullets using this method
     */
    public void shootBullets(){
        gp.bullets.add(new Bullet(gp, "player_bullet",
                2, 2,
                6, 6,
                (int) screenX + gp.squareSize / 2, (int) screenY + gp.squareSize / 2,
                finalBulletX, finalBulletY - 5, "player", false));
        if(shotgunFire){
            //just math
            double a1 = finalBulletX-screenX - gp.squareSize/2.0;
            double b1 = finalBulletY-screenY - gp.squareSize/2.0;
            double a2 = a1*Math.cos(Math.toRadians(5))-b1*Math.sin(Math.toRadians(5));
            double b2 = b1*Math.cos(Math.toRadians(5))+a1*Math.sin(Math.toRadians(5));
            double a3 = a1*Math.cos(-Math.toRadians(5))-b1*Math.sin(-Math.toRadians(5));
            double b3 = b1*Math.cos(-Math.toRadians(5))+a1*Math.sin(-Math.toRadians(5));

            gp.bullets.add(new Bullet(gp, "player_bullet",
                    2, 2,
                    6, 6,
                    (int) screenX + gp.squareSize / 2, (int) screenY + gp.squareSize / 2,
                    (int) screenX + gp.squareSize / 2+(int)a2, (int) screenY + gp.squareSize / 2+(int)b2 - 5, "player", false));
            gp.bullets.add(new Bullet(gp, "player_bullet",
                    2, 2,
                    6, 6,
                    (int) screenX + gp.squareSize / 2, (int) screenY + gp.squareSize / 2,
                    (int) screenX + gp.squareSize / 2+(int)a3, (int) screenY + gp.squareSize / 2+(int)b3 - 5, "player", false));
        }
        gp.player.isReloading = true;
        gp.playSound(7);
        gp.playSound(1);
    }

    public void setFireMode(int fireMode) {
        switch (fireMode) {
            case DEFAULT -> {
                shotgunFire = false;
                burstFire = false;
                requiredReloadingFrames = 24;
            }
            case SHOTGUN -> {
                shotgunFire = true;
                burstFire = false;
                requiredReloadingFrames = 24;
            }
            case BURST -> {
                shotgunFire = false;
                burstFire = true;
                requiredReloadingFrames = 48;
            }
            case BURST_SHOTGUN -> {
                shotgunFire = true;
                burstFire = true;
                requiredReloadingFrames = 48;
            }
        }
    }

    private boolean checkForActiveScripts() {
        return (gp.roomManager.currentRoom.doorsOpeningNow > 0) || (gp.roomManager.currentRoom.doorsClosingNow > 0) ;
    }
}
