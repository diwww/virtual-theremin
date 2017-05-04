package ru.hse.theremin.synthesizer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioPlayer {

    public static final int CHORD_NUM_REPEATS = 4;
    public static final int ONE_NOTE_NUM_REPEATS = 1;

    public enum PlayMode {OneNote, TwoNotes, MajorChord, MinorChord;}

    private final Oscillator oscillator = new Oscillator();
    private AudioTrack audioTrack;
    private int index;
    private boolean playing;
    private PlayMode playMode = PlayMode.OneNote;

    public void setWave(Wave wave) {
        oscillator.setWave(wave);
    }

    public void setFreq(int index) {
        this.index = index;
    }

    public void setPlayMode(PlayMode playMode) {
        this.playMode = playMode;
    }

    private void chord(int numRepeats, int... indices) {
        short[] data;
        for (int i = 0; i < indices.length; i++) {
            oscillator.setFreq(indices[i]);
            for (int j = 0; j < numRepeats; j++) {
                data = oscillator.generate();
                audioTrack.write(data, 0, data.length);
            }
        }
    }

    private void majorChord() {
        chord(CHORD_NUM_REPEATS, index, index + 4, index + 7);
    }

    private void minorChord() {
        chord(CHORD_NUM_REPEATS, index, index + 3, index + 7);
    }

    private void twoNotes() {
        chord(CHORD_NUM_REPEATS, index, index - 12);
    }

    private void oneNote() {
        chord(ONE_NOTE_NUM_REPEATS, index);
    }


    public void play() {
        playing = true;
        try {
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, Oscillator.SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, Oscillator.BUFF_SIZE,
                    AudioTrack.MODE_STREAM);
            audioTrack.play();
            while (playing) {
                switch (playMode) {
                    case OneNote:
                        oneNote();
                        break;
                    case TwoNotes:
                        twoNotes();
                        break;
                    case MajorChord:
                        majorChord();
                        break;
                    case MinorChord:
                        minorChord();
                        break;
                    default:
                        break;
                }
            }
        } finally {
            audioTrack.stop();
            audioTrack.release();
        }
    }

    public void stop() {
        playing = false;
    }
}
