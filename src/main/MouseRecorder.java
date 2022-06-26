package main;

import bullets.Bullet;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseRecorder implements MouseListener {

    GamePanel gp;

    public MouseRecorder(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

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
