package com.aura.YoteCompanion.SettingsActivites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.widget.Toast;

import com.aura.YoteCompanion.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteAllNotesDialogPreference extends DialogPreference{

    private DatabaseReference mDeleteUserNoteDB;
    public DeleteAllNotesDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.xml.preferences);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    @Override
    protected void onClick() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.getDisplayName();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Delete All Notes?");
        dialog.setMessage("This action will delete all your data. Are you sure you want to continue?");
        dialog.setCancelable(true);

        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    mDeleteUserNoteDB = FirebaseDatabase.getInstance().getReference().child("/Notes/" + user.getUid() + "/");
                    mDeleteUserNoteDB.removeValue();
                    Toast.makeText(getContext(), "Deleted Successfully for " + user, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Failed to delete....", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }).setNegativeButton("Cancel", null);
        AlertDialog al = dialog.create();
        al.show();
    }

}