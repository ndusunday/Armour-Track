package com.makarioworks.app.armourkeep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mAuth;

    Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        signOutButton = new Button(this);
        signOutButton.setText("Sign Out");
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutUser();
            }
        });
    }

    public void signOutUser(){
        if (mAuth.getCurrentUser() != null ){
            mAuth.signOut();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
