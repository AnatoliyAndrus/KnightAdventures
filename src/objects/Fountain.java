package objects;

import main.GamePanel;

import java.awt.*;

public class Fountain extends StaticObject{

    public Fountain(GamePanel gp) {
        super(gp, "fountain");

        addImage("fountain_1");
        addImage("fountain_2");
        addImage("fountain_3");
        addImage("fountain_4");
        addImage("fountain_5");
        addImage("fountain_6");
        addImage("fountain_7");
        addImage("fountain_8");
        addImage("fountain_9");
        addImage("fountain_10");
        addImage("fountain_11");
        addImage("fountain_12");
        addImage("fountain_13");
        addImage("fountain_14");
        addImage("fountain_15");
        addImage("fountain_16");
        noAnimation = setImage("objects/fountain/fountain_1");
        animation = StaticObject.ANIMATION_CONTINUOUSLY;
        framesToChangeSprite = 5;

        this.collision = true;
        this.defaultCollisionAreaX = 0;
        this.defaultCollisionAreaY = 48;
        this.areaOfCollision = new Rectangle(defaultCollisionAreaX, defaultCollisionAreaY, 128, 50);
    }

    @Override
    public void draw(Graphics2D g2d) {
        if(imageNum == -1) g2d.drawImage(noAnimation, (int)screenX, (int)screenY, null);
        else g2d.drawImage(images.get(imageNum), (int)screenX, (int)screenY - 15, null);
    }
}
