package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    ImageButton mImageButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(ACTIVITY_NAME, "In function: onCreate");
        setContentView(R.layout.activity_profile);

        EditText emailEntry = findViewById(R.id.enterEmailEditText);
        mImageButton = findViewById(R.id.pictureButton);

        Intent fromMain = getIntent();
        emailEntry.setText(fromMain.getStringExtra("EMAIL"));

        mImageButton.setOnClickListener((v) -> {
                    this.dispatchTakePictureIntent();
                    this.onActivityResult(REQUEST_IMAGE_CAPTURE, RESULT_OK, fromMain);
                }
        );

        Button chatButton = findViewById(R.id.chatButton);
        chatButton.setOnClickListener((v) -> {
            Intent goToChat = new Intent(ProfileActivity.this, ChatRoomActivity.class);
            startActivity(goToChat);
        });

        Button weatherButton = findViewById(R.id.weatherButton);
        weatherButton.setOnClickListener((v) -> {
            Intent goToWeather = new Intent(ProfileActivity.this, WeatherForecast.class);
            startActivity(goToWeather);
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(ACTIVITY_NAME, "In function: onActivityResult");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function: onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function: onDestroy");
    }
}