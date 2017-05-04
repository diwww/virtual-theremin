package ru.hse.theremin.synthesizer;

public class Oscillator {

    public static final int CHANNELS = 1; // Mono configuration
    public static final int SAMPLE_RATE = 44100;
    public static final double DURATION = 0.05; // Such small duration helps to reduce a latency
    public static final int NUM_SAMPLES = (int) (Oscillator.SAMPLE_RATE * Oscillator.CHANNELS * DURATION);
    public static final int BUFF_SIZE = (Short.SIZE / Byte.SIZE) * NUM_SAMPLES;
    public static final double C_FIRST_OCTAVE = 261.63;

    private Wave wave;
    private short amp;
    private double freq;
    /**
     * This value must be initialized here, but not in
     * generate() method. It helps to avoid clicks, since
     * t continues to increase starting from its last value
     * and periodic function smoothly flows further.
     */
    private double t = 0.0;

    /**
     * Creates an oscillator with default values.
     */
    public Oscillator() {
        this.wave = new SineWave();
        this.amp = (short) (Short.MAX_VALUE / 4);
        this.freq = C_FIRST_OCTAVE;
    }

    /**
     * Produces a new PCM sound wave with given values.
     *
     * @return Raw PCM data, stored in an array of shorts.
     */
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

    /**
     * Gets current wave type.
     *
     * @return Wave type, which is in use now.
     */
    public Wave getWave() {
        return wave;
    }

    /**
     * Gets current amplitude value.
     *
     * @return An amplitude, which is in use now.
     */
    public short getAmp() {
        return amp;
    }

    /**
     * Gets current frequency value.
     *
     * @return A frequency, which is in use now.
     */
    public double getFreq() {
        return freq;
    }

    /**
     * Sets current wave type.
     *
     * @param wave new wave type to set
     */
    public void setWave(Wave wave) {
        this.wave = wave;
    }

    /**
     * Sets a new amplitude value.
     *
     * @param amp new amplitude to set
     */
    public void setAmp(short amp) {
        this.amp = amp;
    }

    /**
     * Sets a new frequency value.
     *
     * @param index new frequency to set.
     */
    public void setFreq(int index) {
        this.freq = Math.pow(2, index / 12.0) * C_FIRST_OCTAVE;
    }
}
