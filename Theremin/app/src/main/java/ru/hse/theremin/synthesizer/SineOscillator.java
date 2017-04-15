package ru.hse.theremin.synthesizer;

public class SineOscillator extends BaseOscillator {
    double t = 0.0;

    public SineOscillator(short amp, double freq, int buffSize) {
        super(amp, freq, buffSize);
    }

    @Override
    public short[] generate() {
        short[] data = new short[super.getBuffSize()];

        for (int i = 0; i <= data.length - CHANNELS; i += CHANNELS) {
            for (int channel = 0; channel < CHANNELS; channel++) {
                data[i + channel] = (short) (super.getAmp() * Math.sin(t));
            }
            t += (2 * Math.PI * super.getFreq()) / (SAMPLE_RATE * CHANNELS);
        }

        return data;
    }
}
