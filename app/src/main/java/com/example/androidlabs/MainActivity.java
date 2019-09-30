package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    protected SharedPreferences prefs;
    protected SharedPreferences.Editor edit;
    protected EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        prefs = getSharedPreferences("ReserveName", MODE_PRIVATE);
        editText = findViewById(R.id.email);
        edit = prefs.edit();

        String previous = prefs.getString("ReserveName","");
        editText.setText(previous);


        Button login = findViewById(R.id.button1);
        if(login != null) {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View clk) {
                    Intent goToProfileActivity = new Intent(MainActivity.this,
                            ProfileActivity.class);
                    goToProfileActivity.putExtra("email", editText.getText().toString());
                    MainActivity.this.startActivityForResult(goToProfileActivity, 30);
                }
            });
        }



    }
    @Override
    protected void onPause() {
        super.onPause();


        edit.putString("ReserveName", editText.getText().toString());


        edit.commit();}

}
