package com.aura.YoteCompanion.NoteActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aura.YoteCompanion.Authentication.SignInActivity;
import com.aura.YoteCompanion.Models.Note;
import com.aura.YoteCompanion.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddNotes extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // Not signed in, launch the Sign In activity
        if (mFirebaseUser == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_save);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProgressBar pb = (ProgressBar) findViewById(R.id.prg_saving);
                    EditText txt_note_title = (EditText) findViewById(R.id.txt_note_title);
                    EditText txt_note_details = (EditText) findViewById(R.id.txt_note_details);

                    //Error handling for fields
                    if (txt_note_title.getText().toString().matches("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddNotes.this)
                                .setMessage("You have not entered a title")
                                .setCancelable(false).setPositiveButton("OK", null);
                        AlertDialog ad = builder.create();
                        ad.setTitle("Incomplete Info"); ad.show();
                    } else {
                        pb.setVisibility(View.VISIBLE);

                        //gives each note a key ID
                        String key = mDatabase.child("User Notes").push().getKey();
                        Note note = new Note();
                        note.setDetails(txt_note_details.getText().toString());
                        note.setTitle(txt_note_title.getText().toString());
                        note.setDateSaved(new Date());
                        note.setNoteId(key);

                        Map<String, Object> noteValues = note.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/Notes/" + mFirebaseUser.getUid() + "/" + key, noteValues);
                        mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Note saved", Toast.LENGTH_LONG);
                                    toast.show();
                                    finish();
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Error: " + databaseError.getMessage(), Toast.LENGTH_LONG);
                                    toast.show();
                                    finish();
                                }

                            }
                        });
                    }
                }
            });
        }
    }

}
