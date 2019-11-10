package com.example.androidlabs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
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
    SQLiteDatabase db;
    DatabaseHelper dbOperner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room_activity);
        ListView theList = findViewById(R.id.theList);

        dbOperner =new DatabaseHelper(this);
        db =dbOperner.getWritableDatabase();

        String [] columns={DatabaseHelper.COL_ID,DatabaseHelper.MESSAGE,DatabaseHelper.SENT};
        Cursor results =db.query(DatabaseHelper.TABLE_NAME,columns,null,null,null,null,null);

        db.getVersion();
        int messageColumnIndex = results.getColumnIndex(DatabaseHelper.MESSAGE);
        int sentColumnIndex =results.getColumnIndex(DatabaseHelper.SENT);
        int idColIndex = results.getColumnIndex(DatabaseHelper.COL_ID);
        while(results.moveToNext()){
            boolean sent;
            String message = results.getString(messageColumnIndex);
            if(results.getInt(sentColumnIndex)==1)
                sent = true;
            else
                sent = false;

            long id = results.getLong(idColIndex);

            messageLog.add(new Message(message,sent,id));
        }
        theList.setAdapter( messageAdapter = new MyListAdapter() );
        theList.setOnItemClickListener( ( lv, vw, pos, id) ->{

            Toast.makeText( ChatRoomActivity.this,
                    "You clicked on:" + pos, Toast.LENGTH_SHORT).show();

        } );
        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener( clik -> {
            EditText edit = findViewById(R.id.chatMessage);
            String message = edit.getText().toString();
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(DatabaseHelper.MESSAGE,message);
            newRowValues.put(DatabaseHelper.SENT,1);

            long newID = db.insert(DatabaseHelper.TABLE_NAME,null,newRowValues);
            printCursor(results);
            messageLog.add(new Message(message, true,newID) );
            messageAdapter.notifyDataSetChanged();
            edit.getText().clear();//update yourself
        });
        Button reseveButton = findViewById(R.id.receiveButton);
        reseveButton.setOnClickListener( clik -> {
            EditText edit = findViewById(R.id.chatMessage);
            String message = edit.getText().toString();
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(DatabaseHelper.MESSAGE,message);
            newRowValues.put(DatabaseHelper.SENT,0);


            long newID = db.insert(DatabaseHelper.TABLE_NAME,null,newRowValues);
            printCursor(results);
            messageLog.add(new Message(message, false,newID) );
            messageAdapter.notifyDataSetChanged();
            edit.getText().clear();
        });

    }

    public void printCursor(Cursor c){


        Log.e("Database version number",""+DatabaseHelper.VERSION_NUM);
        Log.e("number of columns","you have "+c.getColumnCount()+" Columns");
        Log.e("name of columns",""+c.getColumnName(0)+", "+c.getColumnName(1)+", "+c.getColumnName(2));
        Log.e("number of results",""+c.getCount()+"");
        c.moveToFirst();
        while(c.moveToNext()) {
            int i;

            i = c.getColumnIndexOrThrow("MESSAGE");
            String message = c.getString(i);

            i = c.getColumnIndexOrThrow("SENT");
            int sent = c.getInt(i);

            i = c.getColumnIndexOrThrow("_id");
            long id = c.getLong(i);

            Log.e("row of results",id+" "+sent+" "+message);

        }


    }
/*    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        public void printCursor(Cursor c){

        }
    }*/

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
            View thisRow;

           // if(convertView == null)
             //   thisRow = getLayoutInflater().inflate(R.layout.reseved_row,null);

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
