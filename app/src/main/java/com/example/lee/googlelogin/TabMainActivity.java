package com.example.lee.googlelogin;

import android.app.TabActivity;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

public class TabMainActivity extends TabActivity{
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
    private int[] tabId = {R.id.tab1,R.id.tab2,R.id.tab3,R.id.tab4};
    private final int tabsCount=4;
    private Drawable[] drawArray = new Drawable[tabsCount];
    private TabHost.TabSpec[] tabs = new TabHost.TabSpec[tabsCount];
    private ImageView naviBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DrawerLayout dl = (DrawerLayout)findViewById(R.id.drawerLayout);
        naviBtn = (ImageView) findViewById(R.id.naviBtn);
        final TabHost th = getTabHost();
        for(int i=0;i<tabsCount;i++){
            drawArray[i] = getResources().getDrawable(blackImage[i]);

            tabs[i] = th.newTabSpec(Integer.toString(i)).setContent(tabId[i]).setIndicator("",drawArray[i]);
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
    }
}