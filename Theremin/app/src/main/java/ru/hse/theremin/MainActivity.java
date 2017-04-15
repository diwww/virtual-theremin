package ru.hse.theremin;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioTrack;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import ru.hse.theremin.synthesizer.BaseOscillator;
import ru.hse.theremin.synthesizer.Oscillator;
import ru.hse.theremin.synthesizer.SineOscillator;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private Button playButton, stopButton;
    private TextView idxTextView, rateTextView;
    private BaseOscillator oscillator;
    private boolean playing = false;
    private float freq;

    public boolean isPlaying() {
        return playing;
    }

    public float getFreq() {
        return freq;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Linking views
        playButton = (Button) findViewById(R.id.play_button);
        stopButton = (Button) findViewById(R.id.stop_button);
        idxTextView = (TextView) findViewById(R.id.idx_textview);
        rateTextView = (TextView) findViewById(R.id.rate_textview);
        // Sensors initialization
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void onButtonClicked(View v) {
        switch (v.getId()) {
            case R.id.play_button:
                Toast.makeText(this, "Playing", Toast.LENGTH_SHORT).show();
                playing = true;
                new SynthAsyncTask().execute(this);
                playButton.setEnabled(false);
                stopButton.setEnabled(true);
                break;
            case R.id.stop_button:
                Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
                playing = false;
                stopButton.setEnabled(false);
                playButton.setEnabled(true);
                break;
            default:
                break;
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        long index = Math.round(event.values[0] * 1.2);
        freq = (float) Math.pow(2, index / 12.0) * 130.82f * 2;

        idxTextView.setText(String.format(Locale.US, "Index: %d", Math.round(event.values[0] * 1.2)));
        rateTextView.setText(String.format(Locale.US, "Freq: %.2f", freq));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
