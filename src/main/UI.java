package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class UI {

    GamePanel gp;
    Font gameFont;
    String screenText;
    BufferedImage start, loading;

    public int optionNum = 1;
    public boolean loadingScreen;
    public int loadingFrames = 0;

    Shape ring, oval;

    public UI(GamePanel gp) {
        this.gp = gp;

        try {
            this.start = ImageIO.read(new File("resources/images/screens/startScreen.png"));
            this.loading = ImageIO.read(new File("resources/images/screens/loadingScreen.png"));
            this.gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/lunchds.ttf"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setFont(gameFont);

        //TITLE STATE
        if (gp.currentState == gp.titleState) {
            if (loadingScreen) {
                drawLoadingScreen(g2d);
            } else drawStartScreen(g2d);
        }
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

    private void drawLoadingScreen(Graphics2D g2d) {

//        loadingFrames++;
//        g2d.drawImage(loading, 0, 0, gp.maxScreenWidth, gp.maxScreenHeight, null);
//
//        //TITLE
//        screenText = "Knight adventures";
//        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 100));
//
//        int y = gp.squareSize * 3;
//        int x = getCenterX(screenText, g2d);
//
//        g2d.setColor(Color.BLACK);
//        g2d.drawString(screenText, x + 5, y + 5);
//
//        g2d.setColor(Color.WHITE);
//        g2d.drawString(screenText, x, y);
//
//        BufferedImage tmp = null;
//
//        if(loadingFrames % (gp.FPS * 2) < gp.FPS / 2) {
//            screenText = "Loading";
//            tmp = gp.player.down1;
//        } else if(loadingFrames % (gp.FPS * 2) >= gp.FPS / 2 && loadingFrames % (gp.FPS * 2) < gp.FPS) {
//            screenText = "Loading.";
//            tmp = gp.player.right1;
//        } else if(loadingFrames % (gp.FPS * 2) >= gp.FPS && loadingFrames % (gp.FPS * 2) < gp.FPS * 1.5) {
//            screenText = "Loading..";
//            tmp = gp.player.up1;
//        } else if(loadingFrames % (gp.FPS * 2) >= gp.FPS * 1.5) {
//            screenText = "Loading...";
//            tmp = gp.player.left1;
//        }
//
//        g2d.drawImage(tmp,
//                gp.squareSize/2,
//                (int) (gp.maxScreenHeight - gp.squareSize * 1.5),
//                null);
//
//        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 48));
//
//        x = (int) (gp.squareSize * 1.5);
//        y = (int) (gp.maxScreenHeight - gp.squareSize * 0.5);
//
//        g2d.setColor(Color.WHITE);
//        g2d.drawString(screenText, x, y);
//
//        if (loadingFrames == gp.FPS * 5) {
            gp.currentState = gp.gameState;
//            gp.setupGame();
//        }
    }

    private void drawStartScreen(Graphics2D g2d) {

        //TITLE
        screenText = "Knight adventures";
        g2d.drawImage(start, 0, 0, gp.maxScreenWidth, gp.maxScreenHeight, null);

        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 100));

        int y = gp.squareSize * 3;
        int x = getCenterX(screenText, g2d);

        g2d.setColor(Color.BLACK);
        g2d.drawString(screenText, x + 5, y + 5);

        g2d.setColor(Color.WHITE);
        g2d.drawString(screenText, x, y);

        //NEW GAME
        screenText = "New game";
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

        y = gp.squareSize * 7;
        x = getCenterX(screenText, g2d);

        if (optionNum == 1) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setStroke(new BasicStroke(5));
            g2d.drawRoundRect(x - 24,
                    y - (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight(),
                    (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth() + 48,
                    (int) (g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight() * 1.25),
                    25, 25);
        }

        g2d.drawString(screenText, x, y);

        //LOAD GAME
        screenText = "Load game";
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

        y = gp.squareSize * 9;
        x = getCenterX(screenText, g2d);

        if (optionNum == 2) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setStroke(new BasicStroke(5));
            g2d.drawRoundRect(x - 24,
                    y - (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight(),
                    (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth() + 48,
                    (int) (g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight() * 1.25),
                    25, 25);
        }

        g2d.drawString(screenText, x, y);


        //EXIT
        screenText = "Exit";
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

        y = gp.squareSize * 11;
        x = getCenterX(screenText, g2d);

        if (optionNum == 3) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setStroke(new BasicStroke(5));
            g2d.drawRoundRect(x - 24,
                    y - (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight(),
                    (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth() + 48,
                    (int) (g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight() * 1.25),
                    25, 25);
        }

        g2d.drawString(screenText, x, y);
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

        //ROOM DARKNESS
        if (gp.roomManager.currentRoom.darkness > 0) drawLight(g2d);
    }

    public void drawLight(Graphics2D g2d) {

        double bigRingCenterX = gp.player.screenX + (double) gp.squareSize / 2;
        double bigRingCenterY = gp.player.screenY + (double) gp.squareSize / 2;
        double bigRingOuterRadius = gp.maxScreenWidth * 1.5;

        ring = createRingShape(bigRingCenterX, bigRingCenterY, bigRingOuterRadius, 100);
        g2d.setColor(new Color(0, 0, 0, gp.roomManager.currentRoom.darkness));
        g2d.fill(ring);

        //PLAYER HAS LIGHT
        if (true) {
            for (int i = 0; i < 20; i++) {
                ring = createRingShape(bigRingCenterX,
                        bigRingCenterY,
                        60 + i * 2,
                        60 + i);
                g2d.setColor(new Color(0, 0, 0, gp.roomManager.currentRoom.darkness / (i + 1)));
                g2d.fill(ring);
            }
        } else {
            g2d.fill(oval);
        }
    }

    public Shape createRingShape(double centerX, double centerY, double outerRadius, double innerRadius) {
        Ellipse2D outer = new Ellipse2D.Double(
                centerX - outerRadius,
                centerY - outerRadius,
                outerRadius * 2,
                outerRadius * 2);
        Ellipse2D inner = new Ellipse2D.Double(
                centerX - innerRadius,
                centerY - innerRadius,
                innerRadius * 2,
                innerRadius * 2);

        oval = new Area(inner);
        Area area = new Area(outer);
        area.subtract(new Area(inner));
        return area;
    }

    private void drawPauseScreen(Graphics2D g2d){

        g2d.setColor(new Color(0,0,0,100));
        g2d.fillRect(0, 0, gp.squareSize*(gp.maxCols - 2), gp.squareSize*(gp.maxRows - 2));

        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont.deriveFont(Font.PLAIN, 70));

        screenText = "Paused";

        int y = gp.maxScreenHeight/2;
        int x = getCenterX(screenText, g2d);

        g2d.drawString(screenText, x, y);
    }

    private int getCenterX (String str, Graphics2D g2d) {
        int length = (int) g2d.getFontMetrics().getStringBounds(str, g2d).getWidth();
        return gp.maxScreenWidth/2 - length/2;
    }
}
