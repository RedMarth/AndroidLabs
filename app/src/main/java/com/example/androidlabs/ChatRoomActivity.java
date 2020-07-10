package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import android.view.LayoutInflater;
import android.widget.TextView;

public class ChatRoomActivity extends AppCompatActivity {
    private ArrayList<Message> elements = new ArrayList<>();
    private MyListAdapter aListAdapter;
    SQLiteDatabase db;
    int positionClicked = 0;
    private static int ACTIVITY_VIEW_MESSAGE = 33;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Button send = findViewById(R.id.sendButton);
        Button receive = findViewById(R.id.receiveButton);
        EditText typeMessage = findViewById(R.id.typeHere);


        loadDataFromDatabase();

        send.setOnClickListener(btn -> {
                    String message = typeMessage.getText().toString();
                    int isSend = 1;

                    ContentValues newRowValues = new ContentValues();
                    newRowValues.put(MyOpener.COL_MESSAGE, message);
                    newRowValues.put(MyOpener.COL_SEND, isSend);

                    long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

                    Message sendMessage = new Message(typeMessage.getText().toString(), 1, newId);
                    elements.add(sendMessage);
                    aListAdapter.notifyDataSetChanged();
                    typeMessage.getText().clear();
                }
        );

        receive.setOnClickListener(btn -> {
            String message = typeMessage.getText().toString();
            int isSend = 0;

            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_MESSAGE, message);
            newRowValues.put(MyOpener.COL_SEND, isSend);

            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            Message sendMessage = new Message(typeMessage.getText().toString(), 0, newId);
            elements.add(sendMessage);
            aListAdapter.notifyDataSetChanged();
            typeMessage.getText().clear();
        });

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(aListAdapter = new MyListAdapter());
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            Message message = elements.get(position);
            alertDialogBuilder.setTitle("Do you want to delete this?");

            //What is the message:
            alertDialogBuilder.setMessage("The selected row is: " + position + "\n\nThe database id is: " + id);

            //What the yes button does:
            alertDialogBuilder.setPositiveButton("Yes", (click, arg) -> {
                elements.remove(position);
                deleteMessage(message);
                aListAdapter.notifyDataSetChanged();
            });

            //What the No button does:
            alertDialogBuilder.setNegativeButton("No", (click, arg) -> {

            });

            //Show the dialog:
            alertDialogBuilder.create().show();

            return true;
        });
    }

    private void loadDataFromDatabase() {
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String[] columns = {MyOpener.COL_ID, MyOpener.COL_MESSAGE, MyOpener.COL_SEND};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);


        //Now the results object has rows of results that match the query.
        //find the column indices:
        int messageColumnIndex = results.getColumnIndex(MyOpener.COL_MESSAGE);
        int isSendColIndex = results.getColumnIndex(MyOpener.COL_SEND);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            int isSend = results.getInt(isSendColIndex);
            String message = results.getString(messageColumnIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            elements.add(new Message(message, isSend, id));
        }

        //At this point, the contactsList array has loaded every row from the cursor.
        printCursor(results, db.getVersion());
    }

    protected void updateMessage(Message message) {
        //Create a ContentValues object to represent a database row:
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(MyOpener.COL_SEND, message.isSend());
        updatedValues.put(MyOpener.COL_MESSAGE, message.getTextMessage());

        //now call the update function:
        db.update(MyOpener.TABLE_NAME, updatedValues, MyOpener.COL_ID + "= ?", new String[]{Long.toString(message.getId())});
    }

    protected void deleteMessage(Message message) {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[]{Long.toString(message.getId())});
    }

    protected void printCursor(Cursor c, int version) {
        if (c.moveToFirst()) {
            Log.v("Version Number", String.valueOf(db.getVersion()));
            Log.v("Cursor Number Columns", String.valueOf(c.getColumnCount()));
            Log.v("Cursor Column Names", Arrays.toString(c.getColumnNames()));
            Log.v("Cursor Number Rows", String.valueOf(c.getCount()));
            Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
        }
    }


    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return elements.size();
        }

        public Message getItem(int position) {
            return elements.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }


        public View getView(int position, View old, ViewGroup parent) {
            Message msg = (Message) getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View newView;

            if (msg.isSend() == 1) {
                newView = inflater.inflate(R.layout.send_layout, parent, false);
            } else newView = inflater.inflate(R.layout.receive_layout, parent, false);

            //set what the text should be for this row:
            TextView tView = (TextView) newView.findViewById(R.id.textGoesHere);
            tView.setText(getItem(position).toString());

            //return it to be put in the table
            return newView;
        }
    }

    private class Message {
        private String textMessage;
        private int isSend;
        private long id;

        public Message(String textMessage, int isSend, long id) {
            this.textMessage = textMessage;
            this.isSend = isSend;
            this.id = id;
        }

        public Message(String textMessage, int isSend) {
            this(textMessage, isSend, 0);
        }

        public String getTextMessage() {
            return textMessage;
        }

        public int isSend() {
            return isSend;
        }

        public void setTextMessage(String textMessage) {
            this.textMessage = textMessage;
        }

        public void setSend(int send) {
            isSend = send;
        }

        @Override
        public String toString() {
            return textMessage;
        }

        public long getId() {
            return id;
        }
    }

    public class MyOpener extends SQLiteOpenHelper {

        protected final static String DATABASE_NAME = "MessageDB";
        protected final static int VERSION_NUM = 1;
        public final static String TABLE_NAME = "MESSAGES";
        public final static String COL_MESSAGE = "MESSAGE";
        public final static String COL_SEND = "SEND";
        public final static String COL_ID = "_id";

        public MyOpener(Context ctx) {
            super(ctx, DATABASE_NAME, null, VERSION_NUM);
        }


        //This function gets called if no database file exists.
        //Look on your device in the /data/data/package-name/database directory.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_MESSAGE + " text,"
                    + COL_SEND + " text);");  // add or remove columns
        }

        //this function gets called if the database version on your device is lower than VERSION_NUM
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {   //Drop the old table:
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            //Create the new table:
            onCreate(db);
        }


        //this function gets called if the database version on your device is higher than VERSION_NUM
        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {   //Drop the old table:
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            //Create the new table:
            onCreate(db);
        }

    }

//    private void getAll() {
//        MyOpener dbHelper = new MyOpener(this);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        String columns[] = {MyOpener.COL_ID, MyOpener.COL_MESSAGE, MyOpener.COL_SEND};
//        Cursor c = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);
//
//        int msgColIndex = c.getColumnIndex(MyOpener.COL_MESSAGE);
//        int sendColIndex = c.getColumnIndex(MyOpener.COL_SEND);
//        int idColIndex = c.getColumnIndex(MyOpener.COL_ID);
//
//        //iterate over the results, return true if there is a next item:
//        while (c.moveToNext()) {
//            String name = c.getString(msgColIndex);
//            boolean email = c.getInt(sendColIndex) == 1;
//            long id = c.getLong(idColIndex);
//
//            //add the new Contact to the array list:
//            elements.add(new Message(name, email, id));
//        }
//    }


}
