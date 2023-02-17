package com.ds.imagecombine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1;
    ImageView imageView;
    Bitmap image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button openGallery = findViewById(R.id.add_image);



        openGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    readDataExternal();
                }
                break;

            default:
                break;
        }
    }

    public void readDataExternal() {
        runOnUiThread(new Runnable() {
            public void run() {


                ImageView imgView = new ImageView(MainActivity.this);
                image=Bitmap.createScaledBitmap(image, 200,200, true);
                imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                LinearLayout lp = findViewById(R.id.cardview_holder);
                lp.addView(imgView);

                //imgView.setImageDrawable(null);
                imgView.setImageBitmap(image);

                imgView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        });
    }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {

            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                Uri photoUri = data.getData();
                if (photoUri != null) {
                    try {
                        image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);

                        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
                        } else {
                            readDataExternal();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }


}