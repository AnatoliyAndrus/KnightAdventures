package characters;

import bullets.Bullet;
import main.GamePanel;
import objects.Coin;

import java.awt.*;


public class EnemyWithPistol extends Character{

    public static int constDamage;

    int standFrames = 0;
    int framesToCount = -gp.FPS;

    double playerDistance;
    double deltaX, deltaY;

    public boolean moving;
    public boolean agressive;

    public boolean tempMoving;
    public int tempMovingFrames = 0;

    int movingTime;
    int standTime;

    public int requiredReloadingFrames = 60;

    public EnemyWithPistol(GamePanel gp, double screenX, double screenY) {
        super(gp);

        this.screenX = screenX;
        this.screenY = screenY;

        setDefaultParameters();
        setEnemyImages();
    }

    public void setDefaultParameters() {
        speed = 1;

        // COLLISION SQUARE OF THE PLAYER
        areaOfCollision = new Rectangle(8, 15, 32, 33);
        defaultCollisionAreaX = 8;
        defaultCollisionAreaY = 15;

        maxHP = 2 * gp.difficulty;
        HP = maxHP;

        damage = gp.difficulty;
        constDamage = damage;

        name = "alien";
    }

    public void setEnemyImages() {
        up1 = setImage("alien/alien_up_1");
        up2 = setImage("alien/alien_up_2");
        up3 = setImage("alien/alien_up_3");
        down1 = setImage("alien/alien_down_1");
        down2 = setImage("alien/alien_down_2");
        down3 = setImage("alien/alien_down_3");
        left1 = setImage("alien/alien_left_1");
        left2 = setImage("alien/alien_left_2");
        left3 = setImage("alien/alien_left_3");
        right1 = setImage("alien/alien_right_1");
        right2 = setImage("alien/alien_right_2");
        right3 = setImage("alien/alien_right_3");
    }

    @Override
    public void update() {

        playerDistance = Math.sqrt(Math.pow(gp.player.screenX - screenX, 2)
                + Math.pow(gp.player.screenY - gp.player.areaOfCollision.height/2.0 - (screenY - areaOfCollision.height/2.0), 2));

        if(playerDistance < gp.squareSize * 12) {
            if(!agressive) framesToCount = 0;
            agressive = true;
            moving = true;
        } else {
            if(agressive) {
                framesToCount = 0;
                moving = false;
                tempMovingFrames = 0;
                tempMoving = false;
            }
            agressive = false;
            speed = 1;
        }

        //NOT AGRESSIVE
        if(!agressive) {

            if(framesToCount < 0)
                framesToCount++;

            if (framesToCount >= 0) {

                if(!moving && framesToCount == 0) {
                    switch ((int) (Math.random() * 4 + 1)) {
                        case 1 -> direction = "up";
                        case 2 -> direction = "down";
                        case 3 -> direction = "left";
                        case 4 -> direction = "right";
                    }
                    moving = true;
                    movingTime = (int)(Math.random() * 4 + 2);
                    standTime = (int)(Math.random() * 2 + 6);
                }

                framesToCount++;

                if(framesToCount == gp.FPS * movingTime) {
                    moving = false;
                    imageNum = 0;
                }
                if(framesToCount == gp.FPS * standTime) {
                    framesToCount = 0;
                }
            }
        }
        //AGRESSIVE
        else {
            //Movement Math
            deltaX = gp.player.screenX - screenX;
            deltaY = gp.player.screenY - gp.player.areaOfCollision.height/2.0 - (screenY - areaOfCollision.height/2.0);

            if(deltaX == 0 && deltaY < 0) {
                deltaX = 1;
            } else if(deltaX == 0 && deltaY > 0) {
                deltaX = 1;
            } else if(deltaX < 0 && deltaY == 0) {
                deltaY = 1;
            } else if(deltaX > 0 && deltaY == 0) {
                deltaY = 1;
            }

            if(!tempMoving) {
                if(deltaX > 0 && deltaY < 0) {
                    if(Math.abs(deltaY) > deltaX) direction = "up";
                    else direction = "right";

                    if (collisionOnX) {
                        direction = "up";
                        screenX--;
                        tempMoving = true;
                    }
                    if(collisionOnY) {
                        direction = "right";
                        screenY++;
                        tempMoving = true;
                    }
                }
                else if(deltaX < 0 && deltaY < 0) {
                    if(deltaY < deltaX) direction = "up";
                    else direction = "left";

                    if (collisionOnX) {
                        direction = "up";
                        screenX++;
                        tempMoving = true;
                    }
                    if(collisionOnY) {
                        direction = "left";
                        screenY++;
                        tempMoving = true;
                    }
                }
                else if(deltaX < 0 && deltaY > 0) {
                    if(deltaY > Math.abs(deltaX)) direction = "down";
                    else direction = "left";

                    if (collisionOnX) {
                        direction = "down";
                        screenX++;
                        tempMoving = true;
                    }
                    if(collisionOnY) {
                        direction = "left";
                        screenY--;
                        tempMoving = true;
                    }
                }
                else if(deltaX > 0 && deltaY > 0) {
                    if(deltaY > deltaX) direction = "down";
                    else direction = "right";

                    if (collisionOnX) {
                        direction = "down";
                        screenX--;
                        tempMoving = true;
                    }
                    if(collisionOnY) {
                        direction = "right";
                        screenY--;
                        tempMoving = true;
                    }
                }

                if (Math.abs(deltaY) - Math.abs(deltaX) <= 5) tempMoving = true;
            }
            if(tempMoving) {
                tempMovingFrames++;
                if(tempMovingFrames == 0.4 * gp.FPS) {
                    tempMoving = false;
                    tempMovingFrames = 0;
                }
            }

            //Agressive modes
            if (playerDistance < gp.squareSize * 2) {
                speed = 2;
            } else {
                speed = 1;

                framesToCount++;

                if(framesToCount == requiredReloadingFrames) {

                    gp.bullets.add(new Bullet(gp, "enemy_bullet",
                            2, 2,
                            6, 6,
                            (int)screenX + 19, (int)screenY + 30,
                            (int)gp.player.screenX + 19, (int)gp.player.screenY + 30, "alien", false));

                    gp.playSound(2);
                    framesToCount = 0;
                }
            }

        }

        collisionOnX = false;
        collisionOnY = false;

        // CHECK COLLISION OF THE MAP
        gp.colViewer.checkMapCollision(this);
        //  CHECK COLLISION OF OBJECTS
        gp.colViewer.checkObjectCollision(this, false);
        // CHECK COLLISION WITH CHARACTERS
        gp.colViewer.checkCharacterCollision(this, false);

        //HORIZONTAL MOVEMENT
        if (!collisionOnX && moving) {
            if (direction.equals("left")) {
                screenX -= speed;
            }
            if (direction.equals("right")) {
                screenX += speed;
            }
        }
        //VERTICAL MOVEMENT
        if(!collisionOnY && moving) {
            if (direction.equals("up")) {
                screenY -= speed;
            }
            if (direction.equals("down")) {
                screenY += speed;
            }
            updateImage();
        }
        //ENEMY STANDS STILL
        else {
            standFrames ++;
            if(standFrames >= gp.FPS/3) {
                imageNum = 1;
            }
        }
    }

    @Override
    public void receiveDamage(int damage) {
        if(HP > 0) {
            HP -= damage;
        }

        if (HP <= 0){
            isDead = true;
            if(!gp.roomManager.currentRoom.name.equals("finalMap")) {
                gp.roomManager.currentRoom.addGameObject(new Coin(gp), screenX, screenY - gp.squareSize);
            }
        }
    }
}
