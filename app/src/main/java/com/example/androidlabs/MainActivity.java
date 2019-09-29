package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefFile = "com.example.android.hellosharedprefs";
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        setContentView(R.layout.activity_main3);

        Button login = findViewById(R.id.button1);
        if(login != null)
            login.setOnClickListener(v -> {
                Intent goToProfileActivity = new Intent(MainActivity.this,
                        ProfileActivity.class);
                    startActivity(goToProfileActivity);
            });

    }
    @Override
    protected void onPause() {
        super.onPause();
        EditText editText = findViewById(R.id.email);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("ReserveName", editText.getText().toString());

        preferencesEditor.apply();}

}
