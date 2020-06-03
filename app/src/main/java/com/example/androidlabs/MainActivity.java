package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);

        TextView myTextView = findViewById(R.id.myText);

        Button btn = findViewById(R.id.clickHereButton);
        btn.setOnClickListener(
                (v)->
                        Toast.makeText(this, this.getString(R.string.toast_message)  , Toast.LENGTH_LONG).show());

        CheckBox cB = findViewById(R.id.checkBox);
        cB.setOnCheckedChangeListener((view1, isChecked) ->{
                    //                https://stackoverflow.com/questions/31090754/android-support-library-snackbar-with-indefinite-length
                    if (isChecked) {
                        Snackbar.make(cB, this.getString(R.string.snackOn), Snackbar.LENGTH_LONG).setAction(this.getString(R.string.undo), view2 -> cB.setChecked(false)).show();
                    } else{
                        Snackbar.make(cB, this.getString(R.string.snackOff), Snackbar.LENGTH_LONG).setAction(this.getString(R.string.undo), view2 -> cB.setChecked(true)).show();
            }
        });

        Switch sw = findViewById(R.id.sw);
        sw.setOnCheckedChangeListener((view1, isChecked) ->{
            if (isChecked) {
                Snackbar.make(sw, this.getString(R.string.snackOn), Snackbar.LENGTH_LONG).setAction(this.getString(R.string.undo), view2 -> sw.setChecked(false)).show();
            } else{
                Snackbar.make(sw, this.getString(R.string.snackOff), Snackbar.LENGTH_LONG).setAction(this.getString(R.string.undo), view2 -> sw.setChecked(true)).show();
            }
        });

        ImageButton iButton = findViewById(R.id.imageButton);


    }
}