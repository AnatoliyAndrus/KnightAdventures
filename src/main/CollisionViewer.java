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

        for (int i = 0; i < gp.roomManager.currentRoom.staticObjects.size(); i++) {
             ch.areaOfCollision.x += ch.screenX;
             ch.areaOfCollision.y += ch.screenY;

             gp.roomManager.currentRoom.staticObjects.get(i).areaOfCollision.x += gp.roomManager.currentRoom.staticObjects.get(i).screenX;
             gp.roomManager.currentRoom.staticObjects.get(i).areaOfCollision.y += gp.roomManager.currentRoom.staticObjects.get(i).screenY;

             if (gp.keyR.up || ch.direction.equals("up")) {
                 ch.areaOfCollision.y -= ch.speed;

                 if (ch.areaOfCollision.intersects(gp.roomManager.currentRoom.staticObjects.get(i).areaOfCollision)) {
                     if (gp.roomManager.currentRoom.staticObjects.get(i).collision) {
                         ch.collisionOnY = true;
                     }

                     if (player) index = i;
                 }

                 ch.areaOfCollision.y += ch.speed;
             }
             if (gp.keyR.down || ch.direction.equals("down")) {
                 ch.areaOfCollision.y += ch.speed;

                 if (ch.areaOfCollision.intersects(gp.roomManager.currentRoom.staticObjects.get(i).areaOfCollision)) {
                     if (gp.roomManager.currentRoom.staticObjects.get(i).collision) {
                         ch.collisionOnY = true;
                     }

                     if (player) index = i;
                 }

                 ch.areaOfCollision.y -= ch.speed;
             }
             if (gp.keyR.left || ch.direction.equals("left")) {
                 ch.areaOfCollision.x -= ch.speed;

                 if (ch.areaOfCollision.intersects(gp.roomManager.currentRoom.staticObjects.get(i).areaOfCollision)) {
                     if (gp.roomManager.currentRoom.staticObjects.get(i).collision) {
                         ch.collisionOnX = true;
                     }

                     if (player) index = i;
                 }
             }
             if (gp.keyR.right || ch.direction.equals("right")) {
                 ch.areaOfCollision.x += ch.speed;

                 if (ch.areaOfCollision.intersects(gp.roomManager.currentRoom.staticObjects.get(i).areaOfCollision)) {
                     if (gp.roomManager.currentRoom.staticObjects.get(i).collision) {
                         ch.collisionOnX = true;
                     }

                     if (player) index = i;
                 }
             }

             //RESTORING STANDARD VALUES
             ch.areaOfCollision.x = ch.defaultCollisionAreaX;
             ch.areaOfCollision.y = ch.defaultCollisionAreaY;

             gp.roomManager.currentRoom.staticObjects.get(i).areaOfCollision.x = gp.roomManager.currentRoom.staticObjects.get(i).defaultCollisionAreaX;
             gp.roomManager.currentRoom.staticObjects.get(i).areaOfCollision.y = gp.roomManager.currentRoom.staticObjects.get(i).defaultCollisionAreaY;
        }

        //-1 IF NOT PLAYER
        return index;
    }

    public void checkObjectCollision(Bullet bullet) {

        for (int i = 0; i < gp.roomManager.currentRoom.staticObjects.size(); i++) {
             bullet.areaOfCollision.x += bullet.screenX;
             bullet.areaOfCollision.y += bullet.screenY;

             gp.roomManager.currentRoom.staticObjects.get(i).areaOfCollision.x += gp.roomManager.currentRoom.staticObjects.get(i).screenX;
             gp.roomManager.currentRoom.staticObjects.get(i).areaOfCollision.y += gp.roomManager.currentRoom.staticObjects.get(i).screenY;

            if (bullet.areaOfCollision.intersects(gp.roomManager.currentRoom.staticObjects.get(i).areaOfCollision)) {
                if (gp.roomManager.currentRoom.staticObjects.get(i).collision) {
                    gp.bullets.remove(bullet);
                }
            }

            //RESTORING STANDARD VALUES
            bullet.areaOfCollision.x = bullet.defaultCollisionAreaX;
            bullet.areaOfCollision.y = bullet.defaultCollisionAreaY;

            gp.roomManager.currentRoom.staticObjects.get(i).areaOfCollision.x = gp.roomManager.currentRoom.staticObjects.get(i).defaultCollisionAreaX;
            gp.roomManager.currentRoom.staticObjects.get(i).areaOfCollision.y = gp.roomManager.currentRoom.staticObjects.get(i).defaultCollisionAreaY;
        }
    }

    public void checkCharacterCollision(Character ch, boolean player) {

        for (int i = 0; i < gp.roomManager.currentRoom.enemies.size(); i++) {
            if(!gp.roomManager.currentRoom.enemies.get(i).isDead) {

                ch.areaOfCollision.x += ch.screenX;
                ch.areaOfCollision.y += ch.screenY;

                if (!ch.equals(gp.roomManager.currentRoom.enemies.get(i))) {
                    gp.roomManager.currentRoom.enemies.get(i).areaOfCollision.x += gp.roomManager.currentRoom.enemies.get(i).screenX;
                    gp.roomManager.currentRoom.enemies.get(i).areaOfCollision.y += gp.roomManager.currentRoom.enemies.get(i).screenY;
                }

                if (!player) {
                    gp.player.areaOfCollision.x += gp.player.screenX;
                    gp.player.areaOfCollision.y += gp.player.screenY;
                }

                //MAIN BLOCK
                if (gp.keyR.up || ch.direction.equals("up")) {
                    ch.areaOfCollision.y -= ch.speed;

                    if (ch.areaOfCollision.intersects(gp.roomManager.currentRoom.enemies.get(i).areaOfCollision)) {
                        if (!ch.equals(gp.roomManager.currentRoom.enemies.get(i))) {
                            ch.collisionOnY = true;
                        }

                        if (player) {
                            if (!gp.player.isInvincible) {
                                gp.player.receiveDamage();
                                gp.player.isInvincible = true;
                            }
                        }
                    }

                    if (!player) {
                        if (ch.areaOfCollision.intersects(gp.player.areaOfCollision)) {
                            ch.collisionOnY = true;
                            ;
                            if (!gp.player.isInvincible) {
                                gp.player.receiveDamage();
                                gp.player.isInvincible = true;
                            }
                        }
                    }

                    ch.areaOfCollision.y += ch.speed;
                }
                if (gp.keyR.down || ch.direction.equals("down")) {
                    ch.areaOfCollision.y += ch.speed;

                    if (ch.areaOfCollision.intersects(gp.roomManager.currentRoom.enemies.get(i).areaOfCollision)) {
                        if (!ch.equals(gp.roomManager.currentRoom.enemies.get(i))) {
                            ch.collisionOnY = true;
                        }

                        if (player) {
                            if (!gp.player.isInvincible) {
                                gp.player.receiveDamage();
                                gp.player.isInvincible = true;
                            }
                        }
                    }

                    if (!player) {
                        if (ch.areaOfCollision.intersects(gp.player.areaOfCollision)) {
                            ch.collisionOnY = true;
                            if (!gp.player.isInvincible) {
                                gp.player.receiveDamage();
                                gp.player.isInvincible = true;
                            }
                        }
                    }

                    ch.areaOfCollision.y -= ch.speed;
                }
                if (gp.keyR.left || ch.direction.equals("left")) {
                    ch.areaOfCollision.x -= ch.speed;

                    if (ch.areaOfCollision.intersects(gp.roomManager.currentRoom.enemies.get(i).areaOfCollision)) {
                        if (!ch.equals(gp.roomManager.currentRoom.enemies.get(i))) {
                            ch.collisionOnX = true;
                        }

                        if (player) {
                            if (!gp.player.isInvincible) {
                                gp.player.receiveDamage();
                                gp.player.isInvincible = true;
                            }
                        }
                    }

                    if (!player) {
                        if (ch.areaOfCollision.intersects(gp.player.areaOfCollision)) {
                            ch.collisionOnX = true;
                            if (!gp.player.isInvincible) {
                                gp.player.receiveDamage();
                                gp.player.isInvincible = true;
                            }
                        }
                    }
                }
                if (gp.keyR.right || ch.direction.equals("right")) {
                    ch.areaOfCollision.x += ch.speed;

                    if (ch.areaOfCollision.intersects(gp.roomManager.currentRoom.enemies.get(i).areaOfCollision)) {
                        if (!ch.equals(gp.roomManager.currentRoom.enemies.get(i))) {
                            ch.collisionOnX = true;
                        }

                        if (player) {
                            if (!gp.player.isInvincible) {
                                gp.player.receiveDamage();
                                gp.player.isInvincible = true;
                            }
                        }
                    }

                    if (!player) {
                        if (ch.areaOfCollision.intersects(gp.player.areaOfCollision)) {
                            ch.collisionOnX = true;
                            if (!gp.player.isInvincible) {
                                gp.player.receiveDamage();
                                gp.player.isInvincible = true;
                            }
                        }
                    }
                }

                //RESTORING STANDARD VALUES
                ch.areaOfCollision.x = ch.defaultCollisionAreaX;
                ch.areaOfCollision.y = ch.defaultCollisionAreaY;

                if (!ch.equals(gp.roomManager.currentRoom.enemies.get(i))) {
                    gp.roomManager.currentRoom.enemies.get(i).areaOfCollision.x = gp.roomManager.currentRoom.enemies.get(i).defaultCollisionAreaX;
                    gp.roomManager.currentRoom.enemies.get(i).areaOfCollision.y = gp.roomManager.currentRoom.enemies.get(i).defaultCollisionAreaY;
                }

                if (!player) {
                    gp.player.areaOfCollision.x = gp.player.defaultCollisionAreaX;
                    gp.player.areaOfCollision.y = gp.player.defaultCollisionAreaY;
                }
            }
        }
    }

    public void checkCharacterCollision(Bullet bullet) {

        if(bullet.shooterIsPlayer) {

            for (int i = 0; i < gp.roomManager.currentRoom.enemies.size(); i++) {
                if (!gp.roomManager.currentRoom.enemies.get(i).isDead) {

                    gp.roomManager.currentRoom.enemies.get(i).areaOfCollision.x += gp.roomManager.currentRoom.enemies.get(i).screenX;
                    gp.roomManager.currentRoom.enemies.get(i).areaOfCollision.y += gp.roomManager.currentRoom.enemies.get(i).screenY;

                    bullet.areaOfCollision.x += bullet.screenX;
                    bullet.areaOfCollision.y += bullet.screenY;

                    if (bullet.areaOfCollision.intersects(gp.roomManager.currentRoom.enemies.get(i).areaOfCollision)) {

                        gp.roomManager.currentRoom.enemies.get(i).receiveDamage();
                        gp.bullets.remove(bullet);
                    }

                    //RESTORING STANDARD VALUES
                    bullet.areaOfCollision.x = bullet.defaultCollisionAreaX;
                    bullet.areaOfCollision.y = bullet.defaultCollisionAreaY;

                    gp.roomManager.currentRoom.enemies.get(i).areaOfCollision.x = gp.roomManager.currentRoom.enemies.get(i).defaultCollisionAreaX;
                    gp.roomManager.currentRoom.enemies.get(i).areaOfCollision.y = gp.roomManager.currentRoom.enemies.get(i).defaultCollisionAreaY;
                }

            }

        } else {
            gp.player.areaOfCollision.x += gp.player.screenX;
            gp.player.areaOfCollision.y += gp.player.screenY;

            bullet.areaOfCollision.x += bullet.screenX;
            bullet.areaOfCollision.y += bullet.screenY;

            if (bullet.areaOfCollision.intersects(gp.player.areaOfCollision)&&!gp.player.isInvincible) {

                gp.player.receiveDamage();
                gp.player.isInvincible=true;
                gp.bullets.remove(bullet);
            }

            //RESTORING STANDARD VALUES
            bullet.areaOfCollision.x = bullet.defaultCollisionAreaX;
            bullet.areaOfCollision.y = bullet.defaultCollisionAreaY;

            gp.player.areaOfCollision.x = gp.player.defaultCollisionAreaX;
            gp.player.areaOfCollision.y = gp.player.defaultCollisionAreaY;
        }
    }
}
