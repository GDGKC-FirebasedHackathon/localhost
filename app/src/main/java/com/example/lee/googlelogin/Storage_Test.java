package com.example.lee.googlelogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by lee on 2017. 2. 17..
 */

public class Storage_Test extends AppCompatActivity {


    private FirebaseStorage storage = FirebaseStorage.getInstance();


    private Button mSelectImage;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_text);

        mStorage = FirebaseStorage.getInstance().getReference(); // .child 해서 붙여도 됨
        mSelectImage = (Button)findViewById(R.id.selectImage);

        mSelectImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View ivew){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
    }





}
