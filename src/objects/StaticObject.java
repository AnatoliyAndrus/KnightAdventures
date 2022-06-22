package objects;

import gameobject.GameObject;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StaticObject extends GameObject {

    public boolean collision;
    public boolean isInteracted;
    public int interactingFrames = 0;

    public StaticObject(GamePanel gp, String name) {
        super(gp);
        this.name = name;
    }

    @Override
    public void update() {
        if (interactingFrames > 0) {
            interactingFrames--;
            isInteracted = true;
        } else isInteracted = false;
    }

    @Override
    public void draw(Graphics2D g2d) {

        BufferedImage image = switch (imageNum) {
            case 1 -> down1;
            case 2 -> down2;
            case 3 -> down3;
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
