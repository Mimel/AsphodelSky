package display.music;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AudioPlayer implements Runnable {

    private final Object lock;

    private final Map<String, MediaPlayer> music;
    private final Map<String, AudioClip> sfx;

    private double musicVolume;
    private double sfxVolume;

    private MediaPlayer selectedSong;
    private AudioClip selectedClip;

    private boolean isIncomingAudioASong;

    public AudioPlayer(String musicDirectory, String sfxDirectory) {
        lock = new Object();

        music = new HashMap<>();
        sfx = new HashMap<>();

        musicVolume = 1.0;
        sfxVolume = 1.0;

        isIncomingAudioASong = true;

        initializeMusicMap(musicDirectory);
        initializeSFXMap(sfxDirectory);
    }

    public void adjustMusicVolume(int adjustment) {
        musicVolume += adjustment;

        if(musicVolume > 1.0) {
            musicVolume = 1.0;
        } else if(musicVolume < 0.0) {
            musicVolume = 0.0;
        }
    }

    public void adjustSFXVolume(int adjustment) {
        sfxVolume += adjustment;

        if(sfxVolume > 1.0) {
            sfxVolume = 1.0;
        } else if(sfxVolume < 0.0) {
            sfxVolume = 0.0;
        }
    }

    public void playSong(String songName) {
        synchronized(lock) {
            selectedSong = music.get(songName);
            isIncomingAudioASong = true;
            lock.notify();
        }
    }

    public void playSFX(String sfxName) {
        synchronized(lock) {
            selectedClip = sfx.get(sfxName);
            isIncomingAudioASong = false;
            lock.notify();
        }
    }

    @Override
    public void run() {
        while(true) {
            synchronized(lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(isIncomingAudioASong) {
                    if(selectedSong.getStatus() == MediaPlayer.Status.PLAYING) {
                        selectedSong.stop();
                    }

                    selectedSong.setVolume(musicVolume);
                    selectedSong.play();
                } else {
                    selectedClip.play(sfxVolume);
                }
            }
        }
    }

    private void initializeMusicMap(String musicDirectoryName) {
        try(DirectoryStream<Path> musicStream = Files.newDirectoryStream(new File(musicDirectoryName).toPath(), "*.mp3")) {
            for(Path songPath : musicStream) {
                Media song = new Media(songPath.toUri().toString());
                MediaPlayer mp = new MediaPlayer(song);

                mp.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        mp.seek(Duration.ZERO);
                    }
                });

                music.put(songPath.getFileName().toString(), mp);
            }
        } catch (IOException e) {
            System.out.println("Cannot find music folder at location " + musicDirectoryName + "./nEnding Program.");
            e.printStackTrace();
            System.exit(2);
        }
    }

    private void initializeSFXMap(String sfxDirectoryName) {
        try(DirectoryStream<Path> sfxStream = Files.newDirectoryStream(new File(sfxDirectoryName).toPath(), "*.mp3")) {
            for(Path sfxPath : sfxStream) {
                sfx.put(sfxPath.getFileName().toString(), new AudioClip(sfxPath.toUri().toString()));
            }
        } catch (IOException e) {
            System.out.println("Cannot find sfx folder at location " + sfxDirectoryName + "./nEndingProgram");
            e.printStackTrace();
            System.exit(3);
        }
    }
}
