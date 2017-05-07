package ru.hse.theremin.synthesizer;

public class SawtoothWave implements Wave {
    @Override
    public double getValue(double t) {
        return (2.0 / Math.PI) * Math.atan(Math.tan(t / 4.0));
    }
}
