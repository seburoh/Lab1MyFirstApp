package edu.uw.tcss450.jrdeal.lab1myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG,"This is a verbose written illustration using no pictures to" +
                "describe that the activity of description MainActivity has successfully" +
                "been created. Following this there will be free coffee in the lounge.");

        Log.i(TAG,getIntent().toString());
        Log.i(TAG,getIntent().getAction());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"On Start. Information given.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"Log error message, why did you want to come back to this app?");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"Debug the bug of the Dutch.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG,"Warning, Warning Will Robinson.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG,"Restart the voyage me captain.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"Accidental deletion of System32.");
    }
}