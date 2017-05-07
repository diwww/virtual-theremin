package ru.hse.theremin.synthesizer;

/**
 * Annoying and disgusting sound.
 * Remove abstract modifier at your
 * own risk. It may damage phone speakers
 * and your ears.
 */
public abstract class SquareWave implements Wave {
    @Override
    public double getValue(double t) {
        // 0.5 to reduce square volume
        return Math.signum(Math.sin(t));
    }
}
