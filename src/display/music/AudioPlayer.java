package display.music;

import javafx.scene.media.AudioClip;

import java.io.File;

public class AudioPlayer implements Runnable {

    private final Object lock;
    private final AudioClip mp;
    private boolean running;

    public AudioPlayer(String sampleFX) {
        lock = new Object();
        mp = new AudioClip(new File(sampleFX).toURI().toString());
        running = true;
    }

    public void playSound() {
        synchronized(lock) {
            lock.notify();
        }
    }

    public void stopAudioPlayer() {
        running = false;
    }

    @Override
    public void run() {
        while(running) {
            synchronized(lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mp.play();
            }
        }
    }
}
