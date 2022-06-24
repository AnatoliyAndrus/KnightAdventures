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

    public boolean changing;

    public ArrayList<BufferedImage> images;
    public BufferedImage noAnimation;
    public int animation;
    public int framesToChangeSprite;

    public boolean collision;
    public boolean isInteracted;
    public int interactingFrames = 0;

    public String relatedRoom;
    public boolean unlocked;

    public boolean isGarbage;

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
        if (animation == NO_ANIMATION){
            imageNum = -1;
        }
        //CONTINUOUS ANIMATION
        else if(animation == ANIMATION_CONTINUOUSLY) {
            if(imageNum < 0) imageNum = 0;

            imageFrames++;
            if(imageFrames == framesToChangeSprite) {
                imageFrames = 0;
                imageNum++;
                if(imageNum == images.size()) imageNum = 0;
            }
        }
        //ONE ANIMATION CYCLE
        else if(animation == ANIMATION_ONCE) {
            if(imageNum < 0 || changing) {
                imageNum = 0;
                changing = false;
            }

            imageFrames++;
            if(imageFrames == framesToChangeSprite) {
                imageFrames = 0;
                imageNum++;
                if(imageNum == images.size() - 1) {
                    setAnimation(NO_ANIMATION);
                    if((name.equals("door_horizontal") || name.equals("door_vertical"))) {
                        collision = true;
                        gp.roomManager.currentRoom.doorsClosingNow--;
                        if(!gp.roomManager.currentRoom.enemiesSpawned) {
                            gp.roomManager.currentRoom.setEnemiesInRoom();
                        }
                    }
                    if(name.equals("lever")) {
                        switch(gp.roomManager.currentRoom.name) {

                            case "finalMap" ->
                                    gp.roomManager.currentRoom.bossSpawned = true;
                        }
                    }
                    noAnimation = images.get(images.size() - 1);
                    imageNum = -1;
                }
            }
        }
        //ONE REVERSE ANIMATION CYCLE
        else if(animation == ANIMATION_ONCE_REVERSE) {
            if(imageNum < 0 || changing) {
                imageNum = images.size() - 1;
                changing = false;
            }

            imageFrames++;
            if(imageFrames == framesToChangeSprite) {
                imageFrames = 0;
                imageNum--;
                if(imageNum == 0) {
                    setAnimation(NO_ANIMATION);
                    if((name.equals("door_horizontal") || name.equals("door_vertical"))) {
                        collision = false;
                        gp.roomManager.currentRoom.doorsOpeningNow--;
                    }
                    noAnimation = images.get(0);
                    imageNum = -1;
                }
            }
        }

        //INTERACTION TIMEOUT
        if (interactingFrames > 0) {
            interactingFrames--;
            isInteracted = true;

            if (interactingFrames == 0) {
                isInteracted = false;
                gp.player.interactedObjectName = "";
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        if(imageNum == -1) g2d.drawImage(noAnimation, (int)screenX, (int)screenY, null);
        else g2d.drawImage(images.get(imageNum), (int)screenX, (int)screenY, null);
    }

    public void setAnimation(int animationType) {
        this.animation = animationType;
        if (animationType == ANIMATION_ONCE || animationType == ANIMATION_ONCE_REVERSE) {
            changing = true;
        }
    }

    public void shadow(Graphics2D g2d) {
        for (int i = 0; i < 24; i++) {
            g2d.setColor(new Color(0, 0, 0, 10));
            g2d.fillRect((int)screenX + areaOfCollision.x - i,
                    (int)screenY + areaOfCollision.y - i,
                    areaOfCollision.width,
                    areaOfCollision.height);
        }
    }
}
