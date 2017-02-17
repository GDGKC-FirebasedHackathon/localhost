package com.example.lee.googlelogin;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class RoomListActivity extends AppCompatActivity {
    FirebaseDatabase fd = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dr = fd.getReference();
    ArrayList<View> arr = new ArrayList<>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);
        listView = (ListView)findViewById(R.id.listView);
        Intent intent = getIntent();
        final String location = intent.getStringExtra("location");
        final String category = intent.getStringExtra("category");
        final DatabaseReference locationRef;
        final Adapter ad = new Adapter();
        listView.setAdapter(ad);
        if(category.equals("all")){
            locationRef = dr.child("room/"+location);

        }else{
            locationRef = dr.child("room/"+location+"/"+category);
        }
        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> datamap = (Map<String,Object>)dataSnapshot.getValue();
                if(datamap!=null&&!category.equals("all")){
                    for(String key : datamap.keySet()){
                        final Map<String,Object> map = (Map<String,Object>)datamap.get(key);
                        View roomView = View.inflate(RoomListActivity.this,R.layout.room_item,null);
                        TextView title = (TextView)roomView.findViewById(R.id.roomTitleTv);
                        TextView userName = (TextView)roomView.findViewById(R.id.profileTv);
                        TextView population = (TextView)roomView.findViewById(R.id.population);
                        TextView detail = (TextView)roomView.findViewById(R.id.locationTv);
                        TextView dateTv = (TextView)roomView.findViewById(R.id.dateTv);
                        String titleString = (String)map.get("title");
                        String locationString = (String)map.get("location");
                        String dateString = (String)map.get("date");
                        String timeString = (String)map.get("time");
                        String populationString = (String)map.get("memberCount");
                        final Integer populationInt = Integer.parseInt(populationString);
                       final  String getKeys = key;
                        roomView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(isUidInThatRoom((Map<String,Object>)map.get("member"),user.getUid())){
                                    Toast.makeText(RoomListActivity.this, "이미 신청하셨습니다.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                   String memberKey= locationRef.child(getKeys).child("member").push().getKey();
                                    locationRef.child(getKeys).child("member").child(memberKey).child("uid").setValue(user.getUid());
                                    locationRef.child(getKeys).child("memberCount").setValue(Integer.toString(populationInt+1));
                                    Toast.makeText(RoomListActivity.this, "신청이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        String detailString = (String)map.get("detailLocation");
                        String uid = (String)map.get("uid");
                        title.setText(titleString);
                        userName.setText((String)map.get("username"));
                        detail.setText(detailString);
                        dateTv.setText(dateString);
                        population.setText(populationString);
                        final ImageView iv = (ImageView)roomView.findViewById(R.id.img);
                        StorageReference sr = FirebaseStorage.getInstance().getReference().child("photos").child("user").child(uid).child("profile.png");
                        sr.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                iv.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
                            }
                        });
                        arr.add(roomView);
                        ad.notifyDataSetChanged();
                    }
                }else if(datamap!=null&&category.equals("all")){
                            for(String category : datamap.keySet()){
                                Map<String,Object> kMap = (Map<String,Object>)datamap.get(category);
                                Toast.makeText(RoomListActivity.this, category, Toast.LENGTH_SHORT).show();
                                if(kMap!=null){
                                   for(String key : kMap.keySet()){
                                       final Map<String,Object> map = (Map<String,Object>)kMap.get(key);
                                       View roomView = View.inflate(RoomListActivity.this,R.layout.room_item,null);
                                       TextView title = (TextView)roomView.findViewById(R.id.roomTitleTv);
                                       TextView userName = (TextView)roomView.findViewById(R.id.profileTv);
                                       TextView population = (TextView)roomView.findViewById(R.id.population);
                                       TextView detail = (TextView)roomView.findViewById(R.id.locationTv);
                                       TextView dateTv = (TextView)roomView.findViewById(R.id.dateTv);
                                       String titleString = (String)map.get("title");
                                       String locationString = (String)map.get("location");
                                       String dateString = (String)map.get("date");
                                       String timeString = (String)map.get("time");
                                       String populationString = (String)map.get("memberCount");
                                       final Integer populationInt = Integer.parseInt(populationString);
                                       String detailString = (String)map.get("detailLocation");
                                       String uid = (String)map.get("uid");
                                       title.setText(titleString);
                                       userName.setText((String)map.get("username"));
                                       detail.setText(detailString);
                                       dateTv.setText(dateString);
                                       population.setText(populationString);
                                       final  String getKeys = key;
                                       final String categorys = category;
                                       roomView.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               if(isUidInThatRoom((Map<String,Object>)map.get("member"),user.getUid())){
                                                   Toast.makeText(RoomListActivity.this, "이미 신청하셨습니다.", Toast.LENGTH_SHORT).show();
                                               }
                                               else{
                                                   String memberKey= locationRef.child(getKeys).child("member").push().getKey();
                                                   locationRef.child(categorys).child(getKeys).child("member").child(memberKey).child("uid").setValue(user.getUid());
                                                   locationRef.child(categorys).child(getKeys).child("memberCount").setValue(Integer.toString(populationInt+1));
                                                   Toast.makeText(RoomListActivity.this, "신청이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       });
                                       final ImageView iv = (ImageView)roomView.findViewById(R.id.img);
                                       StorageReference sr = FirebaseStorage.getInstance().getReference().child("photos").child("user").child(uid).child("profile.png");
                                       sr.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                           @Override
                                           public void onSuccess(byte[] bytes) {
                                               iv.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
                                           }
                                       });
                                       arr.add(roomView);
                                       ad.notifyDataSetChanged();
                                   }
                                }
                    }
                }
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
    public boolean isUidInThatRoom(Map<String,Object> map,String uid){
        for(String key : map.keySet()){
            Map<String,Object> data = (Map<String,Object>)map.get(key);
            String uidData = (String)data.get("uid");
            if(uidData.equals(uid)){
                return true;
            }
        }
        return false;
    }
}
