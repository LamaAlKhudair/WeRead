package com.example.wereadv10.ui.profile.profileTab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.otherProfile.OtherProfileActivity;
import com.example.wereadv10.ui.profile.profileTab.adapter.FollowingAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FollowingTabFragment extends Fragment implements FollowingAdapter.OnFollowListener{
    private List<User> listData;
    private RecyclerView rv;
    private FollowingAdapter adapter;
    private com.example.wereadv10.dbSetUp dbSetUp;
    private String TAG = FollowingTabFragment.class.getSimpleName();
    private FirebaseUser user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following_tab, container, false);
        rv=view.findViewById(R.id.following_recyclview);

        dbSetUp = new dbSetUp();
        user = FirebaseAuth.getInstance().getCurrentUser();

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        listData=new ArrayList<>();
        getFollowers();
/*        Query query = dbSetUp.db.collection("users");
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!= null){
                    Toast.makeText(getContext(), "something error",
                            Toast.LENGTH_LONG).show();
                    return;
                }//end if
                // Convert query snapshot to a list of chats

                List<User> users = queryDocumentSnapshots.toObjects(User.class);
                adapter=new FollowingAdapter(getContext(),users);
                rv.setAdapter(adapter);
//                Toast.makeText(getContext(), "yesss",
//                        Toast.LENGTH_LONG).show();
            }
        })*/
/*        FirestoreRecyclerOptions<User> response = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build()*/;


        return view;

    }
    //toRead Book
    private void getFollowers() {

        String userId = user.getUid();
        dbSetUp.db.collection("following")
                .whereEqualTo("followed_id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            adapter=new FollowingAdapter(getContext(),listData,FollowingTabFragment.this);
                            rv.setAdapter(adapter);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //get book id

                                String followedByID = document.get("followed_by_id").toString();
                                getUsers(followedByID);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        ////

    }//end getToReadBook()
    private void getUsers(String followedByID){
        final DocumentReference docRef = dbSetUp.db.collection("users").document(followedByID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                user.setId(docRef.getId());
                listData.add(user);
                adapter.notifyDataSetChanged();
            }
        });
    }//end getUsers()

    @Override
    public void onFollowUserClick(String otherUserID,String userEmail) {
        Intent intent = new Intent(getActivity(), OtherProfileActivity.class);
        intent.putExtra("otherUserEmail", userEmail);
        intent.putExtra("otherUserID", otherUserID);
        startActivity(intent);
    }
}
