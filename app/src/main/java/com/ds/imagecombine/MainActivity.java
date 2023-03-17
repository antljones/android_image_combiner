package com.ds.imagecombine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
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
    private String alignment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        all_images = new Vector<Bitmap>();
        alignment = new String();

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
                alignment = "HORIZONTAL";
                generateBitmap();

            }
        });

        saveVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the total height of the images added
                // Get the largest width out of the images added
                alignment = "VERTICAL";
                generateBitmap();
            }
        });
    }

    //TODO add error for alignment not VERTICAL OR HORIZONTAL
    public void generateBitmap() {
        Bitmap imageToSave = null;

        if ( alignment.contentEquals("HORIZONTAL")) {
            imageToSave = Bitmap.createBitmap(getTotalWidth(), getTotalHeight(), Bitmap.Config.ARGB_8888);
        } else {
            if ( alignment.contentEquals("VERTICAL")) {
                imageToSave = Bitmap.createBitmap( getTotalWidth(), getTotalHeight(), Bitmap.Config.ARGB_8888);
            }
        }

        Bitmap blackBitmap = changeBitmapColor(imageToSave, 1);

    }

    public static Bitmap changeBitmapColor(Bitmap sourceBitmap, int color)
    {
        Bitmap resultBitmap = sourceBitmap.copy(sourceBitmap.getConfig(),true);
        Paint paint = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        paint.setColorFilter(filter);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);
        return resultBitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    addImageToView();
                }
                break;

            default:
                break;
        }
    }

    //TODO add error for alignment not VERTICAL OR HORIZONTAL
    private int getTotalHeight() {
        int totalHeight = 0;

        if (alignment.contentEquals("HORIZONTAL") ) {
            for (Bitmap tempImage : all_images) {
                if(tempImage.getHeight() > totalHeight) {
                    totalHeight = tempImage.getHeight();
                }
            }
        } else {
            if (alignment.contentEquals("VERTICAL") ) {
                for (Bitmap tempImage : all_images) {
                    totalHeight += tempImage.getHeight();
                }
            }
        }

        return totalHeight;
    }

    //TODO add error for alignment not VERTICAL OR HORIZONTAL
    private int getTotalWidth() {
        int totalWidth = 0;

        if (alignment.contentEquals("HORIZONTAL") ) {
            for (Bitmap tempImage : all_images) {
                totalWidth = tempImage.getWidth();
            }
        } else {
            if (alignment.contentEquals("VERTICAL") ) {
                for (Bitmap tempImage : all_images) {
                    totalWidth += tempImage.getHeight();
                }
            }
        }

        return totalWidth;
    }

    private void addImageToView() {
        runOnUiThread(new Runnable() {
            public void run() {

                LinearLayout lp = findViewById(R.id.image_holder);
                ImageView imgView = new ImageView(MainActivity.this);
                image=Bitmap.createScaledBitmap(image, lp.getWidth(),lp.getWidth(), true);
                imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
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
                        addImageToView();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}