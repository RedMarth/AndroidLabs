package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.widget.TextView;

public class ChatRoomActivity extends AppCompatActivity {
    private ArrayList<Message> elements = new ArrayList<>();
    private MyListAdapter aListAdapter;

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

            if (msg.isSend()) {
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
        private boolean isSend;

        public Message(String textMessage, boolean isSend) {
            this.textMessage = textMessage;
            this.isSend = isSend;
        }

        public String getTextMessage() {
            return textMessage;
        }

        public boolean isSend() {
            return isSend;
        }

        public void setTextMessage(String textMessage) {
            this.textMessage = textMessage;
        }

        public void setSend(boolean send) {
            isSend = send;
        }

        @Override
        public String toString() {
            return textMessage;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView listView = (ListView) findViewById(R.id.listView);
        Button send = findViewById(R.id.sendButton);
        EditText typeMessage = findViewById(R.id.typeHere);
        Button receive = findViewById(R.id.receiveButton);

        listView.setAdapter(aListAdapter = new MyListAdapter());
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this?");

            //What is the message:
            alertDialogBuilder.setMessage("The selected row is: " + position + "\n\nThe database id is: " + id);

            //What the yes button does:
            alertDialogBuilder.setPositiveButton("Yes", (click, arg) -> {
                elements.remove(position);
                aListAdapter.notifyDataSetChanged();
            });

            //What the No button does:
            alertDialogBuilder.setNegativeButton("No", (click, arg) -> {

            });

            //Show the dialog:
            alertDialogBuilder.create().show();

        return true;
        });

        send.setOnClickListener((v) -> {
            elements.add(new Message(typeMessage.getText().toString(), true));
            aListAdapter.notifyDataSetChanged();
            typeMessage.setText("");
        });

        receive.setOnClickListener((v) -> {
            elements.add(new Message(typeMessage.getText().toString(), false));
            aListAdapter.notifyDataSetChanged();
            typeMessage.setText("");
        });
    }
}