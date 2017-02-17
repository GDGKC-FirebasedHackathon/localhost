package com.example.lee.googlelogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class FoodCategorySelect extends AppCompatActivity {
    int textArrayId [] = {R.id.allTv,R.id.roomTv1,R.id.roomTv2
            ,R.id.roomTv3,R.id.roomTv4
            ,R.id.roomTv5,R.id.roomTv6
            ,R.id.roomTv7,R.id.roomTv8};
    String []categoryTag = {"all","치킨","중식","분식/디저트","한식","피자/양식","일식/돈까스","족발/보쌈","야식"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_category_select);
        final Intent intent = getIntent();
        Toast.makeText(this,intent.getStringExtra("location"), Toast.LENGTH_SHORT).show();
        TextView[] textArray = new TextView[9];
        for(int i=0;i<9;i++){
            textArray[i] = (TextView)findViewById(textArrayId[i]);
            final String category = categoryTag[i];
            textArray[i].setOnClickListener(new View.OnClickListener() {
                String mCategory = category;
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(FoodCategorySelect.this,RoomListActivity.class);
                    intent1.putExtra("location",intent.getStringExtra("location"));
                    intent1.putExtra("category",mCategory);
                    startActivity(intent1);
                }
            });
        }
    }
}
