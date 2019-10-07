package com.example.wereadv10.ui.otherProfile;

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
import com.example.wereadv10.ui.profile.profileTab.User;
import com.example.wereadv10.ui.profile.profileTab.adapter.FollowingAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OtherProfileFollowingTabFragment extends Fragment implements FollowingAdapter.OnFollowListener{
    private List<User> listData;
    private RecyclerView rv;
    private FollowingAdapter adapter;
    private com.example.wereadv10.dbSetUp dbSetUp;
    private String TAG = OtherProfileFollowingTabFragment.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_profile_following_tab, container, false);
        rv=view.findViewById(R.id.other_profile_following_recyclview);

        dbSetUp = new dbSetUp();
                Toast.makeText(getContext(), "yesss", Toast.LENGTH_LONG).show();
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        listData=new ArrayList<>();
        getFollowers();

        return view;

    }

    //get followers user
    private void getFollowers() {

        String otherUserID = "";
        if (getActivity().getIntent().getExtras().getString("otherUserID")!=null)
            otherUserID = getActivity().getIntent().getExtras().getString("otherUserID");
            Toast.makeText(getContext(), otherUserID, Toast.LENGTH_SHORT).show();

            dbSetUp.db.collection("following")
                    .whereEqualTo("followed_id", otherUserID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                adapter = new FollowingAdapter(getContext(), listData, OtherProfileFollowingTabFragment.this);
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

    }//end getFollowers()
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
    public void onFollowUserClick(String userID,String userEmail) {
        Intent intent = new Intent(getActivity(), OtherProfileActivity.class);
        intent.putExtra("otherUserEmail", userEmail);
        intent.putExtra("otherUserID", userID);

        startActivity(intent);
    }
}
