package ru.hse.theremin.synthesizer;

public class SquareWave implements Wave {
    @Override
    public double getValue(double t) {
        // 0.1 to reduce volume
        return 0.1 * Math.signum(Math.sin(t));
    }
}
