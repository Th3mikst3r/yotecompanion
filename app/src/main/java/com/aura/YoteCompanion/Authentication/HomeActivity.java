package com.aura.YoteCompanion.Authentication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.aura.YoteCompanion.HabitActivites.HabitList;
import com.aura.YoteCompanion.NoteActivities.NotesList;
import com.aura.YoteCompanion.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private TextView userloggedin;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    /*    if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
*/
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        /*If user is not logged in, Start the SignInAcitivity*/
        if(mFirebaseUser == null){
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API).build();

        /*Displays the logged in user by grabbing the Firebase auth from SignInActivity*/
        /*userloggedin = (TextView) findViewById(R.id.currloggedinuser);
        Bundle extras = getIntent().getExtras();
        userloggedin.setText("Welcome");
        if(extras!=null){
            userloggedin.setText("Welcome "+extras.getString("UserName"));
        }*/

        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_reminder);
        actionA.setTitle("Habits");
        actionA.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HabitList.class);
                startActivity(intent);
            }
        });
        final FloatingActionButton actionB = (FloatingActionButton) findViewById(R.id.action_notes);
        actionB.setTitle("Notes");
        actionB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NotesList.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}