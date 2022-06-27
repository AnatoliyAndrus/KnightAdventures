/**
 * @author Anatolii Andrusenko, Vladislav Marchenko, Andrii Sulimenko
 *
 * @version 1.0
 *
 * this class is for making objects. Game object has area of collision, image
 */
package gameobject;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameObject {

    //GAME PANEL
    public GamePanel gp;

    //IMAGE FRAMES
    public int imageFrames = 0;
    public int imageNum = 0;

    //POSITION OF OBJECT
    public double screenX;
    public double screenY;

    //AREA OF COLLISION
    public Rectangle areaOfCollision;
    public int defaultCollisionAreaX;
    public int defaultCollisionAreaY;

    //NAME OF OBJECT
    public String name;

    //DAMAGE
    public int damage;

    /**
     * constructor
     * @param gp game panel
     */
    public GameObject(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * method to set image of object
     * @param imagePath path to image (resources/images/[imagePath].png)
     * @return image
     */
    public BufferedImage setImage(String imagePath) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("resources/images/" + imagePath + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    /**
     * method to update object
     */
    public void update() {

    }

    /**
     * method to draw object
     * @param g2d
     */
    public void draw(Graphics2D g2d) {

    }
}
