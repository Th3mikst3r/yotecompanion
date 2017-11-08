package com.aura.YoteCompanion.HabitActivites;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.aura.YoteCompanion.Authentication.SignInActivity;
import com.aura.YoteCompanion.Helpers.DividerItemDecoration;
import com.aura.YoteCompanion.Helpers.HabitAdapter;
import com.aura.YoteCompanion.Models.Habit;
import com.aura.YoteCompanion.R;
import com.aura.YoteCompanion.SettingsActivites.SettingsActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Hanson on 10/10/2016.
 */

public class HabitList extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private List<Habit> habitList  = new ArrayList<>();
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference habitRef;

    private RecyclerView lstHabit;
    private HabitAdapter hAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ActionBar
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API).build();

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            String mUsername = mFirebaseUser.getDisplayName();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddHabit.class);
                startActivity(intent);
            }
        });

        lstHabit = (RecyclerView) findViewById(R.id.lst_habit);
        hAdapter = new HabitAdapter(habitList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        lstHabit.setLayoutManager(layoutManager);
        lstHabit.setItemAnimator(new DefaultItemAnimator());
        lstHabit.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        lstHabit.setAdapter(hAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        habitRef = database.getReference("/Habits/" + mFirebaseUser.getUid() + "/");

        habitRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue() != null) {
                    //String uid = dataSnapshot.getValue();
                    String habitName = dataSnapshot.child("habitName").getValue().toString();
                    String details = dataSnapshot.child("details").getValue().toString();
                    String numOfTimes = dataSnapshot.child("Number Of Times").getValue().toString();
                    String date = dataSnapshot.child("date").getValue().toString();
                    String time = dataSnapshot.child("time").getValue().toString();
                    boolean isChecked = (boolean) dataSnapshot.child("isChecked").getValue();
                    Habit habit = new Habit(habitName, details, numOfTimes, date, time, isChecked);
                    habitList.add(habit);
                    hAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        //
        lstHabit.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), lstHabit, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Habit habit = habitList.get(position);
                Intent intent = new Intent(getApplicationContext(), ViewHabit.class);
                intent.putExtra("Habit", habit);
                startActivity(intent);
                Toast.makeText(HabitList.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onLongClick(View view, final int position) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(HabitList.this);
                alert.setMessage("Delete the Habit? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    habitList.remove(position);
                                    hAdapter.notifyDataSetChanged();
                                    Toast.makeText(HabitList.this, "Habit Delete", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("Cancel" , null);
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settings);
                break;
            case R.id.action_logout:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mFirebaseUser = null;
                //mUsername = ANONYMOUS;
                Intent log_out = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(log_out);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private HabitList.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final HabitList.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }


    /*public void onCheckBoxChicked(View view){
        final CheckBox checkBox = (CheckBox) findViewById(R.id.cb_done);
        //checkBox.setChecked(!checkBox.isChecked());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    Toast.makeText(HabitList.this, "Done!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(HabitList.this, "Not Done", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        });
    }*/
}
