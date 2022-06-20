package objects;

import main.GamePanel;
import rooms.Room;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameObject {

    public BufferedImage image1, image2, image3;

    public String name;

    public int imageNum = 1;

    public int defaultCollisionAreaX;
    public int defaultCollisionAreaY;

    public Rectangle areaOfCollision;
    public boolean collision;

    public int screenX;
    public int screenY;

    GamePanel gp;

    public GameObject(GamePanel gp, String name, String imageName) {
        this.gp = gp;
        this.name = name;

        try {
            this.image1 = ImageIO.read(new File("resources/images/objects/" + imageName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2d) {

        BufferedImage image = switch (imageNum) {
            case 1 -> image1;
            case 2 -> image2;
            case 3 -> image3;
            default -> null;
        };

        g2d.drawImage(image, screenX, screenY, null);
    }

    public void shadow(Graphics2D g2d) {
        for (int i = 0; i < 24; i++) {
            g2d.setColor(new Color(0, 0, 0, 10));
            g2d.fillRect(screenX + areaOfCollision.x - i,
                    screenY + areaOfCollision.y - i,
                    areaOfCollision.width,
                    areaOfCollision.height);
        }
    }
}
