package characters;

import bullets.Bullet;
import main.GamePanel;
import objects.StaticObject;
import objects.Target;

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

    public static int constDamage;

    public int mode;
    public int modeTimer;
    public int framesToChangeSprite;
    public int framesToCount;
    public int requiredReloadingFrames;

    public boolean configured;

    public boolean airAttacking;
    public boolean approaching;
    public boolean strafing;

    public int strafingExtraBullets;

    public int airExtraMissiles;
    public ArrayList<StaticObject> targets;

    public double targetX;
    public double targetY;
    public double returnX, returnY;
    public double playerDistance;
    public double deltaX, deltaY;
    public double speedX, speedY;

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
            //CONFIGURING
            if (!configured) {

                isInvincible = true;
                if(HP > 0.5 * maxHP) {
                    airExtraMissiles = 4;
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithPistol(gp, gp.squareSize * 6, gp.squareSize * 5));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithFangs(gp, gp.squareSize * 12, gp.squareSize * 7));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithPistol(gp, gp.squareSize * 18, gp.squareSize * 5));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithFangs(gp, gp.squareSize * 9, gp.squareSize * 6));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithFangs(gp, gp.squareSize * 15, gp.squareSize * 6));
                } else if (HP > 0.25 * maxHP) {
                    airExtraMissiles = 9;
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithPistol(gp, gp.squareSize * 6, gp.squareSize * 5));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithFangs(gp, gp.squareSize * 12, gp.squareSize * 7));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithPistol(gp, gp.squareSize * 18, gp.squareSize * 5));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithFangs(gp, gp.squareSize * 9, gp.squareSize * 6));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithFangs(gp, gp.squareSize * 15, gp.squareSize * 6));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithFangs(gp, gp.squareSize * 7, gp.squareSize * 8));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithFangs(gp, gp.squareSize * 17, gp.squareSize * 8));
                } else {
                    airExtraMissiles = 14;
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithPistol(gp, gp.squareSize * 6, gp.squareSize * 5));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithPistol(gp, gp.squareSize * 12, gp.squareSize * 7));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithPistol(gp, gp.squareSize * 18, gp.squareSize * 5));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithFangs(gp, gp.squareSize * 9, gp.squareSize * 6));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithFangs(gp, gp.squareSize * 15, gp.squareSize * 6));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithFangs(gp, gp.squareSize * 7, gp.squareSize * 8));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithFangs(gp, gp.squareSize * 17, gp.squareSize * 8));
                    gp.roomManager.currentRoom.enemies.add(new EnemyWithFangs(gp, gp.squareSize * 12, gp.squareSize * 10));
                }
                for (int i = 1; i < gp.roomManager.currentRoom.enemies.size(); i++) {
                    gp.roomManager.currentRoom.enemies.get(i).areaOfCollision.x += gp.roomManager.currentRoom.enemies.get(i).screenX;
                    gp.roomManager.currentRoom.enemies.get(i).areaOfCollision.y += gp.roomManager.currentRoom.enemies.get(i).screenY;

                    if (gp.roomManager.currentRoom.enemies.get(i)
                            .areaOfCollision
                            .intersects(new Rectangle((int)gp.player.screenX + gp.player.areaOfCollision.x,
                                    (int)gp.player.screenY + gp.player.areaOfCollision.y,
                                    gp.player.areaOfCollision.width, gp.player.areaOfCollision.height))) {
                        gp.roomManager.currentRoom.enemies.get(i).screenY += gp.squareSize * 3;
                    }
                    gp.roomManager.currentRoom.enemies.get(i).areaOfCollision.x = gp.roomManager.currentRoom.enemies.get(i).defaultCollisionAreaX;
                    gp.roomManager.currentRoom.enemies.get(i).areaOfCollision.y = gp.roomManager.currentRoom.enemies.get(i).defaultCollisionAreaY;
                }
                gp.playSound(20);

                imageFrames = 0;
                framesToChangeSprite = 10;
                speed = 3;
                airAttacking = true;
                if (imageNum < 0) imageNum = 0;
                framesToCount = 0;
                if (screenX > gp.squareSize * 10) direction = "left";
                else direction = "right";

                configured = true;
            }

            //MAIN PART OF THE MODE
            if (screenX < gp.squareSize * 7) direction = "right";
            if (screenX > gp.maxScreenWidth - gp.squareSize * 12) direction = "left";
            if (direction.equals("right")) screenX += speed;
            if (direction.equals("left")) screenX -= speed;

            framesToCount++;
            if(framesToCount == 60) {
                gp.bullets.add(new Bullet(gp, "big_missile_3",
                        4, 0,
                        40, 48,
                        (int)screenX + areaOfCollision.width / 2 + gp.squareSize - 5, (int)screenY,
                        (int)screenX + areaOfCollision.width / 2 + gp.squareSize - 5, (int)screenY - 10, "boss", true));

                Target playerTarget = new Target(gp, (int)gp.player.screenX, (int)gp.player.screenY);
                targets.add(playerTarget);
                gp.roomManager.currentRoom.addGameObject(playerTarget, (int)gp.player.screenX, (int)gp.player.screenY);
                for (int i = 0; i < airExtraMissiles; i++) {
                    int randX = (int) (Math.random() * 940 + 106);
                    int randY = (int) (Math.random() * 412 + 250);

                    Target target  = new Target(gp, randX, randY);
                    targets.add(target);
                    gp.roomManager.currentRoom.addGameObject(target, randX, randY);
                }
            }
            if(gp.roomManager.currentRoom.enemies.size() == 1) {
                isInvincible = false;
                airAttacking = false;
                if(framesToCount >= 60 && framesToCount < 180) framesToCount = 180;
            }
            if(framesToCount == 180) {

                for (int i = 0; i < targets.size(); i++) {
                    int rocketX = (int)targets.get(i).screenX;
                    int rocketY = (int)targets.get(i).screenY;

                    gp.bullets.add(new Bullet(gp, "small_missile_3",
                            0, 0,
                            48, 48,
                            rocketX, -gp.squareSize,
                            rocketX, rocketY, "boss", true));
                }

                targets = new ArrayList<>();
                framesToCount = 0;
            }

            //CHANGING MODE
            if (!airAttacking) {
                direction = "down";
                mode = (int) (Math.random() * 2 + 3);
                configured = false;
            }
        }
        // APPROACH MODE
        else if (mode == APPROACH) {
            //CONFIGURING
            if (!configured) {
                if(HP > 0.5 * maxHP) {
                    speed = 6;
                } else if (HP > 0.25 * maxHP) {
                    speed = 8;
                } else {
                    speed = 10;
                }

                imageFrames = 0;
                framesToChangeSprite = 3;

                approaching = true;
                setSpeed();

                if (imageNum < 0) imageNum = 0;
                configured = true;
            }

            //MAIN PART OF THE MODE
            screenX += speedX;
            screenY += speedY;
            gp.colViewer.checkPlayerCollision(this);
            if(Math.abs(screenX - targetX) < (int)(speed / Math.sqrt(2)) && Math.abs(screenY - targetY) < (int)(speed / Math.sqrt(2))) {
                speedX *= -1;
                speedY *= -1;
            }
            if(Math.abs(screenX - returnX) < (int)(speed / Math.sqrt(2)) && Math.abs(screenY - returnY) < (int)(speed / Math.sqrt(2))) {
                approaching = false;
            }

            //CHANGING MODE
            if (!approaching) {
                direction = "down";
                int rand = (int)(Math.random() * 5);
                if(rand <= 1) {
                    mode = AIR_ATTACK;
                } else if (rand <= 3) {
                    mode = STRAFING;
                } else {
                    mode = APPROACH;
                }
                configured = false;
            }
        }
        // STRAFING MODE
        else if (mode == STRAFING) {
            //CONFIGURING
            if (!configured) {
                if(HP > 0.5 * maxHP) {
                    strafingExtraBullets = 1;
                    requiredReloadingFrames = 30;
                } else if (HP > 0.25 * maxHP) {
                    strafingExtraBullets = 2;
                    requiredReloadingFrames = 25;
                } else {
                    strafingExtraBullets = 3;
                    requiredReloadingFrames = 20;
                }

                imageFrames = 0;
                framesToChangeSprite = 5;
                speed = gp.FPS / 10;
                modeTimer = 600;

                if (imageNum < 0) imageNum = 0;
                framesToCount = 0;
                if (screenX > gp.squareSize * 10) direction = "left";
                else direction = "right";
                configured = true;
            }

            //MAIN PART OF THE MODE
            if (screenX < gp.squareSize * 1.5) direction = "right";
            if (screenX > gp.maxScreenWidth - gp.squareSize * 6.5) direction = "left";

            if (direction.equals("right")) screenX += speed;
            if (direction.equals("left")) screenX -= speed;

            framesToCount++;

            if(framesToCount == requiredReloadingFrames) {

                gp.bullets.add(new Bullet(gp, "enemy_bullet",
                        2, 2,
                        6, 6,
                        (int)screenX + areaOfCollision.width / 2 + gp.squareSize - 5, (int)screenY + areaOfCollision.height,
                        (int)gp.player.screenX + 19, (int)gp.player.screenY + 30, "boss", false));

                for (int i = 0; i < strafingExtraBullets; i++) {
                    gp.bullets.add(new Bullet(gp, "enemy_bullet",
                            2, 2,
                            6, 6,
                            (int)screenX + areaOfCollision.width / 2 + gp.squareSize - 5, (int)screenY + areaOfCollision.height,
                            (int)(Math.random() * 25 * gp.squareSize), (int)gp.player.screenY + 30, "boss", false));
                }

                gp.playSound(2);
                framesToCount = 0;
            }

            //CHANGING MODE
            modeTimer--;
            if (modeTimer == 0) {
                direction = "down";
                mode = (int)(Math.random() * 2 + 2);
                configured = false;
            }
        }

        updateImage();
    }

    @Override
    public void draw(Graphics2D g2d) {
        if(imageNum == -1) g2d.drawImage(noAnimation, (int)screenX, (int)screenY, null);
        else g2d.drawImage(images.get(imageNum), (int)screenX, (int)screenY, null);
    }

    private void setSpeed() {
        returnX = screenX;
        returnY = screenY;
        targetX = gp.player.screenX - areaOfCollision.width/2.0 - gp.squareSize/2.0;
        targetY = gp.player.screenY - gp.squareSize/2.0;
        deltaX = targetX - screenX;
        deltaY = targetY - screenY;

        playerDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        speedX = speed * deltaX / playerDistance;
        speedY = speed * deltaY / playerDistance;
    }

    @Override
    public void receiveDamage(int damage) {
        if(HP > 0) {
            HP -= damage;
        }
        if (HP <= 0){
            isDead = true;
            for (StaticObject obj: gp.roomManager.currentRoom.staticObjects) {
                if(obj.name.equals("target")) {
                    obj.isGarbage = true;
                }
            }
            gp.bullets = new ArrayList<>();
            gp.roomManager.currentRoom.staticObjects.get(1).setAnimation(StaticObject.ANIMATION_ONCE);
            gp.playSound(14);
            gp.roomManager.currentRoom.doorsOpeningNow++;
        }
    }

    @Override
    public void updateImage() {
        if(imageNum >= 0) {
            imageFrames++;
            if (imageFrames == framesToChangeSprite) {
                imageFrames = 0;
                imageNum++;
                if (imageNum == images.size()) imageNum = 0;
            }
        }
    }

    public void setDefaultParameters() {
        mode = NO_ACTION;
        framesToChangeSprite = 5;

        screenX = gp.squareSize * 10;
        screenY = gp.squareSize * 3;
        speed = 0;

        // COLLISION SQUARE OF THE PLAYER
        areaOfCollision = new Rectangle(48, 24, 144, 72);
        defaultCollisionAreaX = 48;
        defaultCollisionAreaY = 24;

        maxHP = switch (gp.difficulty) {
            case 1 -> 50;
            case 2 -> 75;
            case 3 -> 125;
            case 4 -> 150;
            case 5 -> 200;
            default -> 10;
        };
        HP = maxHP;
        name = "boss";

        damage = gp.difficulty;
        constDamage = damage;

        targets = new ArrayList<>();
    }

    public void setImages() {
        addImage("boss_left_1");
        addImage("boss_left_2");
        addImage("boss_standart");
        addImage("boss_right_1");
        addImage("boss_right_2");

        noAnimation = setImage("boss/boss_standart");
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