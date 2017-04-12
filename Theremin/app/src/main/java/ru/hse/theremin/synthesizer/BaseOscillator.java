package ru.hse.theremin.synthesizer;

public abstract class BaseOscillator implements Oscillator {

    private int amp;
    private double freq;
    private int buffSize;

    public BaseOscillator(int amp, double freq, int buffSize) {
        this.amp = amp;
        this.freq = freq;
        this.buffSize = buffSize;
    }

    public int getAmp() {
        return amp;
    }

    public double getFreq() {
        return freq;
    }

    public int getBuffSize() {
        return buffSize;
    }

    public void setAmp(int amp) {
        this.amp = amp;
    }

    public void setFreq(double freq) {
        this.freq = freq;
    }

    public void setBuffSize(int buffSize) {
        this.buffSize = buffSize;
    }
}
