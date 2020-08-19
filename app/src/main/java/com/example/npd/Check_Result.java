package com.example.npd;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Check_Result extends AppCompatActivity {


        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        public String rid;

        public String requestUrl= "http://5aa8bdd6cbb1.ngrok.io/users/";




        public String result;
        public String status;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_check_result);


            SharedPreferences pref4 = getSharedPreferences("SAVE", MODE_PRIVATE);
            rid = pref4.getString("cur_rid", "");

        }

    public void get_test(){
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("5aa8bdd6cbb1.ngrok.io")
                .addPathSegment("users/")
                .addQueryParameter("uid", "\""+currentuser+"\"")
                .addQueryParameter("rid", "\""+rid+"\"")
                .build();

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(httpUrl)
                    .build();

            //비동기 처리 (enqueue 사용)
            client.newCall(request).enqueue(new Callback() {
                //비동기 처리를 위해 Callback 구현
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("Tag","Error Occurred");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i("Result","Success");
                    result = response.body().string();
                    try {
                        JSONObject stat = new JSONObject(result);
                        //ResponseBody test = response.body();
                        status = stat.getString("status");
                        Log.i("Tag", status);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e){
            System.err.println(e.toString());
        }
    }



        public void check_result(View view) {
        // Do something in response to
            get_test();



    }
    public void put_request(View view) {
        // Do something in response to
        Toast.makeText(this,status,Toast.LENGTH_LONG).show();
        //Intent intent = new Intent(Check_Result.this, Put_Request.class);
        //startActivity(intent);

    }
    }


