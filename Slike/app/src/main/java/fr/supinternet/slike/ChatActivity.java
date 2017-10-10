package fr.supinternet.slike;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private List<Message> postsList = new ArrayList<>();
    private RecyclerView rvList;
    private Adapter adapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        rvList = (RecyclerView) findViewById(R.id.rvMessages);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rvList.setLayoutManager(manager);
        adapter = new Adapter(this);
        rvList.setAdapter(adapter);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        getMessages();
        currentUser = mAuth.getCurrentUser();



    }

    public void onClick(View view) {
        DialogFragment dialog = new MyAlertDialogFragment();
        dialog.show(getFragmentManager(), "MyAlertDialogFragment");
    }

    public void getMessages() {
        Query postsQuery = mDatabase.child("messages")
                .limitToLast(100);
        postsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);

                if (message != null) {
                    adapter.addMessage(message);

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "EVENTS_HOMEPAGE");
                    bundle.putString("MESSAGE_COUNT", rvList.getAdapter().getItemCount() + "");

                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(Message.class);

                if (message != null) {
                    adapter.removeMessage(message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}