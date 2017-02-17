package com.example.lee.googlelogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class RoomListActivity extends AppCompatActivity {
    FirebaseDatabase fd = FirebaseDatabase.getInstance();
    DatabaseReference dr = fd.getReference();
    ArrayList<View> arr = new ArrayList<>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);
        listView = (ListView)findViewById(R.id.listView);
        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        DatabaseReference locationRef;
        final Adapter ad = new Adapter();
        if(location.equals("all")){
            locationRef = dr.child("room");

        }else{
            locationRef = dr.child("room"+location);
        }
        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> map = (Map<String,Object>)dataSnapshot.getValue();
                View roomView = View.inflate(RoomListActivity.this,R.layout.room_item,null);
                TextView title = (TextView)roomView.findViewById(R.id.roomTitleTv);
                TextView userName = (TextView)roomView.findViewById(R.id.profileTv);
                TextView population = (TextView)roomView.findViewById(R.id.population);
                TextView detail = (TextView)findViewById(R.id.locationTv);
                TextView dateTv = (TextView)findViewById(R.id.dateTv);
                String titleString = (String)map.get("title");
                String locationString = (String)map.get("location");
                String dateString = (String)map.get("date");
                String timeString = (String)map.get("time");
                String populationString = (String)map.get("0");
                String detailString = (String)map.get("detailLocation");
                String uid = (String)map.get("uid");
                title.setText(titleString);
                userName.setText("");
                detail.setText("");
                arr.add(roomView);
                ad.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public class Adapter extends BaseAdapter{
        @Override
        public int getCount() {
            return arr.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return arr.get(position);
        }
    }
}
