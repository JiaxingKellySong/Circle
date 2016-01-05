package client.media;

import javax.sound.sampled.AudioFormat;

/**
 * Created by jiaxing song on 2015/11/7.
 * The configuration of audio.
 */
public class AudioConfig {
    static private AudioFormat format = new AudioFormat(44100 / 2, 16, 1, true, false);

    static public AudioFormat getAudioFormat() {
        return format;
    }
}
