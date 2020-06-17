package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static String email;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab_3_layout);

        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        EditText emailEdit = findViewById(R.id.enterEmailEditText);
        Button login = findViewById(R.id.loginButton);


        login.setOnClickListener((v) -> {
            Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
            goToProfile.putExtra("EMAIL", emailEdit.getText().toString());
            startActivity(goToProfile);
            saveSharedPrefs(emailEdit.getText().toString());
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        email = prefs.getString("ReserveName", "Default Value");
        saveSharedPrefs(email);

    }

    private void saveSharedPrefs(String s) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ReserveName", s);
        editor.commit();

    }
}