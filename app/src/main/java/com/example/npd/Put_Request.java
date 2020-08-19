package com.example.npd;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

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

public class Put_Request extends AppCompatActivity {

        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        public String rid;

        public String postUrl= "http://5aa8bdd6cbb1.ngrok.io/upload/";
        public String putBody;

        public String result;


        public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_put);





            SharedPreferences pref5 = getSharedPreferences("SAVE", MODE_PRIVATE);
            rid = pref5.getString("cur_rid", "");

            putBody ="{\n" +
                    "    \"uid\": " + "\""  + currentuser + "\"" + ",\n" +
                    "    \"rid\": " + "\""  + rid + "\"" + ",\n" +
                    "    \"type\": \"dst\"" + ",\n" +
                    "    \"filename\": \"dst.mp4\"" +"\n" +
                    "}";

            Log.i("Tag", putBody);
            Toast.makeText(this,putBody,Toast.LENGTH_LONG).show();
        }

        void postRequest(String postUrl,String putBody) throws IOException {

            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(putBody, JSON);

            Request request = new Request.Builder()
                    .url(postUrl)
                    .put(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("TAG", "onFailure: ");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                        Log.i("TAG", "response 33: "+ response.body().string());
                        //result = response.body().string();
                    //try {
                     //   JSONObject rid = new JSONObject(response_server);
                      //  //ResponseBody test = response.body();
                       // result = rid.getString("status");

//                    } catch (JSONException e) {
  //                      e.printStackTrace();
    //                }

                }

            });
        }
        public void check_put(View view) {
        // Do something in response to
            try {
                postRequest(postUrl,putBody);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    }


