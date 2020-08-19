package com.example.npd;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadDst extends AppCompatActivity {

    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public String reference;

    public String rid;




    private static final int PICK_IMAGE_CODE = 1000;
    Button btn_dst;
    ImageView image_view_2;
    AlertDialog dialog;
    StorageReference storageReference;

    public void dstend (View view) {
        // Do something in response to
        Intent intent = new Intent(UploadDst.this, PornDetect.class);
        startActivity(intent);

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
                        Toast.makeText(UploadDst.this,"Failed",Toast.LENGTH_SHORT).show();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        String url = task.getResult().toString();
                        Log.i("Rid", rid);
                        Log.d("DirectLink",url);
                        Picasso.get().load(url).into(image_view_2);
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dst);

        dialog = new SpotsDialog.Builder().setContext(this).build();
        btn_dst = (Button)findViewById(R.id.btn_dst);
        image_view_2 = (ImageView) findViewById(R.id.image_view_2);

        SharedPreferences pref3 = getSharedPreferences("SAVE", MODE_PRIVATE);
        rid = pref3.getString("cur_rid", "");

        Toast.makeText(this, rid, Toast.LENGTH_LONG);

        storageReference = FirebaseStorage.getInstance().getReference("users/" + currentuser + "/" + rid + "/dst/dst.mp4");

        btn_dst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("video/* image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select videos"), PICK_IMAGE_CODE);


            }
        });
    }


}

