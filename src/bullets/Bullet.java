package bullets;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bullet {

    GamePanel gp;
    BufferedImage img;

    public Rectangle areaOfCollision;

    public int defaultCollisionAreaX;
    public int defaultCollisionAreaY;

    public final int speed = 10;
    public double speedX;
    public double speedY;

    public int initialX;
    public int initialY;
    public int finalX;
    public int finalY;

    public double screenX;
    public double screenY;

    public double proportion;

    public String direction;

    public boolean shooterIsPlayer;

    public Bullet(GamePanel gp, String imgName,
                  int defaultCollisionAreaX,
                  int defaultCollisionAreaY,
                  int collisionWidth,
                  int collisionHeight,
                  int initialX,
                  int initialY,
                  int finalX,
                  int finalY,
                  boolean shooterIsPlayer) {

        this.gp = gp;

        try {
            this.img = ImageIO.read(new File("resources/images/bullets/" + imgName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
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

        this.shooterIsPlayer = shooterIsPlayer;

        setDefaultValues();
    }

    public void update(){

        switch (direction) {
            case "upLeft" -> {
                screenX -= speedX;
                screenY -= speedY;
                if(shooterIsPlayer) gp.player.direction = "left";
            }
            case "upRight" -> {
                screenX += speedX;
                screenY -= speedY;
                if(shooterIsPlayer) gp.player.direction = "right";
            }
            case "downLeft" -> {
                screenX -= speedX;
                screenY += speedY;
                if(shooterIsPlayer) gp.player.direction = "left";
            }
            case "downRight" -> {
                screenX += speedX;
                screenY += speedY;
                if(shooterIsPlayer) gp.player.direction = "right";
            }
        }

        //CHECK MAP COLLISION
        gp.colViewer.checkMapCollision(this);
        //CHECK OBJECT COLLISION
        gp.colViewer.checkObjectCollision(this);
        //CHECK CHARACTERS COLLISION
        gp.colViewer.checkCharacterCollision(this);

        if(screenX < 0 || screenX > gp.squareSize*(gp.maxCols - 2) || screenY < 0 || screenY > gp.squareSize*(gp.maxRows - 2)) {
            gp.bullets.remove(this);
        }
    }
    public void draw(Graphics2D g2d){

        g2d.drawImage(img, (int)screenX, (int)screenY, null);

    }
    private void setDefaultValues() {
        double deltaX = finalX - initialX;
        double deltaY = finalY - initialY;

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
}


