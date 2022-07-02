/**
 * @author Anatolii Andrusenko, Andrii Sulimenko, Vladyslav Marchenko
 * @version 1.0
 *
 * class main which starts the whole program
 */
package main;

import javax.swing.*;

import static javax.swing.WindowConstants.*;

public class Main {

    public static JFrame gameFrame;
    public static GamePanel gamePanel;

    /**
     * main method
     * @param args arguments
     */
    public static void main(String[] args) {

        //SETTINGS
        gameFrame = new JFrame();
        gameFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gameFrame.setResizable(false);

        //PANEL
        gamePanel = new GamePanel();
        gameFrame.add(gamePanel);

        gameFrame.pack();
        gameFrame.setTitle("Game");
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        gamePanel.startGameThread();
    }

    /**
     * method which restarts game completely
     */
    public static void restartGame() {

        gameFrame.removeAll();
        gamePanel.gameThread.stop();
        gameFrame.dispose();

        //NEW SETTINGS
        gameFrame = new JFrame();
        gameFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gameFrame.setResizable(false);

        //NEW PANEL
        gamePanel = new GamePanel();
        gameFrame.add(gamePanel);

        gameFrame.pack();
        gameFrame.setTitle("Game");
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        gamePanel.startGameThread();
    }
}
