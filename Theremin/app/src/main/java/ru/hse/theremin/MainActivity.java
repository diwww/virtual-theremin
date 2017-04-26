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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
        CompoundButton.OnCheckedChangeListener,
        View.OnClickListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private Button playButton, stopButton;
    private TextView idxTextView;
    private RadioGroup waveRadioGroup;
    private RadioGroup octaveRadioGroup;
    private RadioGroup playModeRadioGroup;
    private CheckBox lockCheckBox;
    private TextView lockNoteIdTextView;
    AudioPlayer audioPlayer = new AudioPlayer();
    private boolean lockNotesFlag = false;
    // Dark synth
    // private int[] lockNotes = {0, 5, 3, 1};
    // Чижик - пыжик
    // private int[] lockNotes = {10, 6, 10, 6, 11, 10, 8, 1, 1, 3, 5, 6, 6, 6};
    // Гамма
    private int[] lockNotes = {0, 2, 4, 5, 7, 9, 11, 12};
    private int lockNoteId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Linking views
        playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(this);
        stopButton = (Button) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(this);
        idxTextView = (TextView) findViewById(R.id.idx_textview);
        waveRadioGroup = (RadioGroup) findViewById(R.id.wave_radio_group);
        waveRadioGroup.setOnCheckedChangeListener(this);
        waveRadioGroup.check(R.id.sine_radio_button);
        octaveRadioGroup = (RadioGroup) findViewById(R.id.octave_radio_group);
        octaveRadioGroup.check(R.id.one_octave_radio_button);
        playModeRadioGroup = (RadioGroup) findViewById(R.id.play_mode_radio_group);
        playModeRadioGroup.setOnCheckedChangeListener(this);
        playModeRadioGroup.check(R.id.one_note_radio_button);
        lockCheckBox = (CheckBox) findViewById(R.id.lock_checkbox);
        lockCheckBox.setOnCheckedChangeListener(this);
        lockNoteIdTextView = (TextView) findViewById(R.id.lock_note_id_textview);
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

        if (lockNotesFlag) {
            if (lockNotes[lockNoteId] == index) {
                audioPlayer.setFreq(index);
                lockNoteId = (lockNoteId + 1) % lockNotes.length;
                lockNoteIdTextView.setText(String.format(Locale.US, "Next note: %d", lockNotes[lockNoteId]));
            }
        } else {
            audioPlayer.setFreq(index);
        }

        idxTextView.setText(String.format(Locale.US, "Index: %d", index));
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        lockNotesFlag = isChecked;
        if (lockNotesFlag) {
            lockNoteId = 0;
            lockNoteIdTextView.setText(String.format(Locale.US, "Next note: %d", lockNotes[lockNoteId]));
        } else {
            lockNoteIdTextView.setText("");
        }
    }
}
