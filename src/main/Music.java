package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Music {
    public Clip clip;
    File[] soundFiles = new File[30];

    public long currentSecond;

    public Music() {
        soundFiles[0] = new File("resources/sound/musics/ruins.wav");
        soundFiles[1] = new File("resources/sound/attacks/player_single_shot.wav");
        soundFiles[2] = new File("resources/sound/attacks/alien_single_shot.wav");
        soundFiles[3] = new File("resources/sound/shop/welcome.wav");
        soundFiles[4] = new File("resources/sound/shop/what_buying.wav");
        soundFiles[5] = new File("resources/sound/shop/hoho_foundme.wav");
        soundFiles[6] = new File("resources/sound/attacks/enemy_claw_hit.wav");
        soundFiles[7] = new File("resources/sound/attacks/patron_shell.wav");
        soundFiles[8] = new File("resources/sound/attacks/claw_enemy_hitted.wav");
        soundFiles[9] = new File("resources/sound/attacks/alien_hitted.wav");
        soundFiles[10] = new File("resources/sound/attacks/player_hitted.wav");
        soundFiles[11] = new File("resources/sound/door/door_opens.wav");
        soundFiles[12] = new File("resources/sound/coin/coin.wav");
        soundFiles[13] = new File("resources/sound/shop/purchase.wav");
        soundFiles[14] = new File("resources/sound/door/boss_door.wav");
        soundFiles[15] = new File("resources/sound/menu/menu_switch.wav");
        soundFiles[16] = new File("resources/sound/shop/shop_bell.wav");
        soundFiles[17] = new File("resources/sound/lever/lever.wav");
        soundFiles[18] = new File("resources/sound/torch/torches.wav");
        soundFiles[19] = new File("resources/sound/chest/chest_opening.wav");
        soundFiles[20] = new File("resources/sound/boss/alien_spawning.wav");
    }

    public void setClip(int index) {
        try {
            AudioInputStream aus = AudioSystem.getAudioInputStream(soundFiles[index]);
            clip = AudioSystem.getClip();
            clip.open(aus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(){
        clip.setMicrosecondPosition(currentSecond);
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void pause(){
        currentSecond = clip.getMicrosecondPosition();
        clip.stop();
    }

    public void stop(){
        clip.stop();
    }
}
