package com.aura.YoteCompanion.SettingsActivites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.Toast;

import com.aura.YoteCompanion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class DeleteAllNotesDialogPreference extends DialogPreference{

    private DatabaseReference notesRef;
    private FirebaseAuth auth;

    public DeleteAllNotesDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.xml.preferences);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    @Override
    protected void onClick() {

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.getDisplayName();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        notesRef = database.getReference("/Users/" + user.getUid() + "/");

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Delete All Notes?");
        dialog.setMessage("This action will delete all your data. Are you sure you want to continue?");
        dialog.setCancelable(true);

        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notesRef.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Deleted All Notes For " + user, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getContext(), "Unable to delete Notes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).setNegativeButton("Cancel", null);
        AlertDialog al = dialog.create();
        al.show();
    }

}