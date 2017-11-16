package com.aura.YoteCompanion.SettingsActivites;

import android.content.Context;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.EditText;

import com.aura.YoteCompanion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends DialogPreference {

    private FirebaseAuth auth;
    private EditText newPassword;

    public ChangePassword(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Set the layout here
        setDialogLayoutResource(R.xml.preferences);

       /* setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);*/

        setDialogIcon(null);
    }

    @Override
    protected void onClick() {

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && !newPassword.getText().toString().trim().equals("")) {
            if (newPassword.getText().toString().trim().length() < 6) {
                newPassword.setError("Password too short, enter minimum 6 characters");
            } else {
                user.updatePassword(newPassword.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    signOut();
                                } else {
                                    newPassword.setError("Failed");
                                }
                            }
                        });
            }
        } else if (newPassword.getText().toString().trim().equals("")) {
            newPassword.setError("Enter password");
        }
    }

    public void signOut() {
        auth.signOut();
    }

}