package fr.supinternet.slike;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAlertDialogFragment extends DialogFragment {

    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;
    private String username;
    private EditText textMessage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("users").child(firebaseUser.getUid()).child("username").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.addListenerForSingleValueEvent(postListener);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_message, null))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        textMessage = getDialog().findViewById(R.id.message);

                        writeNewPost(username,textMessage.getText().toString());

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MyAlertDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void writeNewPost(String user, String message) {
        String key = mDatabase.child("messages").push().getKey();
        Message post = new Message(key, user, message);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/messages/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
}
