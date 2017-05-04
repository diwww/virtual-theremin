package ru.hse.theremin;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import ru.hse.theremin.synthesizer.AudioPlayer;
import ru.hse.theremin.synthesizer.SineWave;
import ru.hse.theremin.synthesizer.TriangleWave;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener,
        RadioGroup.OnCheckedChangeListener,
        View.OnClickListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private Button playButton, stopButton, lockButton;
    private TextView idxTextView, lockTextView;
    private RadioGroup waveRadioGroup, octaveRadioGroup, playModeRadioGroup;
    AudioPlayer audioPlayer = new AudioPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Linking views
        playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(this);
        stopButton = (Button) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(this);
        lockButton = (Button) findViewById(R.id.lock_button);
        idxTextView = (TextView) findViewById(R.id.idx_textview);
        lockTextView = (TextView) findViewById(R.id.lock_textview);
        waveRadioGroup = (RadioGroup) findViewById(R.id.wave_radio_group);
        waveRadioGroup.setOnCheckedChangeListener(this);
        waveRadioGroup.check(R.id.sine_radio_button);
        octaveRadioGroup = (RadioGroup) findViewById(R.id.octave_radio_group);
        octaveRadioGroup.check(R.id.one_octave_radio_button);
        playModeRadioGroup = (RadioGroup) findViewById(R.id.play_mode_radio_group);
        playModeRadioGroup.setOnCheckedChangeListener(this);
        playModeRadioGroup.check(R.id.one_note_radio_button);
        // Sensors initialization
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        int index = 0;

        if (octaveRadioGroup.getCheckedRadioButtonId() == R.id.one_octave_radio_button) {
            // For one octave (more space to rotate)
            index = (int) Math.round((event.values[0] + 10) * 12.0 / 20.0);
        } else if (octaveRadioGroup.getCheckedRadioButtonId() == R.id.two_octaves_radio_button) {
            // For two octaves (more notes are available)
            index = (int) Math.round(event.values[0] * 1.2);
        }

        if (!lockButton.isPressed()) {
            audioPlayer.setFreq(index);
        }
        idxTextView.setText(String.format(Locale.US, "Index: %d", index));
        // TODO: Может стоит сделать листнер для lockButton, а не проверять это здесь
        // (хотя сенсоры срабатывают очень часто)
        lockTextView.setText(String.format(Locale.US, "Lock status: %s", lockButton.isPressed() ? "ON" : "OFF"));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_button:
                Toast.makeText(this, "Playing", Toast.LENGTH_SHORT).show();
                new SynthAsyncTask().execute(audioPlayer);
                playButton.setEnabled(false);
                stopButton.setEnabled(true);
                break;
            case R.id.stop_button:
                Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
                audioPlayer.stop();
                stopButton.setEnabled(false);
                playButton.setEnabled(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (group.getId() == R.id.wave_radio_group) {
            switch (checkedId) {
                case R.id.sine_radio_button:
                    audioPlayer.setWave(new SineWave());
                    break;
                case R.id.triangle_radio_button:
                    audioPlayer.setWave(new TriangleWave());
                    break;
                default:
                    break;
            }
        } else if (group.getId() == R.id.play_mode_radio_group) {
            switch (checkedId) {
                case R.id.one_note_radio_button:
                    audioPlayer.setPlayMode(AudioPlayer.PlayMode.OneNote);
                    break;
                case R.id.two_notes_radio_button:
                    audioPlayer.setPlayMode(AudioPlayer.PlayMode.TwoNotes);
                    break;
                case R.id.major_chord_radio_button:
                    audioPlayer.setPlayMode(AudioPlayer.PlayMode.MajorChord);
                    break;
                case R.id.minor_chord_radio_button:
                    audioPlayer.setPlayMode(AudioPlayer.PlayMode.MinorChord);
                    break;
                default:
                    break;
            }
        }
    }
}
