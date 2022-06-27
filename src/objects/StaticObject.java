/**
 * @author Anatolii Andrusenko, Vladislav Marchenko, Andrii Sulimenko
 *
 * @version 1.0
 *
 * this is to make static objects, which can't move. They can have animation, collision, action
 */
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

    //ANIMATION STATES
    public final static int NO_ANIMATION = 0;
    public final static int ANIMATION_CONTINUOUSLY = 1;
    public final static int ANIMATION_ONCE = 2;
    public final static int ANIMATION_ONCE_REVERSE = 3;
    public boolean changing;

    //IMAGES AND FRAMES OF ANIMATION
    public ArrayList<BufferedImage> images;
    public BufferedImage noAnimation;
    public int animation;
    public int framesToChangeSprite;

    //COLLISION OF STATIC OBJECT
    public boolean collision;
    //IS OBJECT INTERACTED AT THE MOMENT
    public boolean isInteracted;
    //FRAMES OF INTERACTION
    public int interactingFrames = 0;

    //ROOM WHICH OBJECT IS RELATED TO
    public String relatedRoom;
    //BOOLEAN UNLOCKED
    public boolean unlocked;
    //BOOLEAN IS OPENED
    public boolean isOpened;
    //BOOLEAN CHEST
    public boolean emptyChest;
    //BOOLEAN IS GARBAGE
    public boolean isGarbage;

    /**
     * constructor
     * @param gp game panel
     * @param name name of object
     */
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

    /**
     * method to update state of object
     */
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
                    if(name.equals("door_horizontal") || name.equals("door_vertical")) {
                        collision = true;
                        gp.roomManager.currentRoom.doorsClosingNow--;
                        if(!gp.roomManager.currentRoom.enemiesSpawned) {
                            gp.roomManager.currentRoom.setEnemiesInRoom();
                        }
                    }
                    if (name.equals("boss_door")) {
                        collision = false;
                        gp.roomManager.currentRoom.doorsOpeningNow--;
                    }
                    if(name.equals("chest")) {
                        isOpened = true;
                        gp.player.interactedObjectName = "";
                    }
                    if(name.equals("lever")) {
                        switch(gp.roomManager.currentRoom.name) {
                            case "ruins" -> {
                                gp.roomManager.currentRoom.phase = "completed";
                                gp.roomManager.currentRoom.changingPhase = true;
                            }
                            case "finalMap" -> {
                                gp.roomManager.currentRoom.staticObjects.get(1).setAnimation(ANIMATION_ONCE_REVERSE);
                                gp.playSound(14);
                                gp.roomManager.currentRoom.doorsClosingNow++;
                                gp.roomManager.currentRoom.bossSpawned = true;
                            }
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
                    if(name.equals("door_horizontal") || name.equals("door_vertical")) {
                        collision = false;
                        gp.roomManager.currentRoom.doorsOpeningNow--;
                    }
                    if (name.equals("boss_door")) {
                        collision = true;
                        gp.roomManager.currentRoom.doorsClosingNow--;
                        if(!gp.roomManager.currentRoom.enemiesSpawned) {
                            gp.roomManager.currentRoom.setEnemiesInRoom();
                        }
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

    /**
     * method to draw object
     * @param g2d graphics 2d
     */
    @Override
    public void draw(Graphics2D g2d) {
        if(imageNum == -1) g2d.drawImage(noAnimation, (int)screenX, (int)screenY, null);
        else g2d.drawImage(images.get(imageNum), (int)screenX, (int)screenY, null);
    }

    /**
     * method to set animation
     * @param animationType type of animation
     */
    public void setAnimation(int animationType) {
        this.animation = animationType;
        if (animationType == ANIMATION_ONCE || animationType == ANIMATION_ONCE_REVERSE) {
            changing = true;
        }
    }

    /**
     * method to set shadow
     * @param g2d graphics 2d
     */
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
