package ru.hse.theremin;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.system.Os;

import ru.hse.theremin.synthesizer.BaseOscillator;
import ru.hse.theremin.synthesizer.Oscillator;
import ru.hse.theremin.synthesizer.SineOscillator;

public class SynthAsyncTask extends AsyncTask<MainActivity, Void, Void> {

    AudioTrack audioTrack;
    BaseOscillator oscillator;

    @Override
    protected Void doInBackground(MainActivity... params) {


//        int buffSize = AudioTrack.getMinBufferSize(Oscillator.SAMPLE_RATE,
//                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
//        int buffSize = params[0].getBuffSize();

        final int numSamples = (int) (Oscillator.SAMPLE_RATE * Oscillator.CHANNELS * 0.1);
        final int buffSize = (Short.SIZE / Byte.SIZE) * numSamples * 3;

        oscillator = new SineOscillator((short) (Short.MAX_VALUE / 2), 440, numSamples);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, Oscillator.SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffSize,
                AudioTrack.MODE_STREAM);

        audioTrack.play();

        while (params[0].isPlaying()) {
            short[] data;

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
