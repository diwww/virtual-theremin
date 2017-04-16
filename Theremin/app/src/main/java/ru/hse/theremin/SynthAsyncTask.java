package ru.hse.theremin;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;

import ru.hse.theremin.synthesizer.Oscillator;
import ru.hse.theremin.synthesizer.SineWave;

public class SynthAsyncTask extends AsyncTask<MainActivity, Void, Void> {

    @Override
    protected Void doInBackground(MainActivity... params) {

        // Default oscillator
        Oscillator oscillator = new Oscillator(new SineWave(), (short) (Short.MAX_VALUE / 4), 440);
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, Oscillator.SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, Oscillator.BUFF_SIZE,
                AudioTrack.MODE_STREAM);
        audioTrack.play();

        while (params[0].isPlaying()) {
            short[] data;
            oscillator.setWave(params[0].getWave());

            oscillator.setFreq(params[0].getFreq());
            data = oscillator.generate();
            audioTrack.write(data, 0, data.length);

            oscillator.setFreq(params[0].getFreq() * Math.pow(2, 4.0 / 12));
            data = oscillator.generate();
            audioTrack.write(data, 0, data.length);

            oscillator.setFreq(params[0].getFreq() * Math.pow(2, 7.0 / 12));
            data = oscillator.generate();
            audioTrack.write(data, 0, data.length);
        }

        audioTrack.stop();
        audioTrack.release();

        return null;
    }
}
