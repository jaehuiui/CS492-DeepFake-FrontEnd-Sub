package com.example.npd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public String postUrl= "http://5aa8bdd6cbb1.ngrok.io/users/";
    public String postBody="{\n" +
            "    \"uid\": " + "\""  + currentuser + "\"" + "\n" +
            "}";

    public String requestnum;

    public String reference;


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private static final int PICK_IMAGE_CODE = 1000;
    Button btn_upload;
    ImageView image_view;
    AlertDialog dialog;


    StorageReference storageReference;

    public void nextPage (View view) {
        // Do something in response to
        Intent intent = new Intent(MainActivity.this, UploadDst.class);
        startActivity(intent);
        SharedPreferences pref1 = getSharedPreferences("SAVE", MODE_PRIVATE);
        SharedPreferences.Editor rid;
        if (!pref1.contains("rid1")) {
            rid = pref1.edit();
            rid.putString("rid1", requestnum);
            rid.putString("cur_rid", requestnum);
            rid.commit();
        } else if (!pref1.contains("rid2")) {
            rid = pref1.edit();
            rid.putString("rid2", requestnum);
            rid.putString("cur_rid", requestnum);
            rid.commit();
        } else if (!pref1.contains("rid3")){
            rid = pref1.edit();
            rid.putString("rid3", requestnum);
            rid.putString("cur_rid", requestnum);
            rid.commit();
        }
        else {
            rid = pref1.edit();
            rid.putString("test_rid", requestnum);
            rid.putString("cur_rid", requestnum);
            rid.commit();
        }

    }
    public void postRequest(String postUrl,String postBody) throws IOException {

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
                Log.i("TAG", "response 33: success to post ");
                String response_server = response.body().string();
                try {
                    JSONObject rid = new JSONObject(response_server);
                    //ResponseBody test = response.body();
                    requestnum = rid.getString("rid");
                    Log.i("Tag", requestnum);
                    reference = currentuser + "/" + requestnum + "/src/src.mp4";
                    storageReference = FirebaseStorage.getInstance().getReference("users/" + reference);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_CODE){
            dialog.show();

            UploadTask uploadTask = storageReference.putFile(data.getData());

            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        String url = task.getResult().toString();
                        Log.d("DirectLink",url);
                        Picasso.get().load(url).into(image_view);
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            postRequest(postUrl,postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Tag", postBody);
        Toast.makeText(this,postBody,Toast.LENGTH_LONG).show();

        dialog = new SpotsDialog.Builder().setContext(this).build();
        btn_upload = (Button)findViewById(R.id.btn_upload);
        image_view = (ImageView) findViewById(R.id.image_view);




        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select videos"), PICK_IMAGE_CODE);
                //Log.i("Tag", reference);



            }
        });
    }


}

