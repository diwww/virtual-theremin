package ru.hse.theremin;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;

import ru.hse.theremin.synthesizer.Oscillator;
import ru.hse.theremin.synthesizer.SineWave;

public class SynthAsyncTask extends AsyncTask<MainActivity, Void, Void> {

    private Oscillator oscillator;
    private AudioTrack audioTrack;

    private void majorChord(MainActivity activity) {
        while (activity.isPlaying()) {
            short[] data;
            oscillator.setWave(activity.getWave());

            oscillator.setFreq(activity.getFreq());
            data = oscillator.generate();
            audioTrack.write(data, 0, data.length);

            oscillator.setFreq(activity.getFreq() * Math.pow(2, 4.0 / 12));
            data = oscillator.generate();
            audioTrack.write(data, 0, data.length);

            oscillator.setFreq(activity.getFreq() * Math.pow(2, 7.0 / 12));
            data = oscillator.generate();
            audioTrack.write(data, 0, data.length);
        }
    }

    private void minorChord(MainActivity activity) {
        while (activity.isPlaying()) {
            short[] data;
            oscillator.setWave(activity.getWave());

            oscillator.setFreq(activity.getFreq());
            data = oscillator.generate();
            audioTrack.write(data, 0, data.length);

            oscillator.setFreq(activity.getFreq() * Math.pow(2, 3.0 / 12));
            data = oscillator.generate();
            audioTrack.write(data, 0, data.length);

            oscillator.setFreq(activity.getFreq() * Math.pow(2, 7.0 / 12));
            data = oscillator.generate();
            audioTrack.write(data, 0, data.length);
        }
    }

    private void twoNotes(MainActivity activity) {
        while (activity.isPlaying()) {
            short[] data;
            oscillator.setWave(activity.getWave());

            oscillator.setFreq(activity.getFreq());
            data = oscillator.generate();
            audioTrack.write(data, 0, data.length);

            oscillator.setFreq(activity.getFreq() * 0.5);
            data = oscillator.generate();
            audioTrack.write(data, 0, data.length);
        }
    }

    private void oneNote(MainActivity activity) {
        while (activity.isPlaying()) {
            short[] data;
            oscillator.setWave(activity.getWave());

            oscillator.setFreq(activity.getFreq());
            data = oscillator.generate();
            audioTrack.write(data, 0, data.length);
        }
    }

    @Override
    protected Void doInBackground(MainActivity... params) {
        // Default oscillator
        oscillator = new Oscillator(new SineWave(), (short) (Short.MAX_VALUE / 4), 440);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, Oscillator.SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, Oscillator.BUFF_SIZE,
                AudioTrack.MODE_STREAM);
        audioTrack.play();
        twoNotes(params[0]);
        audioTrack.stop();
        audioTrack.release();

        return null;
    }
}
