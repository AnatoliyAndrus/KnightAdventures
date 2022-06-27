/**
 * @author Anatolii Andrusenko, Andrii Sulimenko, Vladyslav Marchenko
 * @version 1.0
 *
 * class Room which represents game rooms as data type
 */
package rooms;

import characters.Boss;
import characters.Character;
import characters.EnemyWithFangs;
import characters.EnemyWithPistol;
import objects.StaticObject;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Room {

    public String name;

    RoomManager sq;

    //OBJECTS IN ROOM
    public ArrayList<StaticObject> staticObjects;
    public ArrayList<Character> enemies;
    public ArrayList<Character> enemiesData;
    public int[][] roomMatrix;

    //NAVIGATION
    public Room upRoom;
    public Room downRoom;
    public Room leftRoom;
    public Room rightRoom;

    public Square[] squares;

    //SONGS
    public int songIndex;
    public int fightingSongIndex;

    //INDICATORS
    public boolean isWaterRoom;

    public boolean playerEntered;
    public String phase = "initial";
    public boolean changingPhase;
    public boolean enemiesSpawned;
    public boolean bossSpawned;

    public int doorsOpeningNow = 0;
    public int doorsClosingNow = 0;

    /**
     * constructor
     * @param name name
     * @param songIndex song
     * @param fightingSongIndex song for fighting
     * @param roomManager room manager
     */
    public Room(String name, int songIndex, int fightingSongIndex, RoomManager roomManager) {

        this.name = name;
        squares = new Square[30];

        this.sq = roomManager;

        roomMatrix = new int[roomManager.gp.maxCols][roomManager.gp.maxRows];

        staticObjects = new ArrayList<>();
        enemies = new ArrayList<>();
        enemiesData = new ArrayList<>();

        this.songIndex = songIndex;
        this.fightingSongIndex = fightingSongIndex;
    }

    /**
     * sets images data
     * @param room room name
     */
    public void setAllImages(String room) {
        //EMPTY
        setup(room, 0, "tile_1", false);
        setup(room, 1, "tile_1", false);
        setup(room, 2, "tile_1", false);
        setup(room, 3, "tile_1", false);
        setup(room, 4, "tile_1", false);
        setup(room, 5, "tile_1", false);
        setup(room, 6, "tile_1", false);
        setup(room, 7, "tile_1", false);
        setup(room, 8, "tile_1", false);
        setup(room, 9, "tile_1", false);
        //FLOOR
        setup(room, 10, "tile_1", false);
        setup(room, 11, "tile_2", false);
        setup(room, 12, "tile_3", false);
        //WALLS
        setup(room, 13, "wall_up", true);
        setup(room, 14, "wall_down", true);
        setup(room, 15, "wall_left", true);
        setup(room, 16, "wall_right", true);
        setup(room, 17, "wall_up-left-corner", true);
        setup(room, 18, "wall_up-right-corner", true);
        setup(room, 19, "wall_down-left-corner", true);
        setup(room, 20, "wall_down-right-corner", true);
        setup(room, 21, "wall_up-left-door", true);
        setup(room, 22, "wall_up-right-door", true);
        setup(room, 23, "wall_down-left-door", true);
        setup(room, 24, "wall_down-right-door", true);
        setup(room, 25, "wall_standart", true);

        if (name.equals("finalMap")) {
            setup(room, 26, "wall_addition-left-corner", true);
            setup(room, 27, "wall_addition-right-corner", true);
        }
    }

    /**
     * setup 1 image
     * @param room room name
     * @param index image index
     * @param name image name
     * @param collision square collision
     */
    private void setup(String room, int index, String name, boolean collision) {
        try {
            squares[index] = new Square();
            squares[index].img = ImageIO.read(new File("resources/images/" + room + "/" + name + ".png"));
            squares[index].collision = collision;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * set matrix data for room
     * @param roomName room name
     */
    public void setMatrix(String roomName) {

        //READING MATRIX FROM FILE
        try (BufferedReader br = new BufferedReader(new FileReader("resources/maps/" + roomName + ".txt"))) {

            for (int row = 0; row < sq.gp.maxRows; row++) {
                String s = br.readLine();
                String[] array = s.split(" ");

                for (int col = 0; col < array.length; col++) {

                    if (Integer.parseInt(array[col]) > 12) {
                        roomMatrix[col][row] = Integer.parseInt(array[col]);
                    } else {
                        roomMatrix[col][row] = (int) (Math.random() * 3 + 10);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * add 1 game object to room
     * @param staticObject object
     * @param screenX x coordinate
     * @param screenY y coordinate
     */
    public void addGameObject(StaticObject staticObject, double screenX, double screenY) {
        staticObjects.add(staticObject);
        staticObjects.get(staticObjects.size() - 1).screenX = screenX;
        staticObjects.get(staticObjects.size() - 1).screenY = screenY;
    }

    /**
     * register 1 enemy in data list
     * @param ch enemy
     */
    public void addEnemiesData(Character ch) {
        enemiesData.add(ch);
    }

    /**
     * update method for room
     */
    public void update() {
        playerEntered = checkEntrance();

        switch (phase) {
            case "initial" -> {
                if (!name.equals("finalMap")) {
                    if (playerEntered && (!name.equals("dungeon") || sq.gp.player.hasTorch)) {
                        changingPhase = true;
                        phase = "in progress";

                        sq.gp.player.backupArmor = sq.gp.player.armor;
                        sq.gp.player.backupCoinsAmount = sq.gp.player.coinsAmount;
                    }
                } else {
                    if (playerEntered && bossSpawned) {
                        changingPhase = true;
                        phase = "in progress";

                        sq.gp.player.backupArmor = sq.gp.player.armor;
                        sq.gp.player.backupCoinsAmount = sq.gp.player.coinsAmount;
                    }
                }
            }
            case "in progress" -> {
                if (enemies.size() == 0 && changingPhase) {
                    for (StaticObject staticObject: staticObjects) {
                        if ((staticObject.name.equals("door_horizontal") || staticObject.name.equals("door_vertical")) && !staticObject.collision) {
                            staticObject.setAnimation(StaticObject.ANIMATION_ONCE);
                            doorsClosingNow++;
                        }
                    }
                    if(!name.equals("finalMap"))
                        sq.gp.playSound(11);

                    changingPhase = false;
                }
                if (enemies.size() == 0 && enemiesSpawned) {
                    changingPhase = true;
                    phase = "completed";

                    sq.gp.player.HP = sq.gp.player.maxHP;

                    if(songIndex != fightingSongIndex) {
                        sq.gp.stopMusic();
                        sq.gp.playMusic(songIndex);
                    }
                }
            }
            case "completed" -> {
                if(changingPhase && playerEntered) {
                    boolean allDoorsUnlocked = true;
                    boolean doorsOpening = false;
                    for (StaticObject staticObject : staticObjects) {
                        if ((staticObject.name.equals("door_horizontal") || staticObject.name.equals("door_vertical")) &&
                                sq.rooms.get(staticObject.relatedRoom).phase.equals("completed") &&
                                !staticObject.unlocked && !sq.gp.player.hasTorch) {
                            staticObject.setAnimation(StaticObject.ANIMATION_ONCE_REVERSE);
                            staticObject.unlocked = true;
                            doorsOpening = true;
                            doorsOpeningNow++;
                        }
                        if ((staticObject.name.equals("door_horizontal") || staticObject.name.equals("door_vertical")) && !staticObject.unlocked) {
                            allDoorsUnlocked = false;
                        }
                    }
                    if(doorsOpening) {
                        sq.gp.playSound(11);
                    }
                    if(allDoorsUnlocked){
                        changingPhase = false;
                    }
                }
                if(!changingPhase) {
                    switch (name) {
                        case "ruins" -> {}
                        case "castle" -> {}
                        case "sea" -> {}
                        case "swamp" -> {}
                        case "cave" -> {}
                        case "dungeon" -> {
                            if (sq.gp.player.hasTorch) {
                                boolean doorsClosing = false;
                                for (StaticObject staticObject : staticObjects) {
                                    if ((staticObject.name.equals("door_horizontal") || staticObject.name.equals("door_vertical")) &&
                                            !staticObject.collision && staticObject.animation != StaticObject.ANIMATION_ONCE) {
                                        staticObject.setAnimation(StaticObject.ANIMATION_ONCE);
                                        doorsClosing = true;
                                        doorsClosingNow++;
                                    }
                                }
                                if(doorsClosing) {
                                    sq.gp.playSound(11);
                                }
                            } else {
                                boolean doorsOpening = false;
                                for (StaticObject staticObject : staticObjects) {
                                    if ((staticObject.name.equals("door_horizontal") || staticObject.name.equals("door_vertical")) &&
                                            staticObject.collision && staticObject.animation != StaticObject.ANIMATION_ONCE_REVERSE) {
                                        staticObject.setAnimation(StaticObject.ANIMATION_ONCE_REVERSE);
                                        doorsOpening = true;
                                        doorsOpeningNow++;
                                    }
                                }
                                if(doorsOpening) {
                                    sq.gp.playSound(11);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * checks if player entered the room
     * @return true if yes / false if not
     */
    public boolean checkEntrance() {
        return (sq.gp.player.screenX >= sq.gp.squareSize * 2 - 8 && sq.gp.player.screenX <= sq.gp.maxScreenWidth - sq.gp.squareSize * 3 + 8
                && sq.gp.player.screenY >= sq.gp.squareSize * 2 - 24 && sq.gp.player.screenY <= sq.gp.maxScreenHeight - sq.gp.squareSize * 3);
    }

    /**
     * set registered enemy data to a real list and spawns them
     */
    public void setEnemiesInRoom() {
        for (Character enemy: enemiesData) {
            switch (enemy.name) {
                case "enemyWithFangs" -> enemies.add(new EnemyWithFangs(sq.gp, enemy.screenX, enemy.screenY));
                case "alien" -> enemies.add(new EnemyWithPistol(sq.gp, enemy.screenX, enemy.screenY));
                case "boss" -> enemies.add(new Boss(sq.gp));
            }
        }
        enemiesSpawned = true;

        if (songIndex != fightingSongIndex) {
            sq.gp.stopMusic();
            sq.gp.playMusic(fightingSongIndex);
        }
    }
}
