package main;

import bullets.Bullet;
import characters.Character;

public class CollisionViewer {

    GamePanel gp;

    public CollisionViewer(GamePanel gp) {
        this.gp = gp;
    }

    public void checkMapCollision(Character ch) {

        //COORDINATES
        int characterLeftWorldX = ch.screenX + ch.areaOfCollision.x + gp.squareSize;
        int characterRightWorldX = ch.screenX + ch.areaOfCollision.x + ch.areaOfCollision.width + gp.squareSize;
        int characterTopWorldY = ch.screenY + ch.areaOfCollision.y + gp.squareSize;
        int characterBottomWorldY = ch.screenY + ch.areaOfCollision.y + ch.areaOfCollision.height + gp.squareSize;

        //SIDES
        int characterLeftSide = characterLeftWorldX / gp.squareSize;
        int characterRightSide = characterRightWorldX / gp.squareSize;
        int characterTopSide = characterTopWorldY/ gp.squareSize;
        int characterBottomSide = characterBottomWorldY/ gp.squareSize;

        int square1, square2;

        if (gp.keyR.up || ch.direction.equals("up")) {
            characterTopSide = (characterTopWorldY - ch.speed) / gp.squareSize;

            square1 = gp.roomManager.currentMatrix[characterLeftSide][characterTopSide];
            square2 = gp.roomManager.currentMatrix[characterRightSide][characterTopSide];

            if (gp.roomManager.currentRoom.squares[square1].collision || gp.roomManager.currentRoom.squares[square2].collision) {
                ch.collisionOnY = true;
            }
            characterTopSide = characterTopWorldY/ gp.squareSize;
        }
        if (gp.keyR.down || ch.direction.equals("down")) {
            characterBottomSide = (characterBottomWorldY + ch.speed) / gp.squareSize;

            square1 = gp.roomManager.currentMatrix[characterLeftSide][characterBottomSide];
            square2 = gp.roomManager.currentMatrix[characterRightSide][characterBottomSide];

            if (gp.roomManager.currentRoom.squares[square1].collision || gp.roomManager.currentRoom.squares[square2].collision) {
                ch.collisionOnY = true;
            }
            characterBottomSide = characterBottomWorldY/ gp.squareSize;
        }
        if (gp.keyR.left || ch.direction.equals("left")) {
            characterLeftSide = (characterLeftWorldX - ch.speed) / gp.squareSize;

            square1 = gp.roomManager.currentMatrix[characterLeftSide][characterTopSide];
            square2 = gp.roomManager.currentMatrix[characterLeftSide][characterBottomSide];

            if (gp.roomManager.currentRoom.squares[square1].collision || gp.roomManager.currentRoom.squares[square2].collision) {
                ch.collisionOnX = true;
            }
        }
        if (gp.keyR.right || ch.direction.equals("right")) {
            characterRightSide = (characterRightWorldX + ch.speed) / gp.squareSize;

            square1 = gp.roomManager.currentMatrix[characterRightSide][characterTopSide];
            square2 = gp.roomManager.currentMatrix[characterRightSide][characterBottomSide];

            if (gp.roomManager.currentRoom.squares[square1].collision || gp.roomManager.currentRoom.squares[square2].collision) {
                ch.collisionOnX = true;
            }
        }
    }

    public void checkMapCollision(Bullet bullet) {

        //COORDINATES
        int bulletLeftWorldX = (int) bullet.screenX + bullet.areaOfCollision.x + gp.squareSize;
        int bulletRightWorldX = (int) bullet.screenX + bullet.areaOfCollision.x + bullet.areaOfCollision.width + gp.squareSize;
        int bulletTopWorldY = (int) bullet.screenY + bullet.areaOfCollision.y + gp.squareSize;
        int bulletBottomWorldY = (int) bullet.screenY + bullet.areaOfCollision.y + bullet.areaOfCollision.height + gp.squareSize;

        //SIDES
        int bulletLeftSide = bulletLeftWorldX / gp.squareSize;
        int bulletRightSide = bulletRightWorldX / gp.squareSize;
        int bulletTopSide = bulletTopWorldY/ gp.squareSize;
        int bulletBottomSide = bulletBottomWorldY/ gp.squareSize;

        int square;

        switch (bullet.direction) {
            case "upLeft" -> {
                square = gp.roomManager.currentMatrix[bulletLeftSide][bulletTopSide];
                if (gp.roomManager.currentRoom.squares[square].collision) {
                    gp.bullets.remove(bullet);
                }
            }
            case "upRight" -> {
                square = gp.roomManager.currentMatrix[bulletRightSide][bulletTopSide];
                if (gp.roomManager.currentRoom.squares[square].collision) {
                    gp.bullets.remove(bullet);
                }
            }
            case "downLeft" -> {
                square = gp.roomManager.currentMatrix[bulletLeftSide][bulletBottomSide];
                if (gp.roomManager.currentRoom.squares[square].collision) {
                    gp.bullets.remove(bullet);
                }
            }
            case "downRight" -> {
                square = gp.roomManager.currentMatrix[bulletRightSide][bulletBottomSide];
                if (gp.roomManager.currentRoom.squares[square].collision) {
                    gp.bullets.remove(bullet);
                }
            }
        }
    }

    public int checkObjectCollision(Character ch, boolean player) {

        int index = -1;

        for (int i = 0; i < gp.roomManager.currentRoom.gameObjects.size(); i++) {
             ch.areaOfCollision.x += ch.screenX;
             ch.areaOfCollision.y += ch.screenY;

             gp.roomManager.currentRoom.gameObjects.get(i).areaOfCollision.x += gp.roomManager.currentRoom.gameObjects.get(i).screenX;
             gp.roomManager.currentRoom.gameObjects.get(i).areaOfCollision.y += gp.roomManager.currentRoom.gameObjects.get(i).screenY;

             if (gp.keyR.up || ch.direction.equals("up")) {
                 ch.areaOfCollision.y -= ch.speed;

                 if (ch.areaOfCollision.intersects(gp.roomManager.currentRoom.gameObjects.get(i).areaOfCollision)) {
                     if (gp.roomManager.currentRoom.gameObjects.get(i).collision) {
                         ch.collisionOnY = true;
                     }

                     if (player) index = i;
                 }

                 ch.areaOfCollision.y += ch.speed;
             }
             if (gp.keyR.down || ch.direction.equals("down")) {
                 ch.areaOfCollision.y += ch.speed;

                 if (ch.areaOfCollision.intersects(gp.roomManager.currentRoom.gameObjects.get(i).areaOfCollision)) {
                     if (gp.roomManager.currentRoom.gameObjects.get(i).collision) {
                         ch.collisionOnY = true;
                     }

                     if (player) index = i;
                 }

                 ch.areaOfCollision.y -= ch.speed;
             }
             if (gp.keyR.left || ch.direction.equals("left")) {
                 ch.areaOfCollision.x -= ch.speed;

                 if (ch.areaOfCollision.intersects(gp.roomManager.currentRoom.gameObjects.get(i).areaOfCollision)) {
                     if (gp.roomManager.currentRoom.gameObjects.get(i).collision) {
                         ch.collisionOnX = true;
                     }

                     if (player) index = i;
                 }
             }
             if (gp.keyR.right || ch.direction.equals("right")) {
                 ch.areaOfCollision.x += ch.speed;

                 if (ch.areaOfCollision.intersects(gp.roomManager.currentRoom.gameObjects.get(i).areaOfCollision)) {
                     if (gp.roomManager.currentRoom.gameObjects.get(i).collision) {
                         ch.collisionOnX = true;
                     }

                     if (player) index = i;
                 }
             }

             //RESTORING STANDARD VALUES
             ch.areaOfCollision.x = ch.defaultCollisionAreaX;
             ch.areaOfCollision.y = ch.defaultCollisionAreaY;

             gp.roomManager.currentRoom.gameObjects.get(i).areaOfCollision.x = gp.roomManager.currentRoom.gameObjects.get(i).defaultCollisionAreaX;
             gp.roomManager.currentRoom.gameObjects.get(i).areaOfCollision.y = gp.roomManager.currentRoom.gameObjects.get(i).defaultCollisionAreaY;
        }

        //-1 IF NOT PLAYER
        return index;
    }

    public void checkObjectCollision(Bullet bullet) {

        for (int i = 0; i < gp.roomManager.currentRoom.gameObjects.size(); i++) {
             bullet.areaOfCollision.x += bullet.screenX;
             bullet.areaOfCollision.y += bullet.screenY;

             gp.roomManager.currentRoom.gameObjects.get(i).areaOfCollision.x += gp.roomManager.currentRoom.gameObjects.get(i).screenX;
             gp.roomManager.currentRoom.gameObjects.get(i).areaOfCollision.y += gp.roomManager.currentRoom.gameObjects.get(i).screenY;

            if (bullet.areaOfCollision.intersects(gp.roomManager.currentRoom.gameObjects.get(i).areaOfCollision)) {
                if (gp.roomManager.currentRoom.gameObjects.get(i).collision) {
                    gp.bullets.remove(bullet);
                }
            }

            //RESTORING STANDARD VALUES
            bullet.areaOfCollision.x = bullet.defaultCollisionAreaX;
            bullet.areaOfCollision.y = bullet.defaultCollisionAreaY;

            gp.roomManager.currentRoom.gameObjects.get(i).areaOfCollision.x = gp.roomManager.currentRoom.gameObjects.get(i).defaultCollisionAreaX;
            gp.roomManager.currentRoom.gameObjects.get(i).areaOfCollision.y = gp.roomManager.currentRoom.gameObjects.get(i).defaultCollisionAreaY;
        }
    }
}
