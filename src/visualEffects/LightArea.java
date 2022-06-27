/**
 * @author Anatolii Andrusenko, Andrii Sulimenko, Vladyslav Marchenko
 * @version 1.0
 *
 * class light area which represents light effects in rooms with poor light as a type of visual effect of the project
 */
package visualEffects;

import main.GamePanel;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class LightArea {

    /**
     * initial variables
     */
    GamePanel gp;
    BufferedImage darknessFilter;
    Graphics2D g2d;

    /**
     * additional variables with specified meaning
     */
    Shape innerCircle;
    Area innerArea;
    Area lightArea;

    Color[] colors = new Color[5];
    float[] fractions = new float[5];

    /**
     * public constructor of the light area
     * @param gp game panel
     * @param dungeon boolean, which clarify light area in specified room
     */
    public LightArea(GamePanel gp, boolean dungeon) {
        this.gp = gp;

        darknessFilter = new BufferedImage(gp.maxScreenWidth, gp.maxScreenHeight, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D) darknessFilter.getGraphics();

        lightArea = new Area(new Rectangle2D.Double(0,0,gp.maxScreenWidth,gp.maxScreenHeight));

        float f = dungeon ? 1f : 0.85f;

        colors[0] = new Color(0,0,0,0f);
        colors[1] = new Color(0,0,0,0.25f);
        colors[2] = new Color(0,0,0,0.5f);
        colors[3] = new Color(0,0,0,0.75f);
        colors[4] = new Color(0,0,0, f);

        fractions[0] = 0f;
        fractions[1] = 0.25f;
        fractions[2] = 0.5f;
        fractions[3] = 0.75f;
        fractions[4] = f;
    }

    /**
     * light circle effect
     * @param centerX coordinate of the X in center of the light area
     * @param centerY coordinate of the Y in center of the light area
     * @param innerRadius radius of light area circle
     */
    public void addLightCircle(int centerX, int centerY, int innerRadius) {

        double x = centerX - innerRadius;
        double y = centerY - innerRadius;

        innerCircle = new Ellipse2D.Double(x, y, innerRadius*2, innerRadius*2);

        innerArea = new Area(innerCircle);

        lightArea.subtract(innerArea);

        RadialGradientPaint radialGradientPaint = new RadialGradientPaint(centerX, centerY, innerRadius, fractions, colors);
        g2d.setPaint(radialGradientPaint);

        g2d.fill(innerArea);

    }

    /**
     * draw method
     * @param g2d graphics object of Java Swing API
     */
    public void draw(Graphics2D g2d) {

        this.g2d.fill(lightArea);
        this.g2d.dispose();

        g2d.drawImage(darknessFilter, 0, 0, null);
    }
}