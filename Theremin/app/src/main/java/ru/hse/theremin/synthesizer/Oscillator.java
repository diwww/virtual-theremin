package ru.hse.theremin.synthesizer;

public class Oscillator {

    // Constants
    public static final int CHANNELS = 1;
    public static final int SAMPLE_RATE = 44100;

    private Wave wave;
    private short amp;
    private double freq;
    private int buffSize;
    // t must be initialized here, but not in
    // generate() method. It helps to avoid clicks, since
    // t continues to increase starting from its last value
    // and periodic function smoothly flows further.
    // TODO: think of double overflow
    private double t = 0.0;


    public Oscillator(Wave wave, short amp, double freq, int buffSize) {
        this.wave = wave;
        this.amp = amp;
        this.freq = freq;
        this.buffSize = buffSize;
    }

    public short[] generate() {
        short[] data = new short[buffSize];
        for (int i = 0; i <= data.length - CHANNELS; i += CHANNELS) {
            for (int channel = 0; channel < CHANNELS; channel++) {
                data[i + channel] = (short) (amp * wave.getValue(t));
            }
            t += (2 * Math.PI * freq) / (SAMPLE_RATE * CHANNELS);
        }
        return data;
    }

    public Wave getWave() {
        return wave;
    }

    public short getAmp() {
        return amp;
    }

    public double getFreq() {
        return freq;
    }

    public int getBuffSize() {
        return buffSize;
    }

    public void setWave(Wave wave) {
        this.wave = wave;
    }

    public void setAmp(short amp) {
        this.amp = amp;
    }

    public void setFreq(double freq) {
        this.freq = freq;
    }

    public void setBuffSize(int buffSize) {
        this.buffSize = buffSize;
    }
}
