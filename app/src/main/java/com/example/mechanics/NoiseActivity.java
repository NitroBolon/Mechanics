package com.example.mechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class NoiseActivity extends AppCompatActivity {

    TextView mStatusView;
    TextView best;
    MediaRecorder mRecorder;
    Thread runner;
    private double bestV=0.0;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final int RECORD_AUDIO = 0;

    final Runnable updater = new Runnable(){

        public void run(){
            updateTv();
        }
    };
    final Handler mHandler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise);

        if (savedInstanceState != null){
            bestV = savedInstanceState.getFloat(KEY_CURRENT_INDEX);
        }

        mStatusView = findViewById(R.id.status);
        best = findViewById(R.id.best);
        if (ActivityCompat.checkSelfPermission(NoiseActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(NoiseActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO);

        } else {

        if (runner == null)
        {
            runner = new Thread(){
                public void run()
                {
                    while (runner != null)
                    {
                        try
                        {
                            Thread.sleep(200);
                            Log.i("Noise", "Tock");
                        } catch (InterruptedException e) { };
                        mHandler.post(updater);
                    }
                }
            };
            runner.start();
            Log.d("Noise", "start runner()");
        }
        }
    }

    public void onResume()
    {
        super.onResume();
        startRecorder();
    }

    public void onPause()
    {
        super.onPause();
        stopRecorder();
    }

    public void startRecorder(){
        if (mRecorder == null)
        {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try
            {
                mRecorder.prepare();
            }catch (java.io.IOException ioe) {
                android.util.Log.e("Noise", "IOException: " +
                        android.util.Log.getStackTraceString(ioe));
                Toast.makeText(NoiseActivity.this, R.string.noise_fail, Toast.LENGTH_SHORT).show();

            }catch (java.lang.SecurityException e) {
                android.util.Log.e("Noise", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
                Toast.makeText(NoiseActivity.this, R.string.noise_fail, Toast.LENGTH_SHORT).show();
            }
            try
            {
                mRecorder.start();
            }catch (java.lang.SecurityException e) {
                android.util.Log.e("Noise", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
                Toast.makeText(NoiseActivity.this, R.string.noise_fail, Toast.LENGTH_SHORT).show();
            }

        }

    }
    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
        android.util.Log.e("Noise", "onStop");
    }

    public void updateTv(){
        double xD=getAmplitudeEMA()/200;
        mStatusView.setText(Double.toString(Math.round(xD*1000)/1000));
        if(xD>bestV){
            bestV=xD;
        }
        best.setText(Double.toString(Math.round(bestV*1000)/1000));
    }
    public double soundDb(double ampl){
        return  20 * Math.log10(getAmplitudeEMA() / ampl);
    }
    public double getAmplitude() {
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude());
        else
            return 0;
    }
    public double getAmplitudeEMA() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putDouble(KEY_CURRENT_INDEX, bestV);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bestV = savedInstanceState.getDouble(KEY_CURRENT_INDEX);
    }
}
