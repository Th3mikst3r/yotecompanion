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
import com.aura.YoteCompanion.R;
import com.aura.YoteCompanion.Models.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditNote extends AppCompatActivity {
    private EditText txt_title;
    private EditText txt_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        txt_details = (EditText) findViewById(R.id.txt_note_details) ;
        txt_title = (EditText) findViewById(R.id.txt_note_title);
        //
        Intent intent = getIntent();
        Note note = (Note) intent.getSerializableExtra("Note");
        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_save);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProgressBar prg_saving = (ProgressBar) findViewById(R.id.prg_saving);
                    EditText txt_note_title = (EditText) findViewById(R.id.txt_note_title);
                    EditText txt_note_details = (EditText) findViewById(R.id.txt_note_details);
                    //
                    String title = txt_note_title.getText().toString();
                    if (title.matches("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditNote.this)
                                .setMessage("You have not entered a title")
                                .setCancelable(false)
                                .setPositiveButton("OK", null);
                        AlertDialog ad = builder.create();
                        ad.setTitle("Incomplete Info"); ad.show();
                    } else {
                        prg_saving.setIndeterminate(true);
                        prg_saving.setVisibility(View.VISIBLE);
                        txt_note_details.setEnabled(false);
                        txt_note_title.setEnabled(false);

                        final Note note = new Note();
                        note.setDetails(txt_note_details.getText().toString());
                        note.setTitle(txt_note_title.getText().toString());
                        note.setDateSaved(new Date());

                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference notesRef = database.getReference("/user-notes/" + currUser + "/");

                        String key = notesRef.push().getKey();
                        Map<String,Object> childUpdates = new HashMap<>();
                        childUpdates.put(key, note.toMap());

                        notesRef.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError == null) {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Note saved successfully", Toast.LENGTH_LONG);
                                    toast.show();
                                    finish();
                                }
                                else {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "An error occurred while saving the note. Error: "
                                                    + databaseError.getMessage(), Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        });
                    }
                }
            });
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        txt_title.setText(note.getTitle());
        txt_details.setText(note.getDetails());
    }

}
