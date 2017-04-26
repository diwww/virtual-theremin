package ru.hse.theremin;

import android.os.AsyncTask;

import ru.hse.theremin.synthesizer.AudioPlayer;

public class SynthAsyncTask extends AsyncTask<AudioPlayer, Void, Void> {

    @Override
    protected Void doInBackground(AudioPlayer... params) {
        params[0].play();
        return null;
    }
}
