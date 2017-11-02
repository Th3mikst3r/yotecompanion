package com.aura.YoteCompanion;


import com.aura.YoteCompanion.Authentication.LogoutActivity;
import com.aura.YoteCompanion.NoteActivities.NotesList;
import com.aura.YoteCompanion.SettingsActivites.SetTest;
import com.getbase.floatingactionbutton.FloatingActionButton;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private TextView numOfNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

       // numOfNotes = (TextView) findViewById(R.id.txtview_numOfNotes);
        //lstView.getAdapter().getCount() ,
       // numOfNotes.setText();

        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_reminder);
        actionA.setTitle("Habits");
        actionA.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Habits Was clicked", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

}