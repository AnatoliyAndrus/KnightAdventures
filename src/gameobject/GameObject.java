package gameobject;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameObject {

    public GamePanel gp;

    public int imageFrames = 0;
    public int imageNum = 0;

    public double screenX;
    public double screenY;

    public Rectangle areaOfCollision;
    public int defaultCollisionAreaX;
    public int defaultCollisionAreaY;

    public String name;

    public int damage;

    public GameObject(GamePanel gp) {
        this.gp = gp;
    }

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
