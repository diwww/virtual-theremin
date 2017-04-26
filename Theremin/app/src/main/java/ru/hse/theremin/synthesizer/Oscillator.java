package ru.hse.theremin.synthesizer;

public class Oscillator {

    // Constants
    public static final int CHANNELS = 1;
    public static final int SAMPLE_RATE = 44100;
    // TODO: нужно уменьшать длительность для одной ноты
    public static final double DURATION = 0.2;
    public static final int NUM_SAMPLES = (int) (Oscillator.SAMPLE_RATE * Oscillator.CHANNELS * DURATION);
    // TODO: В принципе, работает без умножения на 3 для трех звуков
    // TODO: Подумать, стоит ли умножать размер буфера на 3
    public static final int BUFF_SIZE = (Short.SIZE / Byte.SIZE) * NUM_SAMPLES;
    public static final double C_FIRST_OCTAVE = 261.63;

    private Wave wave;
    private short amp;
    private double freq;
    // t must be initialized here, but not in
    // generate() method. It helps to avoid clicks, since
    // t continues to increase starting from its last value
    // and periodic function smoothly flows further.
    private double t = 0.0;


    public Oscillator() {
        this.wave = new SineWave();
        this.amp = (short) (Short.MAX_VALUE / 4);
        this.freq = C_FIRST_OCTAVE;
    }

    public short[] generate() {
        short[] data = new short[NUM_SAMPLES];
        for (int i = 0; i <= data.length - CHANNELS; i += CHANNELS) {
            for (int channel = 0; channel < CHANNELS; channel++) {
                data[i + channel] = (short) (amp * wave.getValue(t));
            }
            t += (2 * Math.PI * freq) / (SAMPLE_RATE * CHANNELS);
        }
        return data;
    }

    public Wave getWave() {
        return wave;
    }

    public short getAmp() {
        return amp;
    }

    public double getFreq() {
        return freq;
    }

    public void setWave(Wave wave) {
        this.wave = wave;
    }

    public void setAmp(short amp) {
        this.amp = amp;
    }

    public void setFreq(int index) {
        this.freq = Math.pow(2, index / 12.0) * C_FIRST_OCTAVE;
    }
}
