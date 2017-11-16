package com.aura.YoteCompanion.NoteActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aura.YoteCompanion.Models.Note;
import com.aura.YoteCompanion.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewNote extends AppCompatActivity {
    private TextView lbl_title;
    private TextView lbl_details;
    private TextView lbl_saved_at;
    private Button btn_delete_note;
    private FirebaseUser mFirebaseUser;
    DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent intent = getIntent();
        final Note note = (Note) intent.getSerializableExtra("Note");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final String noteId = note.getNoteId();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("/Users/" + mFirebaseUser.getUid() + "/").child(noteId);


        lbl_details = (TextView) findViewById(R.id.lbl_note_details);
        lbl_title = (TextView) findViewById(R.id.lbl_note_title) ;
        lbl_saved_at = (TextView) findViewById(R.id.lbl_saved_at);
        btn_delete_note= (Button) findViewById(R.id.btndeletenote);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EditNote.class);
                intent.putExtra("Note", note);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            lbl_title.setText(note.getTitle());
            lbl_details.setText(note.getDetails());
            lbl_saved_at.setText(note.getDateSaved().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        lbl_title.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), EditNote.class);
                in.putExtra("Note", note);
                startActivity(in);
            }
        });
        lbl_details.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), EditNote.class);
                in.putExtra("Note", note);
                startActivity(in);
            }
        });

        btn_delete_note.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Toast.makeText(ViewNote.this, noteId, Toast.LENGTH_LONG).show();
                //mDatabase.removeValue();
                //Toast.makeText(ViewNote.this, "Note deleted...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
