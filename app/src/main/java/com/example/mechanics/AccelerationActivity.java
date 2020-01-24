package com.example.mechanics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.hardware.Sensor.TYPE_LINEAR_ACCELERATION;

public class AccelerationActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView sensorTextView;
    private TextView sensorValue;
    private TextView best;
    private TextView bestLabel;
    private float bestV=(float)0.0;
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceleration);

        if (savedInstanceState != null){
            bestV = savedInstanceState.getFloat(KEY_CURRENT_INDEX);
        }

        sensorTextView = findViewById(R.id.sensor_name2);
        sensorValue = findViewById(R.id.sensor_value);
        best = findViewById(R.id.best);
        bestLabel = findViewById(R.id.best_label);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(TYPE_LINEAR_ACCELERATION);


        if(sensor == null){
            sensorTextView.setText(R.string.missing_sensor);
        }
    }

    //@Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        float currentValue = event.values[0];

        switch (sensorType){
            case TYPE_LINEAR_ACCELERATION:
                sensorTextView.setText(getResources().getString(R.string.sensor_label));
                sensorValue.setText(Float.toString(Math.abs((float) (Math.round(currentValue * 1000.0) / 1000.0))));
                bestLabel.setText(getResources().getString(R.string.best_label));
                best.setText(Float.toString(bestV));
                if(Math.abs((float) (Math.round(currentValue * 1000.0) / 1000.0))>bestV){
                    bestV=Math.abs((float) (Math.round(currentValue * 1000.0) / 1000.0));
                    bestLabel.setText(getResources().getString(R.string.best_label));
                    best.setText(Float.toString(bestV));
                }
                break;
            default:
        }
    }

    //@Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStart(){
        super.onStart();
        if(sensor != null){
            sensorManager.registerListener((SensorEventListener) this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    protected void onStop(){
        super.onStop();
        sensorManager.unregisterListener((SensorEventListener)this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putFloat(KEY_CURRENT_INDEX, bestV);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bestV = savedInstanceState.getFloat(KEY_CURRENT_INDEX);
    }
}
