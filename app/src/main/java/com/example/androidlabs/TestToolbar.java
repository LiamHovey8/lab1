package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class TestToolbar extends AppCompatActivity {
    public String Tosh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar tBar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(tBar);
        Tosh ="You clicked on the overflow menu";
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_test_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.toolbar_overflow:
                Toast.makeText(this, Tosh, Toast.LENGTH_LONG).show();
                break;
            case R.id.toolbar_icon_one:
                alertExample();
                break;
            case R.id.toolbar_icon_two:
                Snackbar sb=Snackbar.make(findViewById(R.id.my_toolbar),"go back?",Snackbar.LENGTH_LONG);
                sb.setAction("finish", v -> finish());
                sb.show();
                break;
            case R.id.toolbar_icon_three:
                Toast.makeText(this, "This is the initial message", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
    public void alertExample()
    {
        View middle = getLayoutInflater().inflate(R.layout.test_toolbar_dialog_box, null);
        EditText et = (EditText)middle.findViewById(R.id.view_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The Message")
                .setPositiveButton("Positive", (dialog, id) -> {

                    String SText =et.getText().toString();
                    Tosh =SText;

                    // What to do on Accept
                })
                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }
}
