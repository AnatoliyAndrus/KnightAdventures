package gameobject;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameObject {

    public GamePanel gp;

    public BufferedImage down1, down2, down3;

    public int screenX;
    public int screenY;

    public int imageCount = 0;
    public int imageNum = 1;

    public Rectangle areaOfCollision;
    public int defaultCollisionAreaX;
    public int defaultCollisionAreaY;

    public String direction = "down";

    public GameObject(GamePanel gp) {this.gp = gp;}

    public BufferedImage setImage(String imagePath) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("resources/images/" + imagePath + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void update() {

    }

    public void draw(Graphics2D g2d) {

    }
}
