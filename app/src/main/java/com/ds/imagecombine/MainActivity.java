package com.ds.imagecombine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Vector;


public class MainActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1;
    private Vector<Bitmap> all_images;
    Bitmap image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        all_images = new Vector<Bitmap>();

        Button openGallery = findViewById(R.id.add_image);
        Button saveHorizontal = findViewById(R.id.save_horizontal);
        Button saveVertical = findViewById(R.id.save_vertical);



        openGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        saveHorizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the total width of the images added
                // Get the largest height out of the images added
                Bitmap imageToSave = Bitmap.createBitmap(getTotalWidth(), getMaximumSizeVertical(), Bitmap.Config.ARGB_8888);
            }
        });

        saveVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the total height of the images added
                // Get the largest width out of the images added
                Bitmap imageToSave = Bitmap.createBitmap(getMaximumSizeHorizontal(), getTotalHeight(), Bitmap.Config.ARGB_8888);
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

    private int getMaximumSizeVertical() {
        int maxVertical = 0;

        return maxVertical;
    }

    private int getMaximumSizeHorizontal() {
        int maxHorizontal= 0;

        return maxHorizontal;
    }

    private int getTotalHeight() {
        int totalHeight = 0;

        return totalHeight;
    }

    private int getTotalWidth() {
        int totalWidth = 0;

        return totalWidth;
    }

    private void readDataExternal() {
        runOnUiThread(new Runnable() {
            public void run() {


                ImageView imgView = new ImageView(MainActivity.this);
                image=Bitmap.createScaledBitmap(image, 200,200, true);
                imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                LinearLayout lp = findViewById(R.id.image_holder);
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