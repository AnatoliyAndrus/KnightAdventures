package main;

import characters.Player;
import objects.StaticObject;

import javax.sound.sampled.Clip;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyRecorder implements KeyListener {

    GamePanel gp;
    public boolean up, down, left, right;

    public KeyRecorder(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (gp.currentState == gp.titleState) {
            if (!gp.ui.loadingScreen && !gp.ui.difficultyChoice) {
                //TITLE
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    gp.ui.optionNum++;
                    if (gp.ui.optionNum == 3) gp.ui.optionNum = 1;
                    gp.playSound(15);
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    gp.ui.optionNum--;
                    if (gp.ui.optionNum == 0) gp.ui.optionNum = 2;
                    gp.playSound(15);
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    //NEW GAME
                    if (gp.ui.optionNum == 1) {
                        gp.ui.difficultyChoice = true;
                    }
                    //LOAD GAME
                    else if (gp.ui.optionNum == 3) {
                        //LOAD GAME LATER...
                        gp.ui.optionNum = 1;
                    }
                    //EXIT
                    else if (gp.ui.optionNum == 2) {
                        System.exit(0);
                    }
                    gp.playSound(15);
                }
            }
            else if (!gp.ui.loadingScreen) {
                //TITLE
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    gp.ui.optionNum++;
                    if (gp.ui.optionNum == 7) gp.ui.optionNum = 1;
                    gp.playSound(15);
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    gp.ui.optionNum--;
                    if (gp.ui.optionNum == 0) gp.ui.optionNum = 6;
                    gp.playSound(15);
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if(gp.ui.optionNum < 6) {
                        gp.difficulty = gp.ui.optionNum;
                        gp.ui.loadingScreen = true;
                    } else if(gp.ui.optionNum == 6) {
                        gp.ui.difficultyChoice = false;
                    }
                    gp.ui.optionNum = 1;
                    gp.playSound(15);
                }
            }
        }
        else if (gp.currentState == gp.gameState) {
            //MOVEMENT
            if (e.getKeyCode() == KeyEvent.VK_W) {
                up = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                down = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_A) {
                left = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                right = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if(!gp.ui.shopIsOpened) {
                    gp.currentState = gp.pauseState;
                    gp.pauseMusic();
                    gp.waterSound.stop();
                    gp.playSound(15);
                    if(gp.roomManager.currentRoom.phase.equals("in progress") && !gp.player.scriptsAreActive)
                        gp.ui.optionNum = 1;
                    else
                        gp.ui.optionNum = 2;
                } else {
                    gp.ui.shopIsOpened = false;
                    gp.player.shooting = false;
                    gp.ui.optionNum = 1;
                    gp.playSound(16);
                }
            }
            //F INTERACTIONS
            if (e.getKeyCode() == KeyEvent.VK_F) {
                switch (gp.player.interactedObjectName) {
                    case "shop" -> {
                        if(!gp.ui.shopIsOpened) {
                            gp.ui.shopIsOpened = true;
                            gp.ui.optionNum = 1;
                            gp.ui.hintTimer = 0;
                        }
                    }
                    case "dungeonTorches" -> {
                        if(gp.player.screenY <= gp.maxScreenHeight - gp.squareSize * 3) {
                            if (!gp.player.hasTorch) {
                                gp.player.hasTorch = true;
                                gp.player.setPlayerImages(true);
                                gp.roomManager.currentRoom.staticObjects.get(0).setAnimation(StaticObject.NO_ANIMATION);
                                gp.roomManager.currentRoom.staticObjects.get(1).setAnimation(StaticObject.NO_ANIMATION);
                                gp.playSound(18);
                            } else {
                                gp.player.hasTorch = false;
                                gp.player.setPlayerImages(false);
                                gp.roomManager.currentRoom.staticObjects.get(0).setAnimation(StaticObject.ANIMATION_CONTINUOUSLY);
                                gp.roomManager.currentRoom.staticObjects.get(1).setAnimation(StaticObject.ANIMATION_CONTINUOUSLY);
                                gp.playSound(18);
                            }
                        }

                        gp.player.interactedObjectName = "";
                    }
                    case "chest" -> {
                        if(!gp.roomManager.currentRoom.staticObjects.get(2).isOpened &&
                                !(gp.roomManager.currentRoom.staticObjects.get(2).animation == StaticObject.ANIMATION_ONCE)
                                && gp.roomManager.currentRoom.phase.equals("completed")) {
                            gp.roomManager.currentRoom.staticObjects.get(2).isOpened = true;
                            gp.roomManager.currentRoom.staticObjects.get(2).setAnimation(StaticObject.ANIMATION_ONCE);
                            gp.playSound(19);
                        } else if(!gp.roomManager.currentRoom.staticObjects.get(2).emptyChest &&
                                !(gp.roomManager.currentRoom.staticObjects.get(2).animation == StaticObject.ANIMATION_ONCE)
                                && gp.roomManager.currentRoom.phase.equals("completed")){
                            gp.roomManager.currentRoom.staticObjects.get(2).emptyChest = true;
                            gp.roomManager.currentRoom.staticObjects.get(2).noAnimation = gp.roomManager.currentRoom.staticObjects.get(2).images.get(2);
                            gp.player.hasBossKey = true;
                            gp.playSound(12);
                        }
                    }
                    case "lever" -> {
                        if(gp.roomManager.currentRoom.name.equals("finalMap") &&
                                gp.roomManager.currentRoom.staticObjects.get(0).animation != StaticObject.ANIMATION_ONCE) {
                            gp.roomManager.currentRoom.staticObjects.get(0).setAnimation(StaticObject.ANIMATION_ONCE);
                            gp.playSound(17);
                        }
                        if(gp.roomManager.currentRoom.phase.equals("ruins unique phase") &&
                                gp.roomManager.currentRoom.staticObjects.get(2).animation != StaticObject.ANIMATION_ONCE) {
                            gp.roomManager.currentRoom.staticObjects.get(2).setAnimation(StaticObject.ANIMATION_ONCE);
                            gp.playSound(17);
                        }
                    }
                    case "boss_door" -> {
                        if (!gp.roomManager.currentRoom.staticObjects.get(3).isOpened && gp.player.hasBossKey
                                && gp.roomManager.currentRoom.name.equals("castle")) {
                            gp.roomManager.currentRoom.staticObjects.get(3).isOpened = true;
                            gp.player.hasBossKey = false;
                            gp.roomManager.currentRoom.staticObjects.get(3).setAnimation(StaticObject.ANIMATION_ONCE);
                            gp.playSound(14);
                            gp.roomManager.currentRoom.doorsOpeningNow++;
                        }
                    }
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if(gp.ui.shopIsOpened) {
                    gp.ui.optionNum++;
                    if (gp.ui.optionNum == 4) gp.ui.optionNum = 1;
                    gp.playSound(15);
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if(gp.ui.shopIsOpened) {
                    gp.ui.optionNum--;
                    if (gp.ui.optionNum == 0) gp.ui.optionNum = 3;
                    gp.playSound(15);
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if(gp.ui.shopIsOpened) {
                    if(gp.ui.optionNum == 1 && gp.player.coinsAmount >= gp.ui.armorPrice && gp.player.armor < 5) {
                        gp.player.coinsAmount -= gp.ui.armorPrice;
                        gp.player.armor = 5;
                        gp.playSound(13);
                    }
                    if(gp.ui.optionNum == 2 && gp.player.coinsAmount >= gp.ui.shotgunPrice && !gp.player.shotgunFire) {
                        gp.player.coinsAmount -= gp.ui.shotgunPrice;
                        if(gp.player.burstFire) gp.player.setFireMode(Player.BURST_SHOTGUN);
                        else gp.player.setFireMode(Player.SHOTGUN);
                        gp.playSound(13);
                    }
                    if(gp.ui.optionNum == 3 && gp.player.coinsAmount >= gp.ui.burstPrice && !gp.player.burstFire) {
                        gp.player.coinsAmount -= gp.ui.burstPrice;
                        if(gp.player.shotgunFire) gp.player.setFireMode(Player.BURST_SHOTGUN);
                        else gp.player.setFireMode(Player.BURST);
                        gp.playSound(13);
                    }
                }
            }
        }
        //PAUSE
        else if (gp.currentState == gp.pauseState) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                gp.currentState = gp.gameState;
                gp.playSound(15);
                gp.playMusic(gp.currentSong);
                if(gp.roomManager.currentRoom.name.equals("ruins")){
                    gp.waterSound.start();
                    gp.waterSound.loop(Clip.LOOP_CONTINUOUSLY);
                }
                gp.ui.optionNum = 1;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if(gp.roomManager.currentRoom.phase.equals("in progress") && !gp.player.scriptsAreActive) {
                    gp.ui.optionNum++;
                    if (gp.ui.optionNum == 3) {
                        gp.ui.optionNum = 1;
                    }
                    gp.playSound(15);
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if(gp.roomManager.currentRoom.phase.equals("in progress") && !gp.player.scriptsAreActive) {
                    gp.ui.optionNum--;
                    if (gp.ui.optionNum == 0) {
                        gp.ui.optionNum = 2;
                    }
                    gp.playSound(15);
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if(gp.ui.optionNum == 1) {
                    gp.retry();
                    gp.currentState = gp.gameState;
                }
                if(gp.ui.optionNum == 2) {
                    gp.restartGame();
                    gp.ui.optionNum = 1;
                }
                gp.playSound(15);
            }
        }
        //GAME OVER
        else if (gp.currentState == gp.gameOverState) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                gp.ui.optionNum++;
                if (gp.ui.optionNum == 3) gp.ui.optionNum = 1;
                gp.playSound(15);
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                gp.ui.optionNum--;
                if (gp.ui.optionNum == 0) gp.ui.optionNum = 2;
                gp.playSound(15);
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if(gp.ui.optionNum == 1) {
                    gp.retry();
                    gp.currentState = gp.gameState;
                }
                if(gp.ui.optionNum == 2) {
                    gp.restartGame();
                }
                gp.playSound(15);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        //MOVEMENT
        if (e.getKeyCode() == KeyEvent.VK_W) {
            up = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            down = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            left = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            right = false;
        }
    }
}
