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

        if (gp.currentState == gp.titleState) {
            if (!gp.ui.loadingScreen) {
                //TITLE
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    gp.ui.optionNum++;
                    if (gp.ui.optionNum == 4) gp.ui.optionNum = 1;
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    gp.ui.optionNum--;
                    if (gp.ui.optionNum == 0) gp.ui.optionNum = 3;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    //NEW GAME
                    if (gp.ui.optionNum == 1) {
                        gp.ui.loadingScreen = true;
                    }
                    //LOAD GAME
                    else if (gp.ui.optionNum == 2) {

                    }
                    //EXIT
                    else if (gp.ui.optionNum == 3) {
                        System.exit(0);
                    }
                }
            }
        }
        else if (gp.currentState == gp.gameState) {
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
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                gp.currentState = gp.pauseState;
//                gp.pauseMusic();
            }
        }
        //PAUSE
        else if (gp.currentState == gp.pauseState) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                gp.currentState = gp.gameState;
//                gp.playMusic(0);
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
