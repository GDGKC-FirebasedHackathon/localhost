package com.example.lee.googlelogin;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class DetailSelectActivity extends AppCompatActivity {
    private Button finishBtn;
    private ImageView profileImg;
    private EditText nickNameEdit;
    private Button manBtn;
    private Button womenBtn;
    private String gender = "남";
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage fs = FirebaseStorage.getInstance();
    private StorageReference sr = fs.getReference();
    private final int GALLERY_INTENT = 2;
    private final DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_select);
        final Intent intent = getIntent();
        nickNameEdit = (EditText)findViewById(R.id.nicknameEdit);
        manBtn = (Button)findViewById(R.id.manBtn);
        finishBtn = (Button)findViewById(R.id.finishBtn);
        womenBtn = (Button)findViewById(R.id.womenBtn);
        profileImg = (ImageView)findViewById(R.id.profileImageView);
        if(user==null){
       //     Toast.makeText(this, "유저가 널값", Toast.LENGTH_SHORT).show();
        }else{
          //  Toast.makeText(this, "널값아님", Toast.LENGTH_SHORT).show();
        }
        manBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "남";
            }
        });
        womenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender ="여";
            }
        });
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    Map<String,Object> map =(Map<String,Object>)dataSnapshot.getValue();
//                    Uri uri = (Uri)map.get("photoUrl");
                 //   profileImg.setImageURI(uri);
                  //  nickNameEdit.setText((String)map.get("username"));
                    nickNameEdit.setText((String)map.get("username"));
                }else{

                    nickNameEdit.setText(user.getDisplayName());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1,GALLERY_INTENT);
            }
        });
        StorageReference userSR =  sr.child("photos").child("user").child(user.getUid()).child("profile.png");
        if(userSR.getDownloadUrl()!=null){
            //Toast.makeText(DetailSelectActivity.this, "가져오기", Toast.LENGTH_SHORT).show();
            userSR.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    profileImg.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
                }
            });
        }else{

        }
        if(intent.getStringExtra("type").equals("update")){
           nickNameEdit.setEnabled(false);
            finishBtn.setText("수정완료");
        }else if(intent.getStringExtra("type").equals("init")){

        }

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dr.child("/username").setValue(nickNameEdit.getText().toString());
                dr.child("/email").setValue(user.getEmail());
                dr.child("/photoUrl").setValue("");
                dr.child("/sex").setValue(gender);
                dr.child("/allStars").setValue("0");
                dr.child("/rateCount").setValue("0");
                Intent intent3 = new Intent(DetailSelectActivity.this,TabMainActivity.class);
                startActivity(intent3);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT&&resultCode==RESULT_OK){
            Uri uri = data.getData();
            Toast.makeText(this, uri.toString()+"선택완료", Toast.LENGTH_SHORT).show();
            sr.child("photos").child("user").child(user.getUid()).child("profile.png").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    dr.child("/photoUrl").setValue(downloadUrl.toString());
                    Picasso.with(DetailSelectActivity.this).load(downloadUrl).fit().centerCrop().into(profileImg);
                }
            });
        }
    }
}
