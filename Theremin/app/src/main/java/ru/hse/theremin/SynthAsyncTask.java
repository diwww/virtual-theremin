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

        int buffSize = AudioTrack.getMinBufferSize(Oscillator.SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        oscillator = new SineOscillator(Short.MAX_VALUE, 440, (int) (Oscillator.SAMPLE_RATE * 0.05));
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, Oscillator.SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffSize,
                AudioTrack.MODE_STREAM);
        audioTrack.play();

        while (params[0].isPlaying()) {
            oscillator.setFreq(params[0].getFreq());
            short[] data = oscillator.generate();
            audioTrack.write(data, 0, data.length);
        }

        audioTrack.stop();
        audioTrack.release();

        return null;
    }
}
