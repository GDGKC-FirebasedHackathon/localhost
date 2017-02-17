package com.example.lee.googlelogin;

import android.app.TabActivity;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_main);
        final DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavigationView nv = (NavigationView) findViewById(R.id.naviView);
        View naviView = nv.getHeaderView(0);
        final TextView myLocationTv = (TextView) naviView.findViewById(R.id.myLocationTv);
        final RatingBar rb = (RatingBar) naviView.findViewById(R.id.ratingBar);
        final ImageView profileIv = (ImageView)naviView. findViewById(R.id.naviImage);
        final TextView profileName = (TextView)naviView.findViewById(R.id.naviProfileTv);
        profileName.setText(user.getDisplayName());
        mStorage = FirebaseStorage.getInstance().getReference();
        rb.setEnabled(false);
        naviBtn = (ImageView) findViewById(R.id.naviBtn);
        final TabHost th = getTabHost();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        geocoder = new Geocoder(getApplicationContext());
        for (int i = 0; i < tabsCount; i++) {
            drawArray[i] = getResources().getDrawable(blackImage[i]);

            tabs[i] = th.newTabSpec(Integer.toString(i)).setContent(tabId[i]).setIndicator("", drawArray[i]);
            th.addTab(tabs[i]);
        }


        /*
        th.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                int index = Integer.parseInt(s);

                for(int i=0;i<tabsCount;i++){
                    if(i ==index){
                        drawArray[i] = getResources().getDrawable(whiteImage[i]);
                    }else{
                        drawArray[i] = getResources().getDrawable(blackImage[i]);
                    }
                    tabs[i] = th.newTabSpec(Integer.toString(i)).setContent(tabId[i]).setIndicator("",drawArray[i]);
                    th.addTab(tabs[i]);
                }
                th.setCurrentTab(index);
            }
        });
        */

        naviBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dl.openDrawer(Gravity.LEFT);
            }
        });

        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Toast.makeText(TabMainActivity.this, "dddddddddddddddd", Toast.LENGTH_SHORT).show();
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