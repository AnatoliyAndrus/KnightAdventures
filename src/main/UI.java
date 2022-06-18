package main;

import java.awt.*;

public class UI {

    GamePanel gp;

    public UI(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2d) {

        //GAME STATE
        if (gp.currentState == gp.gameState) {
            drawDarkScreen(g2d);
        }
        //PAUSE STATE
        if (gp.currentState == gp.pauseState) {
            drawDarkScreen(g2d);
            drawPauseScreen(g2d);
        }
    }

    private void drawDarkScreen(Graphics2D g2d) {
        //UP
        if(gp.player.screenY < gp.squareSize * 2 - 16) {
            int darkAlpha = (int) ((double)(gp.squareSize * 2 - 16 - gp.player.screenY) / (gp.squareSize*2) * 150);
            g2d.setColor(new Color(0, 0, 0, darkAlpha));
            g2d.fillRect(0, 0, gp.squareSize*(gp.maxCols - 2), gp.squareSize*(gp.maxRows - 2));
        }
        //DOWN
        else if(gp.player.screenY > gp.squareSize * (gp.maxRows - 5)) {
            int darkAlpha = (int) ((double)(gp.player.screenY - gp.squareSize * (gp.maxRows - 5)) / (gp.squareSize*2) * 150);
            g2d.setColor(new Color(0, 0, 0, darkAlpha));
            g2d.fillRect(0, 0, gp.squareSize*(gp.maxCols - 2), gp.squareSize*(gp.maxRows - 2));
        }
        //LEFT
        else if(gp.player.screenX < gp.squareSize * 2 - 8) {
            int darkAlpha = (int) ((double)(gp.squareSize * 2 - 8 - gp.player.screenX) / (gp.squareSize*2) * 150);
            g2d.setColor(new Color(0, 0, 0, darkAlpha));
            g2d.fillRect(0, 0, gp.squareSize*(gp.maxCols - 2), gp.squareSize*(gp.maxRows - 2));
        }
        //RIGHT
        else if(gp.player.screenX > gp.squareSize * (gp.maxCols - 5) + 8) {
            int darkAlpha = (int) ((double)(gp.player.screenX - (gp.squareSize * (gp.maxCols - 5) + 8)) / (gp.squareSize*2) * 150);
            g2d.setColor(new Color(0, 0, 0, darkAlpha));
            g2d.fillRect(0, 0, gp.squareSize*(gp.maxCols - 2), gp.squareSize*(gp.maxRows - 2));
        }
    }

    private void drawPauseScreen(Graphics2D g2d){

        g2d.setColor(new Color(0,0,0,100));
        g2d.fillRect(0, 0, gp.squareSize*(gp.maxCols - 2), gp.squareSize*(gp.maxRows - 2));

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 30));

        String screenText = "Paused";

        int length = (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth();

        int y = gp.maxScreenHeight/2;
        int x = gp.maxScreenWidth/2 - length/2;

        g2d.drawString(screenText, x, y);
    }
}
