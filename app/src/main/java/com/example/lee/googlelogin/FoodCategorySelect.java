package com.example.lee.googlelogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class FoodCategorySelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_category_select);
        Intent intent = getIntent();
        Toast.makeText(this,intent.getStringExtra("location"), Toast.LENGTH_SHORT).show();
    }
}
