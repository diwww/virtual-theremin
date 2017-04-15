package ru.hse.theremin.synthesizer;

public class SquareWave implements Wave {
    @Override
    public double getValue(double t) {
        return Math.signum(Math.sin(t));
    }
}
