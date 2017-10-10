package fr.supinternet.slike;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by guillaume on 26/09/2017.
 */

public class FirebaseUtils {

    public void signUp(){

    }

    private static String getUid(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser == null ? null : currentUser.getUid();
    }

    public static void login(){

    }

    private static DatabaseReference getRef(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference();
    }
}
