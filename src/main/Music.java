package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

public class Music {
    public Clip clip;
    File[] soundFiles = new File[20];

    public long currentSecond;

    public Music() {
        soundFiles[0] = new File("resources/sound/mainTrack.wav");
        soundFiles[1] = new File("resources/sound/welcome.wav");
        soundFiles[2] = new File("resources/sound/what_buying.wav");
        soundFiles[3] = new File("resources/sound/hoho_foundme.wav");
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
