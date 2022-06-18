/**
 * @author Anatolii Andrusenko, Andrii Sulimenko, Vladyslav Marchenko
 */
package main;

import javax.swing.*;
import java.util.ArrayList;

import static javax.swing.WindowConstants.*;

public class Main {
    //qqqqqq
    public static void main(String[] args) {

        //SETTINGS
        JFrame gameFrame = new JFrame();
        gameFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gameFrame.setResizable(false);

        //PANEL
        GamePanel gamePanel = new GamePanel();
        gameFrame.add(gamePanel);

        gameFrame.pack();
        gameFrame.setTitle("Game");
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}
