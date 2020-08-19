package com.example.npd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainPage extends AppCompatActivity {

    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }
    public void user_info(View view){
        Intent intent = new Intent(MainPage.this, MainActivity_All.class);
        startActivity(intent);
        Toast.makeText(this, "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
    }
    public void upload_video(View view){
        Intent intent = new Intent(MainPage.this, MainActivity.class);
        startActivity(intent);
    }
    public void rid_test(View view){
        Intent intent = new Intent(MainPage.this, Check_Result.class);
        startActivity(intent);
    }
    public void download(View view){
        Intent intent = new Intent(MainPage.this,
                Download.class);
        startActivity(intent);
    }
}
