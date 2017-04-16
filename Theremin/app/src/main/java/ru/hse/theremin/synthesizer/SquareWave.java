package ru.hse.theremin.synthesizer;

public class SquareWave implements Wave {
    @Override
    public double getValue(double t) {
        // 0.5 to reduce square volume
        return 0.5 * Math.signum(Math.sin(t));
    }
}
