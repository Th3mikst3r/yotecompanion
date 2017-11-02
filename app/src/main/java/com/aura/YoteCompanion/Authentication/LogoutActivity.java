package com.aura.YoteCompanion.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.aura.YoteCompanion.R;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.firebase.auth.FirebaseAuth;

import org.xml.sax.helpers.LocatorImpl;

public class LogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FirebaseAuth.getInstance().signOut();

     /*  LoginActivity la = new LoginActivity();
       la.signOut();*/

        Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
