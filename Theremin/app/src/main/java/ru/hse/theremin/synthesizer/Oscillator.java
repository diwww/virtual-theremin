package ru.hse.theremin.synthesizer;

public interface Oscillator {
    int CHANNELS = 1;
    int SAMPLE_RATE = 44100;

    short[] generate();
}
