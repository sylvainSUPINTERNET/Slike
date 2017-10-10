package fr.supinternet.slike;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    Button signUpButton;
    EditText signUpUsernameField;
    EditText signUpPasswordField;
    EditText signUpEmailField;
    EditText loginEmailField;
    EditText loginPasswordField;
    LinearLayout progressBarLayout;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        /*
        if(currentUser != null){
            goToFeedActivity();
            return;
        }
        */

        mDatabase = FirebaseDatabase.getInstance().getReference();

        signUpButton = (Button) findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(signUpListener);


        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(loginListener);

        signUpUsernameField = (EditText) findViewById(R.id.signUp_username);
        signUpEmailField = (EditText) findViewById(R.id.signUp_email);
        signUpPasswordField = (EditText) findViewById(R.id.signUp_password);

        loginEmailField = (EditText) findViewById(R.id.login_email);
        loginPasswordField = (EditText) findViewById(R.id.login_password);

        progressBarLayout = findViewById(R.id.progressBarLayout);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    View.OnClickListener signUpListener = new View.OnClickListener() {
        public void onClick(View v) {
            final String usernameText = signUpUsernameField.getText().toString();
            final String emailText = signUpEmailField.getText().toString();
            final String passwordText = signUpPasswordField.getText().toString();

            if (usernameText.isEmpty()) {
                Toast.makeText(MainActivity.this, "Specify a name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (emailText.isEmpty()) {
                Toast.makeText(MainActivity.this, "Specify a email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (passwordText.isEmpty()) {
                Toast.makeText(MainActivity.this, "Specify a password", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar = findViewById(R.id.progressBar);
            progressBarLayout.setVisibility(View.VISIBLE);

            progressBarLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.grey));

            mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(MainActivity.this.getClass().getName(), "createUserWithEmail:success");
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Authentication success.",
                                        Toast.LENGTH_SHORT).show();

                                mDatabase.child("users").child(firebaseUser.getUid()).child("name").setValue(usernameText);
                                mDatabase.child("users").child(firebaseUser.getUid()).child("email").setValue(firebaseUser.getEmail());

                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "EVENTS_LOGIN_PAGE");
                                bundle.putString("ENTRY_POINT", "SIGNUP");
                                bundle.putString("EMAIL_LENGTH", emailText.length() + "");
                                bundle.putString("PASSWORD_LENGTH", passwordText.length() + "");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


                                goToFeedActivity();
                            } else {
                                // If sign in fails, display a message to the user.
                                progressBarLayout.setVisibility(View.GONE);
                                Log.w(MainActivity.this.getClass().getName(), "createUserWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };


    View.OnClickListener loginListener = new View.OnClickListener() {
        public void onClick(View v) {
            final String emailText = loginEmailField.getText().toString();
            final String passwordText = loginPasswordField.getText().toString();

            if (emailText.isEmpty()) {
                Toast.makeText(MainActivity.this, "Specify a email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (passwordText.isEmpty()) {
                Toast.makeText(MainActivity.this, "Specify a password", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar = findViewById(R.id.progressBar);
            progressBarLayout.setVisibility(View.VISIBLE);

            progressBarLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.grey));

            mAuth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(MainActivity.this.getClass().getName(), "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "EVENTS_LOGIN_PAGE");
                                bundle.putString("ENTRY_POINT", "SIGNIN");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                                goToFeedActivity();
                            } else {
                                // If sign in fails, display a message to the user.
                                progressBarLayout.setVisibility(View.GONE);
                                Log.w(MainActivity.this.getClass().getName(), "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };

    public void showLoading(){

    }

    public void checkEditText(){

    }

    public void goToFeedActivity(){
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }
}
