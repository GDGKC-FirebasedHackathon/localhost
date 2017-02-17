package com.example.lee.googlelogin;

        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import com.google.android.gms.auth.api.Auth;
        import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
        import com.google.android.gms.auth.api.signin.GoogleSignInResult;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.SignInButton;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.ResultCallback;
        import com.google.android.gms.common.api.Status;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;




public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

// !!!!!!!!!!!!!#####################  auth 부분  !!!!!!!!!!!!!#####################
    SignInButton signInButton;
    Button signOutButton;
    TextView statusTextView;
    GoogleApiClient mGoogleApiClient;


    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(); // 데이터 베이스 변경 될 때마다 처리
    DatabaseReference mConditionRef = mRootRef.child("condition");
    // !!!!!!!!!!!!!#####################  auth 부분  !!!!!!!!!!!!!#####################


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // !!!!!!!!!!!!!#####################  auth 부분  !!!!!!!!!!!!!#####################

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        statusTextView = (TextView)findViewById(R.id.status_textview);
        signInButton.setOnClickListener(this);

        signOutButton = (Button)findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);
      // !!!!!!!!!!!!!#####################  auth 부분  !!!!!!!!!!!!!#####################




    }  //oncreate 끝    // !!!!!!!!!!!!!#####################  auth 부분  !!!!!!!!!!!!!#####################

    @Override
    public void onClick(View v){
        // !!!!!!!!!!!!!#####################  auth 부분  !!!!!!!!!!!!!#####################
        switch (v.getId()){
            case R.id.sign_in_button: signIn(); break;
            case R.id.signOutButton : signOut(); break;

        }
        // !!!!!!!!!!!!!#####################  auth 부분  !!!!!!!!!!!!!#####################
    }



    // !!!!!!!!!!!!!#####################  auth 부분  !!!!!!!!!!!!!#####################
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
        Log.d(TAG, "handleSignInResult:" +result.isSuccess());
        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
             statusTextView.setText("Login Success!!" + acct.getDisplayName());
        }else{

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                statusTextView.setText("로그아웃");
            }
        });
    }

    // !!!!!!!!!!!!!#####################  auth 부분  !!!!!!!!!!!!!#####################

}