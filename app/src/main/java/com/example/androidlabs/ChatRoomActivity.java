package com.example.androidlabs;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener( clik -> {
            EditText edit = findViewById(R.id.chatMessage);
            String message = edit.getText().toString();
            messageLog.add(new Message(message, true) );
            messageAdapter.notifyDataSetChanged();
            edit.getText().clear();//update yourself
        });
        Button reseveButton = findViewById(R.id.receiveButton);
        reseveButton.setOnClickListener( clik -> {
            EditText edit = findViewById(R.id.chatMessage);
            String message = edit.getText().toString();
            messageLog.add(new Message(message, false) );
            messageAdapter.notifyDataSetChanged();
            edit.getText().clear();
        });

    }
    private class MyListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return messageLog.size();
        }

        @Override
        public Message getItem(int position) {
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
                thisRow = getLayoutInflater().inflate(R.layout.reseved_row,null);

            Message rowMessage = getItem(position);
            if(rowMessage.getSent()){
                thisRow = getLayoutInflater().inflate(R.layout.sent_row,null);
                TextView itemText = thisRow.findViewById(R.id.chatLogMessage);
                itemText.setText(rowMessage.getMessage());

            }
            else{
                thisRow = getLayoutInflater().inflate(R.layout.reseved_row,null);
                TextView itemText = thisRow.findViewById(R.id.chatLogMessage);
                itemText.setText(rowMessage.getMessage());
            }



            return thisRow;
        }
    }

}
