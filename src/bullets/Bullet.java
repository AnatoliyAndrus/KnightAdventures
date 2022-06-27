/**
 * @author Anatolii Andrusenko, Vladislav Marchenko, Andrii Sulimenko
 *
 * @version 1.0
 *
 * class of bullets. Can provide different types of bullet
 */
package bullets;

import characters.Boss;
import characters.EnemyWithPistol;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Bullet {

    //GAME PANEL
    GamePanel gp;
    //IMAGES OF BULLET
    public ArrayList<BufferedImage> images;
    //IMAGE WHEN BULLET HAS NO ANIMATION
    BufferedImage noAnimation;
    //NAME OF BULLET
    public String bulletName;

    //AREA OF COLLISION OF BULLET
    public Rectangle areaOfCollision;

    public int defaultCollisionAreaX;
    public int defaultCollisionAreaY;

    //SPEED OF BULLET
    public int speed;
    public double speedX;
    public double speedY;

    //INITIAL POSITION OF BULLET
    public int initialX;
    public int initialY;
    //FINAL POS OF BULLET (TO DEFINE VECTOR OF MOVING)
    public int finalX;
    public int finalY;
    double deltaX;
    double deltaY;

    //POSITION OF BULLET
    public double screenX;
    public double screenY;

    //PROPORTION (FOR VECTOR OF MOVING)
    public double proportion;

    //DIRECTION OF MOVING
    public String direction;

    //DAMAGE OF BULLET
    public int damage;

    //NAME OF SHOOTER
    public String shooter;
    public boolean changedPlayerDir;

    //DEFINES IF THE BULLET IS BOSS MORTAR
    public boolean bossMortar;

    /**
     * constructor
     * @param gp game panel
     * @param bulletName name of bullet
     * @param defaultCollisionAreaX collision area
     * @param defaultCollisionAreaY collision area
     * @param collisionWidth width of collision area
     * @param collisionHeight height of collision area
     * @param initialX initial x position of bullet
     * @param initialY initial y position of bullet
     * @param finalX final x position of bullet
     * @param finalY final y position of bullet
     * @param shooter shooter of bullet
     * @param bossMortar true if the bullet is boss mortar, false otherwise
     */
    public Bullet(GamePanel gp, String bulletName,
                  int defaultCollisionAreaX,
                  int defaultCollisionAreaY,
                  int collisionWidth,
                  int collisionHeight,
                  int initialX,
                  int initialY,
                  int finalX,
                  int finalY,
                  String shooter, boolean bossMortar) {

        this.gp = gp;

        try {
            this.noAnimation = ImageIO.read(new File("resources/images/bullets/" + bulletName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bulletName = bulletName;

        this.shooter = shooter;
        this.bossMortar = bossMortar;

        switch (shooter) {
            case "player" -> {
                speed = 15;
                damage = 1;
            }
            case "alien" -> {
                speed = 5;
                damage = EnemyWithPistol.constDamage;
            }
            case "boss" -> {
                if(bossMortar) {
                    speed = 10;
                    damage = Boss.constDamage * 2;
                } else {
                    speed = 10;
                    damage = Boss.constDamage;
                }
            }
        }

        this.defaultCollisionAreaX = defaultCollisionAreaX;
        this.defaultCollisionAreaY = defaultCollisionAreaY;

        this.areaOfCollision = new Rectangle(defaultCollisionAreaX,
                defaultCollisionAreaY,
                collisionWidth,
                collisionHeight);

        this.initialX = initialX;
        this.initialY = initialY;

        this.finalX = finalX;
        this.finalY = finalY;

        this.screenX = initialX;
        this.screenY = initialY;

        setDefaultValues();
    }

    /**
     * method which updates bullet
     */
    public void update(){

        if(bossMortar) {
            if(deltaY > 0) screenY += speed;
            else screenY -= speed;
        } else {
            switch (direction) {
                case "upLeft" -> {
                    screenX -= speedX;
                    screenY -= speedY;
                    if (shooter.equals("player") && !changedPlayerDir) {
                        if (lookingUpOrDown()) gp.player.direction = "up";
                        else gp.player.direction = "left";
                        changePlayerDirection();
                    }
                }
                case "upRight" -> {
                    screenX += speedX;
                    screenY -= speedY;
                    if (shooter.equals("player") && !changedPlayerDir) {
                        if (lookingUpOrDown()) gp.player.direction = "up";
                        else gp.player.direction = "right";
                        changePlayerDirection();
                    }
                }
                case "downLeft" -> {
                    screenX -= speedX;
                    screenY += speedY;
                    if (shooter.equals("player") && !changedPlayerDir) {
                        if (lookingUpOrDown()) gp.player.direction = "down";
                        else gp.player.direction = "left";
                        changePlayerDirection();
                    }
                }
                case "downRight" -> {
                    screenX += speedX;
                    screenY += speedY;
                    if (shooter.equals("player") && !changedPlayerDir) {
                        if (lookingUpOrDown()) gp.player.direction = "down";
                        else gp.player.direction = "right";
                        changePlayerDirection();
                    }
                }
            }
        }

        if(!bossMortar) {
            //CHECK MAP COLLISION
            gp.colViewer.checkMapCollision(this);
            //CHECK OBJECT COLLISION
            gp.colViewer.checkObjectCollision(this);
            //CHECK CHARACTERS COLLISION
            gp.colViewer.checkCharacterCollision(this);

            if(screenX < 0 || screenX > gp.squareSize*(gp.maxCols - 2) || screenY < 0 || screenY > gp.squareSize*(gp.maxRows - 2)) {
                gp.bullets.remove(this);
            }
        } else {
            if(screenY >= finalY && bulletName.equals("small_missile_3")) {
                for (int i = 0; i < gp.roomManager.currentRoom.staticObjects.size(); i++) {
                    if(gp.roomManager.currentRoom.staticObjects.get(i).name.equals("target") &&
                            gp.roomManager.currentRoom.staticObjects.get(i).screenX == finalX &&
                            gp.roomManager.currentRoom.staticObjects.get(i).screenY == finalY) {
                        gp.roomManager.currentRoom.staticObjects.get(i).isGarbage = true;
                        break;
                    }
                }
                if(new Rectangle((int)gp.player.screenX, (int)gp.player.screenY, 48, 48).intersects(new Rectangle(finalX, finalY, 48, 48)) &&
                        !gp.player.isInvincible) {
                    gp.player.receiveDamage(damage);
                    gp.player.isInvincible = true;
                }
                gp.bullets.remove(this);
            }

            if(screenX < -gp.squareSize || screenX > gp.squareSize*(gp.maxCols - 1) || screenY < -gp.squareSize * 2 || screenY > gp.squareSize*(gp.maxRows - 1)) {
                gp.bullets.remove(this);
            }
        }
    }

    /**
     * method to draw bullet
     * @param g2d graphics 2d
     */
    public void draw(Graphics2D g2d){

        g2d.drawImage(noAnimation, (int)screenX, (int)screenY, null);

    }

    /**
     * method which sets default values of bullet
     */
    private void setDefaultValues() {
        deltaX = finalX - initialX;
        deltaY = finalY - initialY;

        if (deltaX > 0 && deltaY > 0) {
            direction = "downRight";
        } else if (deltaX > 0 && deltaY < 0) {
            direction = "upRight";
        } else if (deltaX < 0 && deltaY < 0) {
            direction = "upLeft";
        } else if (deltaX < 0 && deltaY > 0) {
            direction = "downLeft";
        } else if(deltaX == 0 && deltaY < 0) {
            direction = "upRight";
            deltaX = 1;
        } else if(deltaX == 0 && deltaY > 0) {
            direction = "downRight";
            deltaX = 1;
        } else if(deltaX < 0 && deltaY == 0) {
            direction = "downLeft";
            deltaY = 1;
        } else if(deltaX > 0 && deltaY == 0) {
            direction = "downRight";
            deltaY = 1;
        } else if(deltaX == 0 && deltaY == 0) {
            direction = "upRight";
            deltaX = 1;
            deltaY = -1;
        }

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            proportion = Math.abs(deltaX/deltaY);
            speedY = Math.sqrt(Math.pow(speed, 2) / (1 + Math.pow(proportion, 2)));
            speedX = speedY * proportion;
        } else {
            proportion = Math.abs(deltaY/deltaX);
            speedX = Math.sqrt(Math.pow(speed, 2) / (1 + Math.pow(proportion, 2)));
            speedY = speedX * proportion;
        }
    }

    /**
     * method which changes direction of player
     * is used to rotate player when he shoots
     */
    private void changePlayerDirection() {
        gp.player.lastBulletDirection = gp.player.direction;
        gp.player.lastBulletFrames = 6;
        changedPlayerDir = true;
    }

    /**
     * method which checks direction of player
     * @return true if player is looking up and false otherwise
     */
    private boolean lookingUpOrDown() {
        return (Math.abs(deltaY) >= Math.abs(deltaX) && proportion >= 2);
    }
}


