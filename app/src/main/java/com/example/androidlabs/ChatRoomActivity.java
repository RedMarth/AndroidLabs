package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.widget.TextView;

public class ChatRoomActivity extends AppCompatActivity {
    private ArrayList<String> elements = new ArrayList<>();
    private MyListAdapter aListAdapter;
//    private ListAdapter aListAdapter = new ListAdapter() {
//
//        @Override
//        public boolean areAllItemsEnabled() {
//            return false;
//        }
//
//        @Override
//        public boolean isEnabled(int position) {
//            return false;
//        }
//
//        @Override
//        public void registerDataSetObserver(DataSetObserver observer) {
//
//        }
//
//        @Override
//        public void unregisterDataSetObserver(DataSetObserver observer) {
//
//        }
//
//        @Override
//        public int getCount() {
//            return elements.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return elements.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return (long) position;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return false;
//        }
//
//        @Override
//        public View getView(int position, View old, ViewGroup parent) {
//            View newView = old;
//            LayoutInflater inflater = getLayoutInflater();
//
//            //make a new row:
//            if(newView == null) {
//                newView = inflater.inflate(R.layout.row_layout, parent, false);
//            }
//
//            //set what the text should be for this row:
//            TextView tView = newView.findViewById(R.id.textGoesHere);
//            tView.setText(getItem(position).toString());
//
//            return newView;
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return 0;
//        }
//
//        @Override
//        public int getViewTypeCount() {
//            return 0;
//        }
//
//        @Override
//        public boolean isEmpty() {
//            return false;
//        }
//
//    };

    private class MyListAdapter extends BaseAdapter {

        public int getCount() { return elements.size(); }

        public Object getItem(int position) { return elements.get(position); }

        public long getItemId(int position) { return (long) position; }

        public View getView(int position, View old, ViewGroup parent)
        {
            View newView = old;
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            if(newView == null) {
                newView = inflater.inflate(R.layout.row_layout, parent, false);

            }
            //set what the text should be for this row:
            TextView tView = newView.findViewById(R.id.textGoesHere);
            tView.setText( getItem(position).toString() );

            //return it to be put in the table
            return newView;
        }
    }

    private class Message{
        private String textMessage;
        private boolean isSend;

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(aListAdapter = new MyListAdapter());


    }
}