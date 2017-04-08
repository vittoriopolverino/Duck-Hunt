package duckhunt.utility.sound;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import javax.sound.sampled.*;

/**
 * 
 * @author Vittorio Polverino
 */

public class Sound {

    private static final Line.Info INFO = new Line.Info(Clip.class);
    private static HashMap<String, Sound> map;
    private URL soundUrl;
    private Clip readyClip;

    public Sound(String path) throws SoundException, MalformedURLException {
        this(new URL(path));
    }

    public Sound(URL url) throws SoundException {
        if (url == null) {
            throw new SoundException("Cannot read null url path");
        }
        this.soundUrl = url;
        this.readyClip = this.getNewClip();
    }

    public Clip play() {
        return play(1);
    }

    public Clip loop() {
        return play(-1);
    }

    public Clip play(int times) {
        Clip clip = null;
        try {
            clip = getNewClip();
        } catch (SoundException ex) {
            throw new RuntimeException(ex);
        }
        if (clip != null && times != 0) {
            clip.loop(times - 1);
        } 
        return clip;
    }


    public final Clip getNewClip() throws SoundException {
        try {
            if (this.readyClip == null) {
                this.readyClip = Sound.getNewClip(this.soundUrl);
            }
            Clip c = this.readyClip;
            this.readyClip = Sound.getNewClip(this.soundUrl);
            return c;
        } catch (SoundException ex) {
            this.readyClip = null;
            throw ex;
        }
    }

    public static HashMap<String, Sound> getMap() {
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }


    public static Clip getNewClip(URL clipURL) throws SoundException {
        Clip clip = null;
        try {
            clip = (Clip) AudioSystem.getLine(INFO);
            clip.open(AudioSystem.getAudioInputStream(clipURL));
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
            throw new SoundException(clipURL.getFile(), ex);
        }
        return clip;
    }
}