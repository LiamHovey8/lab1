package com.example.androidlabs;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {
    ArrayList<Message> messageLog = new ArrayList<>();
    BaseAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room_activity);
        ListView theList = findViewById(R.id.theList);
        theList.setAdapter( messageAdapter = new MyListAdapter() );
        theList.setOnItemClickListener( ( lv, vw, pos, id) ->{

            Toast.makeText( ChatRoomActivity.this,
                    "You clicked on:" + pos, Toast.LENGTH_SHORT).show();

        } );

    }
    private class MyListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return messageLog.size();
        }

        @Override
        public Object getItem(int position) {
            return messageLog.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View thisRow = convertView;

            if(convertView == null)
                thisRow = getLayoutInflater().inflate(R.layout.,null);
        }
    }

}
