package ru.hse.theremin.synthesizer;

public class TriangleWave implements Wave {
    @Override
    public double getValue(double t) {
        return (2.0 / Math.PI) * Math.asin(Math.sin(t));
    }
}
