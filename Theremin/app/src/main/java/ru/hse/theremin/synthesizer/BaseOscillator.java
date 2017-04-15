package ru.hse.theremin.synthesizer;

public abstract class BaseOscillator implements Oscillator {

    private short amp;
    private double freq;
    private int buffSize;

    public BaseOscillator(short amp, double freq, int buffSize) {
        this.amp = amp;
        this.freq = freq;
        this.buffSize = buffSize;
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
