/**
 * @author Anatolii Andrusenko, Vladislav Marchenko, Andrii Sulimenko
 *
 * @version 1.0
 *
 * class UI to manage user interface
 */
package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class UI {

    //GAME PANEL
    GamePanel gp;
    //STANDARD FONT
    Font gameFont;
    //SCREEN TEXT
    String screenText;
    //IMAGES
    BufferedImage start, loading;
    BufferedImage[] hearts;

    //INITIALISE
    boolean init;
    //NUMBER OF OPTION (FOR MENU)
    public int optionNum = 1;
    //STATE OF MENU
    public boolean loadingScreen;
    public boolean difficultyChoice;
    public int loadingFrames = 0;

    //SHAPE FOR DARK ROOMS
    Shape ring;

    //SCREEN HINTS
    String hint;
    int hintTimer;

    //VARIABLES FOR SHOP
    public boolean shopIsOpened;
    public int armorPrice = 5;
    public int shotgunPrice = 12;
    public int burstPrice = 12;

    /**
     * constructor
     * @param gp game panel
     */
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

    /**
     * method to draw UI
     * @param g2d graphics 2d
     */
    public void draw(Graphics2D g2d) {
        g2d.setFont(gameFont);

        //TITLE STATE
        if (gp.currentState == gp.titleState) {
            if (loadingScreen) {
                drawLoadingScreen(g2d);
            } else if(difficultyChoice) {
                drawDifficultyChoiceScreen(g2d);
            } else {
                drawStartScreen(g2d);
            }
        }
        //GAME STATE
        if (gp.currentState == gp.gameState) {
            drawDarkScreen(g2d);
            drawPlayerStats(g2d);
            drawScreenHint(g2d);
            if(shopIsOpened) drawShopMenu(g2d);
            if(gp.roomManager.currentRoom.name.equals("finalMap")) drawBossHealthBar(g2d);
        }
        //PAUSE STATE
        if (gp.currentState == gp.pauseState) {
            drawDarkScreen(g2d);
            drawPlayerStats(g2d);
            drawPauseScreen(g2d);
        }
        //GAME OVER STATE
        if (gp.currentState == gp.gameOverState) {
            drawDarkScreen(g2d);
            drawPlayerStats(g2d);
            if(gp.player.HP <= 0) drawGameOverScreen(g2d);
            else drawWinScreen(g2d);
        }
    }

    /**
     * draws players HP
     * @param g2d graphics
     */
    public void drawPlayerStats(Graphics2D g2d) {

        if(!init){
            hearts = new BufferedImage[gp.player.maxHP/3];
            Arrays.fill(hearts, gp.player.heart1);
            init = true;
        }
        int currentHeart = (gp.player.HP+2)/3;
        if(gp.player.HP%3==0&&gp.player.HP!=0){
            hearts[currentHeart-1]=gp.player.heart1;
        }
        if(gp.player.HP%3==2){
            hearts[currentHeart-1]=gp.player.heart2;
        }
        if(gp.player.HP%3==1){
            hearts[currentHeart-1]=gp.player.heart3;
        }

        for(int i = 1; i <= gp.player.maxHP/3;i++){
            if(i<currentHeart){
                hearts[i-1]=gp.player.heart1;
            }
            if(i>currentHeart) {
                hearts[i - 1] = gp.player.heart4;
            }
        }

        for(int i = 1; i <=gp.player.maxHP/3; i++) {

            g2d.drawImage(hearts[i-1], i* gp.squareSize, 0, null);
        }

        for(int i = 1; i <= gp.player.armor; i++) {
            g2d.drawImage(gp.player.shield, i * gp.squareSize, gp.squareSize - 5, null);
        }

        //COINS
        BufferedImage coinImage;
        BufferedImage keyImage;
        try {
            coinImage = ImageIO.read(new File("resources/images/objects/coin/coin_1.png"));
            keyImage = ImageIO.read(new File("resources/images/objects/chest/key_1.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        g2d.drawImage(coinImage, gp.maxScreenWidth - gp.squareSize * 3 + 24, -gp.squareSize, null);
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(40f));
        screenText = "x" + gp.player.coinsAmount;
        g2d.drawString(screenText, gp.maxScreenWidth - gp.squareSize * 2 + 24, gp.squareSize - 5);

        //KEYS
        if(gp.player.hasBossKey) {
            g2d.drawImage(keyImage, gp.maxScreenWidth - gp.squareSize * 3 + 24, gp.squareSize, null);
            g2d.setColor(Color.WHITE);
            g2d.setFont(g2d.getFont().deriveFont(40f));
            screenText = "x1";
            g2d.drawString(screenText, gp.maxScreenWidth - gp.squareSize * 2 + 24, gp.squareSize * 2 - 5);
        }
    }

    /**
     * method to draw health bar
     * @param g2d graphics 2d
     */
    private void drawBossHealthBar(Graphics2D g2d) {

        if(gp.roomManager.currentRoom.enemies.size() > 0) {
            BufferedImage bossBarImage;
            try {
                bossBarImage = ImageIO.read(new File("resources/images/boss/boss_bar.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int x = gp.squareSize * 9;
            int y = gp.maxScreenHeight - 72;

            g2d.setColor(Color.RED);

            int healthBarWidth = (int)(((double)gp.roomManager.currentRoom.enemies.get(0).HP / gp.roomManager.currentRoom.enemies.get(0).maxHP) * 288);

            g2d.fillRect(x + 24, y + 14, healthBarWidth, 20);

            g2d.drawImage(bossBarImage, x, y, null);
        }
    }

    /**
     * method to draw loading screen
     * @param g2d graphics 2d
     */
    private void drawLoadingScreen(Graphics2D g2d) {

        loadingFrames++;
        g2d.drawImage(loading, 0, 0, gp.maxScreenWidth, gp.maxScreenHeight, null);

        //TITLE
        screenText = "Knight adventures";
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 100));

        int y = gp.squareSize * 3;
        int x = getCenterX(screenText, g2d);

        g2d.setColor(Color.BLACK);
        g2d.drawString(screenText, x + 5, y + 5);

        g2d.setColor(Color.WHITE);
        g2d.drawString(screenText, x, y);

        BufferedImage tmp = null;

        if(loadingFrames % (gp.FPS * 2) < gp.FPS / 2) {
            screenText = "Loading";
            tmp = gp.player.down1;
        } else if(loadingFrames % (gp.FPS * 2) >= gp.FPS / 2 && loadingFrames % (gp.FPS * 2) < gp.FPS) {
            screenText = "Loading.";
            tmp = gp.player.right1;
        } else if(loadingFrames % (gp.FPS * 2) >= gp.FPS && loadingFrames % (gp.FPS * 2) < gp.FPS * 1.5) {
            screenText = "Loading..";
            tmp = gp.player.up1;
        } else if(loadingFrames % (gp.FPS * 2) >= gp.FPS * 1.5) {
            screenText = "Loading...";
            tmp = gp.player.left1;
        }

        g2d.drawImage(tmp,
                gp.squareSize/2,
                (int) (gp.maxScreenHeight - gp.squareSize * 1.5),
                null);

        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 48));

        x = (int) (gp.squareSize * 1.5);
        y = (int) (gp.maxScreenHeight - gp.squareSize * 0.5);

        g2d.setColor(Color.WHITE);
        g2d.drawString(screenText, x, y);

        if (loadingFrames == gp.FPS * 5) {
            gp.setupGame();
            gp.currentState = gp.gameState;
            loadingScreen = false;
            loadingFrames = 0;
        }
    }

    /**
     * method to draw start screen
     * @param g2d graphics 2d
     */
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

        //EXIT
        screenText = "Exit";
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


//        //LOAD GAME
//        screenText = "Load game";
//        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));
//
//        y = gp.squareSize * 11;
//        x = getCenterX(screenText, g2d);
//
//        if (optionNum == 3) {
//            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));
//
//            g2d.setStroke(new BasicStroke(5));
//            g2d.drawRoundRect(x - 24,
//                    y - (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight(),
//                    (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth() + 48,
//                    (int) (g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight() * 1.25),
//                    25, 25);
//        }
//
//        g2d.drawString(screenText, x, y);
    }

    /**
     * method to draw difficulty choises screen
     * @param g2d graphics 2d
     */
    private void drawDifficultyChoiceScreen(Graphics2D g2d) {
        //TITLE
        screenText = "Select difficulty:";
        g2d.drawImage(start, 0, 0, gp.maxScreenWidth, gp.maxScreenHeight, null);

        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 100));

        int y = gp.squareSize * 3;
        int x = getCenterX(screenText, g2d);

        g2d.setColor(Color.BLACK);
        g2d.drawString(screenText, x + 5, y + 5);

        g2d.setColor(Color.WHITE);
        g2d.drawString(screenText, x, y);

        //Peaceful
        screenText = "Peaceful";
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

        y = gp.squareSize * 5;
        x = getCenterX(screenText, g2d);

        if (optionNum == 1) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setStroke(new BasicStroke(5));
            g2d.drawRoundRect(x - 24,
                    y - (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight(),
                    (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth() + 48,
                    (int) (g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight() * 1.25),
                    25, 25);

            long minutes = gp.records[optionNum - 1] / 60;
            long seconds = gp.records[optionNum - 1] % 60;

            String record = "(Record - " + (minutes < 10 ? ("0" + minutes) : minutes) + ":" + (seconds < 10 ? ("0" + seconds) : seconds) + ")";
            if(minutes + seconds == 0) record = "(No record)";
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));
            g2d.drawString(record, gp.maxScreenWidth/2 + 200, y);
        }

        g2d.drawString(screenText, x, y);

        //Easy
        screenText = "Easy";
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

        y = gp.squareSize * 7;
        x = getCenterX(screenText, g2d);

        if (optionNum == 2) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setStroke(new BasicStroke(5));
            g2d.drawRoundRect(x - 24,
                    y - (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight(),
                    (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth() + 48,
                    (int) (g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight() * 1.25),
                    25, 25);

            long minutes = gp.records[optionNum - 1] / 60;
            long seconds = gp.records[optionNum - 1] % 60;

            String record = "(Record - " + (minutes < 10 ? ("0" + minutes) : minutes) + ":" + (seconds < 10 ? ("0" + seconds) : seconds) + ")";
            if(minutes + seconds == 0) record = "(No record)";
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));
            g2d.drawString(record, gp.maxScreenWidth/2 + 200, y);
        }

        g2d.drawString(screenText, x, y);

        //Medium
        screenText = "Medium";
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

        y = gp.squareSize * 9;
        x = getCenterX(screenText, g2d);

        if (optionNum == 3) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setStroke(new BasicStroke(5));
            g2d.drawRoundRect(x - 24,
                    y - (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight(),
                    (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth() + 48,
                    (int) (g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight() * 1.25),
                    25, 25);

            long minutes = gp.records[optionNum - 1] / 60;
            long seconds = gp.records[optionNum - 1] % 60;

            String record = "(Record - " + (minutes < 10 ? ("0" + minutes) : minutes) + ":" + (seconds < 10 ? ("0" + seconds) : seconds) + ")";
            if(minutes + seconds == 0) record = "(No record)";
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));
            g2d.drawString(record, gp.maxScreenWidth/2 + 200, y);
        }

        g2d.drawString(screenText, x, y);

        //Hard
        screenText = "Hard";
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

        y = gp.squareSize * 11;
        x = getCenterX(screenText, g2d);

        if (optionNum == 4) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setStroke(new BasicStroke(5));
            g2d.drawRoundRect(x - 24,
                    y - (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight(),
                    (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth() + 48,
                    (int) (g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight() * 1.25),
                    25, 25);

            long minutes = gp.records[optionNum - 1] / 60;
            long seconds = gp.records[optionNum - 1] % 60;

            String record = "(Record - " + (minutes < 10 ? ("0" + minutes) : minutes) + ":" + (seconds < 10 ? ("0" + seconds) : seconds) + ")";
            if(minutes + seconds == 0) record = "(No record)";
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));
            g2d.drawString(record, gp.maxScreenWidth/2 + 200, y);
        }

        g2d.drawString(screenText, x, y);

        //No mercy
        screenText = "No mercy";
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

        y = gp.squareSize * 13;
        x = getCenterX(screenText, g2d);

        if (optionNum == 5) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setStroke(new BasicStroke(5));
            g2d.drawRoundRect(x - 24,
                    y - (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight(),
                    (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth() + 48,
                    (int) (g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight() * 1.25),
                    25, 25);

            long minutes = gp.records[optionNum - 1] / 60;
            long seconds = gp.records[optionNum - 1] % 60;

            String record = "(Record - " + (minutes < 10 ? ("0" + minutes) : minutes) + ":" + (seconds < 10 ? ("0" + seconds) : seconds) + ")";
            if(minutes + seconds == 0) record = "(No record)";
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));
            g2d.drawString(record, gp.maxScreenWidth/2 + 200, y);
        }

        g2d.drawString(screenText, x, y);

        //Back
        screenText = "Back";
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

        y = gp.squareSize * 16;
        x = getCenterX(screenText, g2d);

        if (optionNum == 6) {
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

    /**
     * method to draw shadow in dark rooms
     * @param g2d graphics 2d
     */
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
        if (gp.roomManager.currentRoom.name.equals("dungeon") && gp.player.hasTorch) drawLight(g2d);
    }

    /**
     * method to draw light in dark rooms
     * @param g2d graphics 2d
     */
    public void drawLight(Graphics2D g2d) {

        double centerX;
        double centerY;
        double innerRadius = 114;

        if(gp.player.hasTorch) {
            centerX = gp.player.screenX + (double) gp.squareSize / 2;
            centerY = gp.player.screenY + (double) gp.squareSize / 2;
        } else {
            centerX = gp.squareSize * 14 + gp.squareSize/2.0;
            centerY = gp.squareSize * 15 + gp.squareSize/2.0;
        }

        Area rect = new Area(new Rectangle2D.Double(0,0,gp.maxScreenWidth, gp.maxScreenHeight));
        Ellipse2D inner = new Ellipse2D.Double(
                centerX - innerRadius,
                centerY - innerRadius,
                innerRadius * 2,
                innerRadius * 2);
        Area oval = new Area(inner);
        rect.subtract(oval);
        g2d.setColor(new Color(0, 0, 0, 1f));
        g2d.fill(rect);

//        //WITH BLUR
//        for(int i = 1; i < innerRadius; i += 1) {
//            ring = createRingShape(centerX, centerY, i + 1, i);
//            g2d.setColor(new Color(0, 0, 0, (int) ((i / (innerRadius - 1)) * 240)));
//            RenderingHints rh = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
//            g2d.setRenderingHints(rh);
//            g2d.fill(ring);
//        }

        //WITH CIRCLE
        for(int i = 1; i < innerRadius - 40; i += 1) {
            ring = createRingShape(centerX, centerY, innerRadius, i + 30);
            g2d.setColor(new Color(0, 0, 0, 8));
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            g2d.setRenderingHints(rh);
            g2d.fill(ring);
        }
    }

    /**
     * method to create ring
     * @param centerX center x pos
     * @param centerY center y pos
     * @param outerRadius outer radius
     * @param innerRadius inner radius
     * @return ring shape
     */
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

        Area oval = new Area(inner);
        Area area = new Area(outer);
        area.subtract(oval);
        return area;
    }

    /**
     * method to draw shop menu
     * @param g2d graphics 2d
     */
    public void drawShopMenu(Graphics2D g2d) {
        //BOUNDARIES
        int x = gp.maxScreenWidth/2 - gp.squareSize * 5;
        int y = gp.squareSize * 2;

        g2d.setColor(new Color(0,0,0, 200));
        g2d.fillRoundRect(x, y,
                gp.squareSize * 10,
                gp.squareSize * 4,
                25, 25);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x, y,
                gp.squareSize * 10,
                gp.squareSize * 4,
                25, 25);

        g2d.setColor(new Color(0,0,0, 200));
        g2d.fillRoundRect(x - 100, y - 50,
                100,
                50,
                25, 25);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x - 100, y - 50,
                100,
                50,
                25, 25);

        BufferedImage cross;
        try {
            cross = ImageIO.read(new File("resources/images/objects/shop/icon_cross.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        g2d.drawImage(cross, x - 90, y - 40, 32, 32, null);

        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(30f));
        screenText = "Esc";
        g2d.drawString(screenText, x - 53, y - 60 + gp.squareSize);

        //ITEMS
        BufferedImage coinImage;
        BufferedImage tickImage;
        BufferedImage shotgunImage;
        BufferedImage burstImage;
        try {
            coinImage = ImageIO.read(new File("resources/images/objects/coin/coin_1.png"));
            tickImage = ImageIO.read(new File("resources/images/objects/shop/shopIcon_1.png"));
            shotgunImage = ImageIO.read(new File("resources/images/objects/shop/iconForShop_2.png"));
            burstImage = ImageIO.read(new File("resources/images/objects/shop/iconForShop_1.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //ARMOR
        g2d.drawImage(gp.player.shield, x + 15, y + 15, null);
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));
        screenText = "Armor - " + armorPrice;
        g2d.drawImage(coinImage, x + gp.squareSize * 6 - 15, y + 12 - gp.squareSize, null);
        if(gp.player.armor == 5) {
            g2d.drawImage(tickImage, x + gp.squareSize * 9 - 20, y + 15, null);
        }
        if(optionNum == 1) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(x + 10, y + 10,
                    gp.squareSize * 10 - 20,
                    gp.squareSize + 10,
                    25, 25);
        }
        g2d.drawString(screenText, x + 15 + gp.squareSize, y + 5 + gp.squareSize);

        //SHOTGUN UPGRADE
        g2d.drawImage(shotgunImage, x + 15, y + 25 + gp.squareSize, null);
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));
        screenText = "Shotgun - " + shotgunPrice;
        g2d.drawImage(coinImage, x + gp.squareSize * 7 + 10, y + 22, null);
        if(gp.player.shotgunFire) {
            g2d.drawImage(tickImage, x + gp.squareSize * 9 - 20, y + 25 + gp.squareSize, null);
        }
        if(optionNum == 2) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(x + 10, y + 20 + gp.squareSize,
                    gp.squareSize * 10 - 20,
                    gp.squareSize + 10,
                    25, 25);
        }
        g2d.drawString(screenText, x + 15 + gp.squareSize, y + 15 + gp.squareSize * 2);

        //BURST UPGRADE
        g2d.drawImage(burstImage, x + 15, y + 35 + gp.squareSize * 2, null);
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));
        screenText = "Burst - " + burstPrice;
        g2d.drawImage(coinImage, x + gp.squareSize * 6 + 10, y + 32 + gp.squareSize, null);
        if(gp.player.burstFire) {
            g2d.drawImage(tickImage, x + gp.squareSize * 9 - 20, y + 35 + gp.squareSize * 2, null);
        }
        if(optionNum == 3) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(x + 10, y + 30 + gp.squareSize * 2,
                    gp.squareSize * 10 - 20,
                    gp.squareSize + 10,
                    25, 25);
        }
        g2d.drawString(screenText, x + 15 + gp.squareSize, y + 25 + gp.squareSize * 3);
    }

    /**
     * method to make screen hint
     * @param hint text of hint
     * @param hintTimer timer of hint
     */
    public void makeScreenHint(String hint, int hintTimer){
        this.hint = hint;
        this.hintTimer = hintTimer;
    }

    /**
     * method to draw screen hint
     * @param g2d
     */
    private void drawScreenHint(Graphics2D g2d){
        if(hintTimer > 0){
            hintTimer--;
            ArrayList<String> hints = new ArrayList<>(Arrays.asList(hint.split("#")));
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 30));

            String max = "";
            for (int i = 0; i < hints.size(); i++) {
                if (hints.get(i).length() > max.length()) max = hints.get(i);
            }

            int x = getCenterX(max, g2d);
            int y = gp.squareSize * 2;

            g2d.setColor(new Color(0,0,0, 150));
            g2d.fillRoundRect(x - 36,
                    y - (int) g2d.getFontMetrics().getStringBounds(max, g2d).getHeight() - 20,
                    (int) g2d.getFontMetrics().getStringBounds(max, g2d).getWidth() + 72,
                    (int) (g2d.getFontMetrics().getStringBounds(max, g2d).getHeight() * hints.size()) + 48,
                    25, 25);

            g2d.setColor(new Color(255,255,255, 100));
            g2d.setStroke(new BasicStroke(5));
            g2d.drawRoundRect(x - 36,
                    y - (int) g2d.getFontMetrics().getStringBounds(max, g2d).getHeight() - 20,
                    (int) g2d.getFontMetrics().getStringBounds(max, g2d).getWidth() + 72,
                    (int) (g2d.getFontMetrics().getStringBounds(max, g2d).getHeight() * hints.size()) + 48,
                    25, 25);

            g2d.setColor(Color.WHITE);
            for (int i = 0; i < hints.size(); i++) {
                x = getCenterX(hints.get(i), g2d);
                y = 2 * gp.squareSize + (int) (g2d.getFontMetrics().getStringBounds(hints.get(i), g2d).getHeight()) * i;
                g2d.drawString(hints.get(i), x, y);
            }
        }
    }

    /**
     * method to draw pause screen
     * @param g2d graphics 2d
     */
    private void drawPauseScreen(Graphics2D g2d){

        //DARK SCREEN
        g2d.setColor(new Color(0,0,0,100));
        g2d.fillRect(0, 0, gp.maxScreenWidth, gp.maxScreenHeight);

        //GAME OVER
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont.deriveFont(Font.PLAIN, 70));

        screenText = "Paused";

        int y = gp.squareSize * 5;
        int x = getCenterX(screenText, g2d);

        g2d.drawString(screenText, x, y);

        //Retry
        if(gp.roomManager.currentRoom.phase.equals("in progress") && !gp.player.scriptsAreActive) {
            screenText = "Retry";
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

            y = gp.squareSize * 9;
            x = getCenterX(screenText, g2d);

            if (optionNum == 1) {
                g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(x - 24,
                        y - (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight(),
                        (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth() + 48,
                        (int) (g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight() * 1.25),
                        25, 25);
            }

            g2d.drawString(screenText, x, y);
        }

        //Quit
        screenText = "Quit";
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

        y = gp.squareSize * 11;
        x = getCenterX(screenText, g2d);

        if (optionNum == 2) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(x - 24,
                    y - (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight(),
                    (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth() + 48,
                    (int) (g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight() * 1.25),
                    25, 25);
        }

        g2d.drawString(screenText, x, y);
    }

    /**
     * method to draw game over screen
     * @param g2d graphics 2d
     */
    private void drawGameOverScreen(Graphics2D g2d) {
        //DARK SCREEN
        g2d.setColor(new Color(0,0,0,200));
        g2d.fillRect(0, 0, gp.squareSize*(gp.maxCols - 2), gp.squareSize*(gp.maxRows - 2));

        //GAME OVER
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont.deriveFont(Font.PLAIN, 70));

        screenText = "Game Over";

        int y = gp.squareSize * 5;
        int x = getCenterX(screenText, g2d);

        g2d.drawString(screenText, x, y);

        //Retry
        screenText = "Retry";
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

        y = gp.squareSize * 9;
        x = getCenterX(screenText, g2d);

        if (optionNum == 1) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(x - 24,
                    y - (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight(),
                    (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth() + 48,
                    (int) (g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight() * 1.25),
                    25, 25);
        }

        g2d.drawString(screenText, x, y);

        //Quit
        screenText = "Quit";
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

        y = gp.squareSize * 11;
        x = getCenterX(screenText, g2d);

        if (optionNum == 2) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(x - 24,
                    y - (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight(),
                    (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth() + 48,
                    (int) (g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight() * 1.25),
                    25, 25);
        }

        g2d.drawString(screenText, x, y);
    }

    /**
     * method to draw win screen
     * @param g2d graphics 2d
     */
    private void drawWinScreen(Graphics2D g2d) {
        //DARK SCREEN
        g2d.setColor(new Color(0,0,0,200));
        g2d.fillRect(0, 0, gp.squareSize*(gp.maxCols - 2), gp.squareSize*(gp.maxRows - 2));

        //Congratulations!
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont.deriveFont(Font.PLAIN, 70));

        screenText = "Congratulations!";

        int y = gp.squareSize * 5;
        int x = getCenterX(screenText, g2d);

        g2d.drawString(screenText, x, y);

        //RECORD BLOCK
        long currentMinutes = gp.timeInSeconds / 60;
        long currentSeconds = gp.timeInSeconds % 60;

        screenText = "Time - " + (currentMinutes < 10 ? ("0" + currentMinutes) : currentMinutes) + ":" + (currentSeconds < 10 ? ("0" + currentSeconds) : currentSeconds);
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

        y = gp.squareSize * 7;
        x = getCenterX(screenText, g2d);

        g2d.drawString(screenText, x, y);

        if(gp.timeInSeconds < gp.records[gp.difficulty - 1] || gp.records[gp.difficulty - 1] == 0) {
            screenText = "(New record)";
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

            y = gp.squareSize * 8;
            x = getCenterX(screenText, g2d);

            g2d.drawString(screenText, x, y);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("resources/files/records/record_" + gp.difficulty + ".txt"))){
                bw.write("" + gp.timeInSeconds);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //Continue exploring
        screenText = "Continue exploring";
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

        y = gp.squareSize * 14;
        x = getCenterX(screenText, g2d);

        if (optionNum == 1) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(x - 24,
                    y - (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight(),
                    (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth() + 48,
                    (int) (g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight() * 1.25),
                    25, 25);
        }

        g2d.drawString(screenText, x, y);

        //Quit
        screenText = "Quit";
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40));

        y = gp.squareSize * 16;
        x = getCenterX(screenText, g2d);

        if (optionNum == 2) {
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 40));

            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(x - 24,
                    y - (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight(),
                    (int) g2d.getFontMetrics().getStringBounds(screenText, g2d).getWidth() + 48,
                    (int) (g2d.getFontMetrics().getStringBounds(screenText, g2d).getHeight() * 1.25),
                    25, 25);
        }

        g2d.drawString(screenText, x, y);
    }

    /**
     * method to get center x
     * @param str
     * @param g2d
     * @return
     */
    private int getCenterX (String str, Graphics2D g2d) {
        int length = (int) g2d.getFontMetrics().getStringBounds(str, g2d).getWidth();
        return gp.maxScreenWidth/2 - length/2;
    }
}
