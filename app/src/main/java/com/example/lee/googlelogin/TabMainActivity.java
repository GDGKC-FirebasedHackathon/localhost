package com.example.lee.googlelogin;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TabHost;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class TabMainActivity extends TabActivity {
    private int[] blackImage = {R.drawable.multiple_users_silhouette,
            R.drawable.rounded_add_button,
            R.drawable.black_star,
            R.drawable.calendar_with_a_clock_time_tools
    };
    private int[] whiteImage = {R.drawable.white_multiple_user,
            R.drawable.white_rounded_btn,
            R.drawable.white_star,
            R.drawable.white_clock
    };
    private int[] tabId = {R.id.tab1, R.id.tab2, R.id.tab3, R.id.tab4};
    private final int tabsCount = 4;
    private Drawable[] drawArray = new Drawable[tabsCount];
    private TabHost.TabSpec[] tabs = new TabHost.TabSpec[tabsCount];
    private ImageView naviBtn;
    private LocationManager lm;
    private Geocoder geocoder;
    private LocationListener mLocationListener;
    private StorageReference mStorage;
    private FirebaseDatabase fd = FirebaseDatabase.getInstance();
    private DatabaseReference dr = fd.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference userDr = dr.child("user").child(user.getUid());
    private StorageReference sr = FirebaseStorage.getInstance().getReference().child("photos").child("user").child(user.getUid()).child("profile.png");
    private String selectCategory;
    private String spinnerSelect;
    private Integer hour=0,minute=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_main);
        final DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavigationView nv = (NavigationView) findViewById(R.id.naviView);
        View naviView = nv.getHeaderView(0);
        final TextView myLocationTv = (TextView) naviView.findViewById(R.id.myLocationTv);
        final RatingBar rb = (RatingBar) naviView.findViewById(R.id.ratingBar);
        final TextView ratingTv = (TextView)naviView.findViewById(R.id.ratingTv);
        final ImageView profileIv = (ImageView)naviView. findViewById(R.id.naviImage);
        final TextView profileName = (TextView)naviView.findViewById(R.id.naviProfileTv);
        profileName.setText(user.getDisplayName());
        mStorage = FirebaseStorage.getInstance().getReference();
        rb.setEnabled(false);
        naviBtn = (ImageView) findViewById(R.id.naviBtn);
        final TabHost th = getTabHost();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(TabMainActivity.this,DetailSelectActivity.class);
                intent3.putExtra("type","update");
                startActivity(intent3);
                finish();
            }
        });
        userDr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> map = (Map<String,Object>)dataSnapshot.getValue();
                String username = (String)map.get("username");
                String stars =(String)map.get("allStars");
                String allCounting =(String)map.get("rateCount");
                float starRate = Float.parseFloat(stars);
                profileName.setText(username);
                DecimalFormat df = new DecimalFormat("0.##");
                if(!allCounting.equals("0")){
                    ratingTv.setText(df.format(starRate/Float.parseFloat(allCounting))+"점");
                    rb.setRating(starRate/Float.parseFloat(allCounting));
                }

                sr.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        profileIv.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        geocoder = new Geocoder(getApplicationContext());
        for (int i = 0; i < tabsCount; i++) {
            drawArray[i] = getResources().getDrawable(blackImage[i]);

            tabs[i] = th.newTabSpec(Integer.toString(i)).setContent(tabId[i]).setIndicator("", drawArray[i]);
            th.addTab(tabs[i]);
        }

        naviBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dl.openDrawer(Gravity.LEFT);
            }
        });

        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                //Toast.makeText(TabMainActivity.this, "dddddddddddddddd", Toast.LENGTH_SHORT).show();
                double longitude = location.getLongitude(); //경도
                double latitude = location.getLatitude();   //위도
                double altitude = location.getAltitude();   //고도
                float accuracy = location.getAccuracy();    //정확도
                String provider = location.getProvider();   //위치제공자
                //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
                //Network 위치제공자에 의한 위치변화
                //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocation(latitude, longitude, 10);
                    myLocationTv.setText(list.get(0).getAdminArea() + " " + list.get(0).getLocality() + " " + list.get(0).getThoroughfare());
                } catch (IOException e) {

                }
                if (list != null) {

                } else {

                }
            }

            public void onProviderDisabled(String provider) {
                // Disabled시
            }

            public void onProviderEnabled(String provider) {
                // Enabled시
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                // 변경시
            }
        };
        try {

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mLocationListener);
        } catch (SecurityException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        final int[] locationIdList = {R.id.seoul,R.id.gyungi,R.id.incheon,R.id.daejeon,R.id.chungcheong,R.id.gangyeon
        ,R.id.pusan,R.id.ulsan,R.id.kyengsang,R.id.jeonla,R.id.daeku,R.id.jeju};
        String[] locationString = {"서울","경기","인천","대전","충청","강원","부산","울산","경상","전라","대구","제주"};
        TextView[] textViewList = new TextView[locationIdList.length];
        for(int i=0;i<locationIdList.length;i++){
            textViewList[i] = (TextView)findViewById(locationIdList[i]);
            final String myLocation = locationString[i];
            textViewList[i].setOnClickListener(new View.OnClickListener() {
                final String location = myLocation;
                @Override
                public void onClick(View v) {
                    Intent intentString = new Intent(TabMainActivity.this,FoodCategorySelect.class);
                    intentString.putExtra("location",location);
                    startActivity(intentString);
                }
            });
        }
        final Button signOutBtn = (Button)naviView.findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(TabMainActivity.this,MainActivity.class));
                finish();
            }
        });
        final EditText roomEdit = (EditText)findViewById(R.id.roomEdit);
        final RadioGroup rg = (RadioGroup)findViewById(R.id.categorySelect);
        final DatePicker dp = (DatePicker)findViewById(R.id.datePicker);
        final EditText tp = (EditText) findViewById(R.id.timePicker);
        final Button createBtn = (Button)findViewById(R.id.roomCreateBtn);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio1:
                        selectCategory = "치킨";
                        break;
                    case R.id.radio2:
                        selectCategory = "중식";
                        break;
                    case R.id.radio3:
                        selectCategory = "분식/디저트";
                        break;
                    case R.id.radio4:
                        selectCategory = "한식";
                        break;
                    case R.id.radio5:
                        selectCategory = "피자/양식";
                        break;
                    case R.id.radio6:
                        selectCategory = "일식/돈까스";
                        break;
                    case R.id.radio7:
                        selectCategory = "족발/보쌈";
                        break;
                    case R.id.radio8:
                        selectCategory = "야식";
                        break;
                }
            }
        });
        final Spinner spinner = (Spinner)findViewById(R.id.locationSpinner);
        final ArrayAdapter<String> adapters = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,locationString);
        spinner.setAdapter(adapters);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelect= adapters.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerSelect= adapters.getItem(0);
            }
        });
        final EditText locationEdit = (EditText)findViewById(R.id.editLocationDetail);
        final EditText memoEdit = (EditText)findViewById(R.id.roomMemoEdit);


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomTitle = roomEdit.getText().toString();
                String category = selectCategory;
                String date = dp.getYear()+"년"+dp.getMonth()+"월"+dp.getDayOfMonth()+"일";
                String time = tp.getText().toString();
                String locationSelect = spinnerSelect;
                String memo = memoEdit.getText().toString();
                String detailLocation = locationEdit.getText().toString();
                String key = dr.child("room").child(locationSelect).push().getKey();
                DatabaseReference room = dr.child("room").child(locationSelect).child(key);
                room.child("username").setValue(user.getUid());
                room.child("photoUrl").setValue();
                room.child("title").setValue(roomTitle);
                room.child("date").setValue(date);
                room.child("time").setValue(time);
                room.child("location").setValue(locationSelect);
                room.child("detailLocation").setValue(detailLocation);
                room.child("key").setValue(key);
            }
        });
    }
}

/*

        StorageReference filePath = mStorage.child("photos").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if (filePath == null) {
            Toast.makeText(this, "너럴널", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "잇긴잇음", Toast.LENGTH_SHORT).show();
            filePath.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Toast.makeText(TabMainActivity.this, "성공함", Toast.LENGTH_SHORT).show();
                    profileIv.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                }
            });
        }
 */