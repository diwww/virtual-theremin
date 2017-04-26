package ru.hse.theremin.synthesizer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioPlayer {
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

    private void majorChord() {
        short[] data;
        oscillator.setFreq(index);
        data = oscillator.generate();
        audioTrack.write(data, 0, data.length);
        oscillator.setFreq(index + 4);
        data = oscillator.generate();
        audioTrack.write(data, 0, data.length);
        oscillator.setFreq(index + 7);
        data = oscillator.generate();
        audioTrack.write(data, 0, data.length);
    }

    private void minorChord() {
        short[] data;
        oscillator.setFreq(index);
        data = oscillator.generate();
        audioTrack.write(data, 0, data.length);
        oscillator.setFreq(index + 3);
        data = oscillator.generate();
        audioTrack.write(data, 0, data.length);
        oscillator.setFreq(index + 7);
        data = oscillator.generate();
        audioTrack.write(data, 0, data.length);
    }

    private void twoNotes() {
        short[] data;
        oscillator.setFreq(index);
        data = oscillator.generate();
        audioTrack.write(data, 0, data.length);
        oscillator.setFreq(index - 12);
        data = oscillator.generate();
        audioTrack.write(data, 0, data.length);

    }

    private void oneNote() {
        short[] data;
        oscillator.setFreq(index);
        data = oscillator.generate();
        audioTrack.write(data, 0, data.length);
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
