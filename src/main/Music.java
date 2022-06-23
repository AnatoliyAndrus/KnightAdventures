package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Music {
    public Clip clip;
    File[] soundFiles = new File[20];

    public long currentSecond;

    public Music() {
        soundFiles[0] = new File("resources/sound/musics/mainTrack.wav");
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

    public void stop(){
        currentSecond = clip.getMicrosecondPosition();
        clip.stop();
    }
}
