package com.example.wereadv10;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class dbSetUp {
    public FirebaseAuth mAuth;
    public FirebaseFirestore db ;

    public dbSetUp(){
        db = FirebaseFirestore.getInstance();
    }


}
