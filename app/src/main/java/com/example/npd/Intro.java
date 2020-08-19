package com.example.npd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Intro extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
    }

    public void getStarted(View view) {
        // Do something in response to
        Intent intent = new Intent(Intro.this, LoginActivity.class);
        startActivity(intent);
    }

}
