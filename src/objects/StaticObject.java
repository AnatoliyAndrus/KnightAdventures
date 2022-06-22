package objects;

import gameobject.GameObject;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StaticObject extends GameObject {

    public final static int NO_ANIMATION = 0;
    public final static int ANIMATION_CONTINUOUSLY = 1;
    public final static int ANIMATION_ONCE = 2;
    public final static int ANIMATION_ONCE_REVERSE = 3;

    public ArrayList<BufferedImage> images;
    public BufferedImage noAnimation;
    public int isAnimated;
    public int framesToChangeSprite;

    public boolean collision;
    public boolean isInteracted;
    public int interactingFrames = 0;

    public StaticObject(GamePanel gp, String name) {
        super(gp);
        this.name = name;
        images = new ArrayList<>();
    }

    public void addImage(String imageName) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("resources/images/objects/" + name + "/" + imageName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        images.add(image);
    }

    @Override
    public void update() {
        //CONTINUOUS ANIMATION
        if(isAnimated == ANIMATION_CONTINUOUSLY) {
            if(imageNum < 0) imageNum = 0;

            imageFrames++;
            if(imageFrames == framesToChangeSprite) {
                imageFrames = 0;
                imageNum++;
                if(imageNum == images.size()) imageNum = 0;
            }
        } else {
            imageNum = -1;
        }

        //ONE ANIMATION CYCLE


        //ONE REVERSE ANIMATION CYCLE


        //INTERACTION TIMEOUT
        if (interactingFrames > 0) {
            interactingFrames--;
            isInteracted = true;
        } else {
            isInteracted = false;
            gp.player.interactedObject = "";
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        if(imageNum == -1) g2d.drawImage(noAnimation, screenX, screenY, null);
        else g2d.drawImage(images.get(imageNum), screenX, screenY, null);
    }

    public void setAnimation(int animationType) {
        this.isAnimated = animationType;
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
