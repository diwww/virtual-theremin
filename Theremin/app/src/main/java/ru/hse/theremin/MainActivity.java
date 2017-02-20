package ru.hse.theremin;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SoundPool.OnLoadCompleteListener, SensorEventListener {

    SensorManager sensorManager;
    Sensor sensor;
    private SoundPool soundPool;
    private int soundId;
    private int streamId;
    private Button playButton, stopButton;
    private TextView xTextView, yTextView, zTextView, rateTextView;
    private CheckBox discrCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = (Button) findViewById(R.id.play_button);
        stopButton = (Button) findViewById(R.id.stop_button);

        xTextView = (TextView) findViewById(R.id.x_textview);
        yTextView = (TextView) findViewById(R.id.y_textview);
        zTextView = (TextView) findViewById(R.id.z_textview);
        rateTextView = (TextView) findViewById(R.id.rate_textview);

        discrCheckBox = (CheckBox) findViewById(R.id.discr_checkbox);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(this);
        // TODO: load sound in AsyncTask
        soundPool.load(this, R.raw.square, 1);
    }

    public void onButtonClicked(View v) {
        switch (v.getId()) {
            case R.id.play_button:
                Toast.makeText(this, "Playing", Toast.LENGTH_SHORT).show();
                streamId = soundPool.play(soundId, 1, 1, 0, -1, 1);
                playButton.setEnabled(false);
                stopButton.setEnabled(true);
                break;
            case R.id.stop_button:
                Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
                soundPool.stop(streamId);
                stopButton.setEnabled(false);
                playButton.setEnabled(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        if (status == 0) {
            soundId = sampleId;
            playButton.setEnabled(true);
        } else {
            soundId = -1; // -1 is set to track errors
        }
        Toast.makeText(this, "Loaded soundId " + soundId + " with status " + status, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x;
        if (discrCheckBox.isChecked()) {
            int i = Math.round((event.values[0] + 10) * 0.55f) + 1;
            x = (float) Math.pow(2, i / 12.0);
        } else {
            x = (event.values[0] + 10) / 20.0f + 1;
//            x = (event.values[0] + 10) * 0.075f + 0.5f;
        }
        soundPool.setRate(streamId, x);

        xTextView.setText(String.format(Locale.US, "X: %.2f", event.values[0]));
        yTextView.setText(String.format(Locale.US, "Y: %.2f", event.values[1]));
        zTextView.setText(String.format(Locale.US, "Z: %.2f", event.values[2]));
        rateTextView.setText(String.format(Locale.US, "Rate: %.2f", x));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
