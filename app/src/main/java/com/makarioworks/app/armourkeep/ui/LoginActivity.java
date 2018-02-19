package com.makarioworks.app.armourkeep.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.makarioworks.app.armourkeep.MainActivity;
import com.makarioworks.app.armourkeep.R;

/**
 * Created by ndu on 2/19/18.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = LoginActivity.class.getSimpleName().toUpperCase();
    public static final int RC_SIGN_IN = 1;

    GoogleSignInClient mGoogleSignInClient;
    SignInButton gSignInButton;

    GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        // Configure the sign-in to request user's ID, email and basic profile
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Create the google sign in client object and pass in the gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        gSignInButton = findViewById(R.id.google_signin_Button);
        gSignInButton.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "OnStart: Check for available signed in account");
        // Check for existing google sign in account
        if (GoogleSignIn.getLastSignedInAccount(LoginActivity.this) != null){
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            updateUI(account);

            // Notify the user
            Toast.makeText(this, "Login found", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "OnStart: Check for available signed in account");
        }
    }

    /* This is the function to update the ui for successful login*/
    public void updateUI(GoogleSignInAccount acc){
        Log.v(TAG, "Authenticated, Main Activity is initiated");
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
    }

    /* Do the actual sign in with the SignInClient */
    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the intent form Client.getSignInIntent ...
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String userEmail = account.getEmail();
            String user = account.getDisplayName();
            String userId = account.getId();

            Log.v(TAG, "User info: "+user+" Email: "+userEmail);
            //Signed In Successfully , show the main activity
            updateUI(account);
        } catch (ApiException e) {
            e.printStackTrace();

            Log.w(TAG, "sighnInResult:failed code ="+ e.getStatusCode());
            updateUI(null);
        }
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.google_signin_Button:
                signIn();
                break;
        }
    }
}
