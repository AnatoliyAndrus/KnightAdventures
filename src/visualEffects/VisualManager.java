/**
 * @author Anatolii Andrusenko, Andrii Sulimenko, Vladyslav Marchenko
 * @version 1.0
 *
 * class visual effects which represents main visual effects of the project
 */
package visualEffects;

import main.GamePanel;
import java.awt.*;

public class VisualManager {

    /**
     * initial variables
     */
    GamePanel gp;
    LightArea caveLightArea;
    LightArea dungeonLightArea;

    /**
     * visual manager constructor
     * @param gp game panel
     */
    public VisualManager(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * setup method which perform basic settings of the main visual effects
     */
    public void setup() {
        caveLightArea = new LightArea(gp, false);
        caveLightArea.addLightCircle(4 * gp.squareSize + 24, 4 * gp.squareSize, 190);
        caveLightArea.addLightCircle(4 * gp.squareSize + 24, 12 * gp.squareSize + 40, 190);
        caveLightArea.addLightCircle(20 * gp.squareSize + 24, 4 * gp.squareSize, 190);
        caveLightArea.addLightCircle(20 * gp.squareSize + 24, 12 * gp.squareSize + 40, 190);
        caveLightArea.addLightCircle(12 * gp.squareSize + 24, 12 * gp.squareSize + 40, 190);
        caveLightArea.addLightCircle(12 * gp.squareSize + 24, 4 * gp.squareSize, 190);

        dungeonLightArea = new LightArea(gp, true);
        dungeonLightArea.addLightCircle(11 * gp.squareSize + 5, 14 * gp.squareSize + 24, 67);
        dungeonLightArea.addLightCircle(14 * gp.squareSize - 5, 14 * gp.squareSize + 24, 67);
    }

    /**
     * draw method
     * @param g2d graphics object of Java Swing API
     */
    public void draw(Graphics2D g2d) {

        if(gp.roomManager.currentRoom.name.equals("cave")) {
            caveLightArea.draw(g2d);
        }
        if(gp.roomManager.currentRoom.name.equals("dungeon") && !gp.player.hasTorch) {
            dungeonLightArea.draw(g2d);
        }
    }
}