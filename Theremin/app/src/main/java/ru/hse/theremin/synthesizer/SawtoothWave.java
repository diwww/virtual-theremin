package ru.hse.theremin.synthesizer;

public class SawtoothWave implements Wave {
    @Override
    public double getValue(double t) {
        // 0.2 to reduce volume
        return 0.2 * (2.0 / Math.PI) * Math.atan(Math.tan(t / 4.0));
    }
}
