package com.example.npd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PornDetect extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();

    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public String rid;

    public String postUrl= "http://5aa8bdd6cbb1.ngrok.io/media/";
    public String putUrl= "http://5aa8bdd6cbb1.ngrok.io/upload/";
    public String postBody;
    public String postBody_src;
    public String postBody_dst;
    public String result_src;
    public String answer_src;
    public String result_dst;
    public String answer_dst;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);

        SharedPreferences pref4 = getSharedPreferences("SAVE", MODE_PRIVATE);
        rid = pref4.getString("cur_rid", "");

        postBody ="{\n" +
                "    \"uid\": " + "\""  + currentuser + "\"" + ",\n" +
                "    \"rid\": " + "\""  + rid + "\"" + ",\n" +
                "    \"name\": \"out.mp4\"" + "\n" +
                "}";

        postBody_src ="{\n" +
                "    \"uid\": " + "\""  + currentuser + "\"" + ",\n" +
                "    \"rid\": " + "\""  + rid + "\"" + ",\n" +
                "    \"type\": \"src\"" + ",\n" +
                "    \"filename\": \"src.mp4\"" +"\n" +
                "}";

        postBody_dst ="{\n" +
                "    \"uid\": " + "\""  + currentuser + "\"" + ",\n" +
                "    \"rid\": " + "\""  + rid + "\"" + ",\n" +
                "    \"type\": \"dst\"" + ",\n" +
                "    \"filename\": \"dst.mp4\"" +"\n" +
                "}";

        Log.i("Tag", postBody);
        Toast.makeText(this,postBody,Toast.LENGTH_LONG).show();

    }

    void postRequest(String postUrl,String postBody) throws IOException {

        OkHttpClient client1 = client.newBuilder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .build();

        RequestBody body = RequestBody.create(postBody, JSON);

        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();

        client1.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("TAG", "response 33: "+ response.body().string());

            }

        });
    }

    void putRequest_src(String putUrl,String postBody_src) throws IOException {

        OkHttpClient client2 = client.newBuilder()
                .readTimeout(300000, TimeUnit.MILLISECONDS)
                .build();

        RequestBody body = RequestBody.create(postBody_src, JSON);

        Request request = new Request.Builder()
                .url(putUrl)
                .post(body)
                .build();

        client2.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("TAG", "response 33: "+ response.body().string());
                result_src = response.body().string();
                try {
                   JSONObject rid = new JSONObject(result_src);
                   ResponseBody test = response.body();
                   answer_src = rid.getString("status");
                   TextView myTextView_src = (TextView) findViewById(R.id.src_check);
                   myTextView_src.setText(answer_src);

                    } catch (JSONException e) {
                                      e.printStackTrace();
                                }

            }

        });
    }

    void putRequest_dst(String putUrl,String postBody_dst) throws IOException {

        OkHttpClient client3 = client.newBuilder()
                .readTimeout(300000, TimeUnit.MILLISECONDS)
                .build();

        RequestBody body = RequestBody.create(postBody_dst, JSON);

        Request request = new Request.Builder()
                .url(putUrl)
                .post(body)
                .build();

        client3.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("TAG", "response 33: "+ response.body().string());
                result_dst = response.body().string();
                try {
                    JSONObject rid = new JSONObject(result_dst);
                    ResponseBody test = response.body();
                    answer_dst = rid.getString("status");
                    TextView myTextView_dst = (TextView) findViewById(R.id.dst_check);
                    myTextView_dst.setText(answer_dst);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    public void src(View view){
        try {
            putRequest_src(putUrl,postBody_src);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dst(View view){
        try {
            putRequest_dst(putUrl,postBody_dst);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start(View view){
        //Toast.makeText(this, postBody, Toast.LENGTH_LONG).show();
        try {
            postRequest(postUrl,postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(PornDetect.this, MainPage.class);
        startActivity(intent);
    }
}
