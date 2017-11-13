package com.aura.YoteCompanion.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aura.YoteCompanion.HabitActivites.AddHabit;
import com.aura.YoteCompanion.HabitActivites.HabitList;
import com.aura.YoteCompanion.HabitActivites.ViewHabit;
import com.aura.YoteCompanion.Helpers.HabitAdapter;
import com.aura.YoteCompanion.Models.Habit;
import com.aura.YoteCompanion.R;
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
 * Provides UI for the view with List.
 */
public class HabitsListFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    private List<Habit> habitList  = new ArrayList<>();
    private DatabaseReference habitRef;
    private RecyclerView lstHabit;
    private HabitAdapter hAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.activity_habitlist, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddHabit.class);
                startActivity(intent);
            }

        });

        lstHabit = (RecyclerView) v.findViewById(R.id.lst_habit);
        hAdapter = new HabitAdapter(habitList);
        //lstNotes.setItemAnimator(new DefaultItemAnimator());
        lstHabit.setLayoutManager(new LinearLayoutManager(getActivity()));
        lstHabit.setAdapter(hAdapter);

  /*      Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,42);
        Intent intent = new Intent(getActivity(),NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,pendingIntent);
*/
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFireBaseUser = mFirebaseAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        habitRef = database.getReference("/Habits/" + mFireBaseUser.getUid() + "/");

        habitRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.getValue() != null) {
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

        lstHabit.addOnItemTouchListener(new HabitList.RecyclerTouchListener(getActivity(), lstHabit, new HabitList.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Habit habit = habitList.get(position);
                Intent intent = new Intent(getActivity(), ViewHabit.class);
                intent.putExtra("Habit", habit);
                startActivity(intent);
                Toast.makeText(v.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onLongClick(View view, final int position) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Delete the Habit? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    habitList.remove(position);
                                    hAdapter.notifyDataSetChanged();
                                    Toast.makeText(v.getContext(), "Habit Delete", Toast.LENGTH_SHORT).show();
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

        return  v;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
