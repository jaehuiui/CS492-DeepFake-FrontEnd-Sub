package com.example.npd;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class upload_first extends AppCompatActivity {

        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        public String rid;

        public String postUrl= "http://5aa8bdd6cbb1.ngrok.io/media/";
        public String postBody;

        public String result;


        public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.first_upload);



            SharedPreferences pref4 = getSharedPreferences("SAVE", MODE_PRIVATE);
            rid = pref4.getString("cur_rid", "");

            postBody ="{\n" +
                    "    \"uid\": " + "\""  + currentuser + "\"" + ",\n" +
                    "    \"rid\": " + "\""  + rid + "\"" + ",\n" +
                    "    \"src\": \"src.mp4\"" + ",\n" +
                    "    \"dst\": \"dst.mp4\"" + ",\n" +
                    "    \"result\": \"test\"" + "\n" +
                    "}";

            Log.i("Tag", postBody);
            Toast.makeText(this,postBody,Toast.LENGTH_LONG).show();


        }

        void postRequest(String postUrl,String postBody) throws IOException {

            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(postBody, JSON);

            Request request = new Request.Builder()
                    .url(postUrl)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("TAG", "onFailure: ");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                        Log.i("TAG", "response 33: "+ response.body().string());
                        //String response_server = response.body().string();
                    //try {
                        //JSONObject rid = new JSONObject(response_server);
                        //ResponseBody test = response.body();
                        //result = rid.getString("status");

                    //} catch (JSONException e) {
                    //   e.printStackTrace();
                    //}

                }

            });
        }
        public void rid_check(View view) {
        // Do something in response to
            Toast.makeText(this, postBody, Toast.LENGTH_LONG).show();
            try {
                postRequest(postUrl,postBody);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    }


