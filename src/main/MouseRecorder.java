/**
 * @author Anatolii Andrusenko, Vladislav Marchenko, Andrii Sulimenko
 *
 * @version 1.0
 *
 * mouse recorder class which manages all mouse actions
 */
package main;

import bullets.Bullet;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseRecorder implements MouseListener {

    //GAME PANEL
    GamePanel gp;

    /**
     * constructor
     * @param gp
     */
    public MouseRecorder(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * method to manage presses of the mouse. Actually, it only manages the shooting of player
     * @param e mouse event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(gp.currentState == gp.gameState && !gp.player.isReloading && !gp.player.scriptsAreActive) {
            gp.player.shooting=true;
            gp.player.finalBulletX=e.getX();
            gp.player.finalBulletY=e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
