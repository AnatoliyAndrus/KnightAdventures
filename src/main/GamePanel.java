package main;

import bullets.Bullet;
import characters.Player;
import objects.GameObject;
import rooms.RoomManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{

    //GAME SCREEN
    public final int squareSize = 48; // 48x48px

    public final int maxCols = 27;
    public final int maxRows = 19;
    final int maxScreenWidth = (maxCols - 2) * squareSize;
    final int maxScreenHeight = (maxRows - 2) * squareSize;

    //GAME FPS
    public final int FPS = 60;

    //SYSTEM
    Thread gameThread;
    public KeyRecorder keyR = new KeyRecorder(this);
    public MouseRecorder mouseR = new MouseRecorder(this);
    public RoomManager roomManager = new RoomManager(this);
    public UI ui = new UI(this);
    public CollisionViewer colViewer = new CollisionViewer(this);


    //GAME CHARACTERS
    public Player player = new Player(this, keyR);

    //BULLETS
    public ArrayList<Bullet> bullets = new ArrayList<>();

    //SOUNDS
    public Music music = new Music();
    int currentSong = -1;
    public Music sounds = new Music();

    //GAME STATE
    public final int pauseState = 0;
    public final int gameState = 1;
    public int currentState = gameState;

    public GamePanel() {

        //SETTINGS
        this.setPreferredSize(new Dimension(maxScreenWidth, maxScreenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        //KEYS LISTENER
        this.addKeyListener(keyR);
        this.addMouseListener(mouseR);
        this.setFocusable(true);
    }

    public void setupGame(){

        //DEFAULT SETUP
//        playMusic(0);
    }

    public void startGameThread() {

        //STARTING THREAD
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double frameInterval = (double)1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / frameInterval;

            lastTime = currentTime;

            //UPDATE AND REPAINT 60 FPS
            if(delta >= 1) {
                System.out.println(bullets.size());
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {

        //GAME STATE
        if (currentState == gameState) {

            //PLAYER
            player.update();

            //BULLETS
            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).update();
            }

        }
        //PAUSE STATE
        if (currentState == pauseState) {

        }

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        if (true) {
            //MAP
            roomManager.draw(g2d);

            // OBJECTS
            for (GameObject obj : roomManager.currentRoom.gameObjects) {
                if (obj != null && obj.collision) obj.shadow(g2d);
            }
            for (GameObject obj : roomManager.currentRoom.gameObjects) {
                if (obj != null) obj.draw(g2d);
            }

            //BULLETS
            if(bullets.size() > 0) {
                for (Bullet bullet : bullets) {
                    bullet.draw(g2d);
                }
            }

            //PLAYER
            player.draw(g2d);

            //UI
            ui.draw(g2d);
        }

        g2d.dispose();
    }

    public void playMusic(int index) {
        if(currentSong != index) {
            music.setClip(index);
            music.currentSecond = 0;
            currentSong = index;
        }
        music.play();
        music.loop();
    }
    public void pauseMusic() {
        music.stop();
    }
    public void playSound(int index) {
        sounds.setClip(index);
        sounds.play();
    }
}
