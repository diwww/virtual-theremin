package ru.hse.theremin.synthesizer;

public class TriangleWave implements Wave {
    @Override
    public double getValue(double t) {
        return Math.asin(Math.sin(t));
    }
}
