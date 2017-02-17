package com.example.lee.googlelogin;

        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.auth.api.Auth;
        import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
        import com.google.android.gms.auth.api.signin.GoogleSignInResult;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.SignInButton;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.ResultCallback;
        import com.google.android.gms.common.api.Status;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private FirebaseAuth fa = FirebaseAuth.getInstance();
    private FirebaseDatabase fb = FirebaseDatabase.getInstance();
    private DatabaseReference df = fb.getReference();
    private SignInButton signInButton;
    private Button signOutButton;
    private TextView statusTextView;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(); // 데이터 베이스 변경 될 때마다 처리
    private DatabaseReference mConditionRef = mRootRef.child("condition");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        signInButton = (SignInButton)findViewById(R.id.sign_in_button);
     //   statusTextView = (TextView)findViewById(R.id.status_textview);
        signInButton.setOnClickListener(this);
        fa.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fb = fa.getCurrentUser();
                if(fb==null){
                    Toast.makeText(MainActivity.this,"안됨",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"성공",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.sign_in_button: signIn(); break;
//            case R.id.signOutButton : signOut(); break;

        }
    }




    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(MainActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, acct.getEmail(), Toast.LENGTH_SHORT).show();

        }else{

            Toast.makeText(MainActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        Toast.makeText(MainActivity.this, "연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
    }
    /*
  private void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                statusTextView.setText("로그아웃");
            }
        });
    }
    */
}