package com.aura.YoteCompanion.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
 * Provides UI for the view with habit list
 * Michael Hanson 11/16/17
 */
public class HabitsListFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    private List<Habit> habitList  = new ArrayList<>();
    private DatabaseReference habitRef, mDeleteUserHabitDB;
    private RecyclerView lstHabit;
    private HabitAdapter hAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.activity_habitlist, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout_habits_list);

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



        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFireBaseUser = mFirebaseAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        habitRef = database.getReference("/Habits/" + mFireBaseUser.getUid() + "/");

      /*  if(hAdapter.getItemCount() == 0){
            Snackbar snackbar = Snackbar.make(v.findViewById(R.id.coordinatorLayoutHabitsList), "You have no habits", Snackbar.LENGTH_INDEFINITE).
                    setAction("Create one", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), AddHabit.class);
                            startActivity(intent);
                        }
                    });
            snackbar.show();
        }*/

        habitRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.getValue() != null) {
                        String habitName = dataSnapshot.child("habitName").getValue().toString();
                        String details = dataSnapshot.child("details").getValue().toString();
                        String date = dataSnapshot.child("date").getValue().toString();
                        String time = dataSnapshot.child("time").getValue().toString();
                        String habitId = (String) dataSnapshot.child("habitId").getValue();

                        boolean isChecked = false;
                        try {
                            isChecked = (boolean) dataSnapshot.child("isChecked").getValue();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Habit habit = new Habit(habitName, details, date, time, habitId, isChecked);

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
            }
            @Override
            public void onLongClick(View view, final int position) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Delete this Habit? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Habit habit = habitList.get(position);
                                    String habitId = habit.getHabitId();
                                    mDeleteUserHabitDB = FirebaseDatabase.getInstance().getReference().child("/Habits/" + mFireBaseUser.getUid() + "/").child(habitId);
                                    mDeleteUserHabitDB.removeValue();
                                    habitList.remove(position);
                                    hAdapter.notifyItemRemoved(position);
                                    Toast.makeText(v.getContext(), "Deleted Successfully " + habitId, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(v.getContext(), "Failed to delete....", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("Cancel" , null);
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        }));

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout_habits_list);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                refresh();
            }
        });

        return  v;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    public void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                hAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1200);
    }
}
