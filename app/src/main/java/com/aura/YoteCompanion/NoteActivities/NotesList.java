package com.aura.YoteCompanion.NoteActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aura.YoteCompanion.Authentication.LogoutActivity;
import com.aura.YoteCompanion.HomeActivity;
import com.aura.YoteCompanion.SettingsActivites.SetTest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.aura.YoteCompanion.Helpers.NotesAdapter;
import com.aura.YoteCompanion.Helpers.DividerItemDecoration;
import com.aura.YoteCompanion.Models.Note;
import java.util.ArrayList;
import java.util.List;
import com.aura.YoteCompanion.R;

public class NotesList extends AppCompatActivity {
    private List<Note> notesList  = new ArrayList<>();
    private RecyclerView lstNotes;
    private NotesAdapter nAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_noteslist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ActionBar
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNotes.class);
                startActivity(intent);
            }
        });
        lstNotes = (RecyclerView) findViewById(R.id.lst_notes);
        nAdapter = new NotesAdapter(notesList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        lstNotes.setLayoutManager(layoutManager);
        lstNotes.setItemAnimator(new DefaultItemAnimator());
        lstNotes.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        lstNotes.setAdapter(nAdapter);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference notesRef = database.getReference("/user-notes/" + currUser + "/");

        notesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue() != null) {
                    String title = dataSnapshot.child("title").getValue().toString();
                    String details = (String) dataSnapshot.child("details").getValue();
                    String date = (String) dataSnapshot.child("dateSaved").getValue();
                    Note note = new Note(title, details, date);
                    notesList.add(note);
                    nAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    notesRef.getRef().removeValue();
                }
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        //
        lstNotes.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), lstNotes, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Note note = notesList.get(position);
                Intent intent = new Intent(getApplicationContext(), ViewNote.class);
                intent.putExtra("Note", note);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, final int position) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(NotesList.this);
                alert.setMessage("Delete the note? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    notesList.remove(position);
                                    nAdapter.notifyDataSetChanged();
                                    Toast.makeText(NotesList.this, "Note Delete", Toast.LENGTH_SHORT).show();
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


        //Toast.makeText(this, "Total number of notes " + "",+ Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notes:
                Intent notes = new Intent(getApplicationContext(), NotesList.class);
                startActivity(notes);
                break;
            case R.id.action_habits:
                //Intent hab = new Intent(getApplicationContext(), HabitsActivity.class);
                //startActivity(hab);
                break;
            case R.id.action_refresh:
                Intent refresh = new Intent(getApplicationContext(), NotesList.class);
                startActivity(refresh);
                break;
            case R.id.action_home:
                Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(home);
                break;
            case R.id.action_settings:
                Intent settings = new Intent(getApplicationContext(), SetTest.class);
                startActivity(settings);
                break;
            case R.id.action_logout:
                Intent log_out = new Intent(getApplicationContext(), LogoutActivity.class);
                startActivity(log_out);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return false;
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private NotesList.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final NotesList.ClickListener clickListener) {
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
}