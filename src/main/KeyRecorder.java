package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyRecorder implements KeyListener {

    GamePanel gp;
    public boolean up, down, left, right;

    public KeyRecorder(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        //MOVEMENT
        if (e.getKeyCode() == KeyEvent.VK_W) {
            up = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            down = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            left = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            right = true;
        }
        //PAUSE
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (gp.currentState == gp.gameState) {
                gp.currentState = gp.pauseState;
                gp.pauseMusic();
            }
            else if (gp.currentState == gp.pauseState) {
                gp.currentState = gp.gameState;
                gp.playMusic(0);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        //MOVEMENT
        if (e.getKeyCode() == KeyEvent.VK_W) {
            up = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            down = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            left = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            right = false;
        }
    }
}
