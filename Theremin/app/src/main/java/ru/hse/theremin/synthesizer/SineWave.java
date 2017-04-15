package ru.hse.theremin.synthesizer;

public class SineWave implements Wave {

    @Override
    public double getValue(double t) {
        return Math.sin(t);
    }
}
