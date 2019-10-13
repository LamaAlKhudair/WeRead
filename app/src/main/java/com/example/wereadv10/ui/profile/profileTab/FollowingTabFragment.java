package com.example.wereadv10.ui.profile.profileTab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.clubs.oneClub.MembersAdapter;
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

public class FollowingTabFragment extends Fragment {
    /*    private List<User> listData;
        private RecyclerView rv;
        private FollowingAdapter adapter;*/
    private int[] sampleImages = new int[5];

    private com.example.wereadv10.dbSetUp dbSetUp;
    private String TAG = FollowingTabFragment.class.getSimpleName();
    private FirebaseUser user;
    // Followers recycler view
    private RecyclerView rvFollowers;
    private MembersAdapter Followers_adapter;
    private RecyclerView.LayoutManager Followers_LayoutManager;
    private List<User> Followers = new ArrayList<>();
    // Following recycler view
    private RecyclerView rvFollowing;
    private MembersAdapter Following_adapter;
    private RecyclerView.LayoutManager Following_LayoutManager;
    private List<User> Followings = new ArrayList<>();
    //followings and followers num
    TextView followingNumTV, followersNumTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following_tab, container, false);
//        rv=view.findViewById(R.id.following_recyclview);

        dbSetUp = new dbSetUp();
        user = FirebaseAuth.getInstance().getCurrentUser();
        followersNumTV = view.findViewById(R.id.profile_followers_num);
        followingNumTV = view.findViewById(R.id.profile_following_num);
        sampleImages[0] = R.drawable.man;
        sampleImages[1] = R.drawable.girl;
        //followers
        rvFollowers = view.findViewById(R.id.profile_followers_recyclerview);
        Followers_LayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFollowers.setLayoutManager(Followers_LayoutManager);
        getFollowers();
        //followings
        rvFollowing = view.findViewById(R.id.profile_followings_recyclerview);
        Following_LayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFollowing.setLayoutManager(Following_LayoutManager);
        getFollowings();
/*        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        listData=new ArrayList<>();*/
        getFollowers();


        return view;

    }

    //get followers
    private void getFollowers() {

        String userId = user.getUid();
        dbSetUp.db.collection("following")
                .whereEqualTo("followed_id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Followers_adapter = new MembersAdapter(getContext(), Followers);
                            rvFollowers.setAdapter(Followers_adapter);
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

    private void getUsers(String followedByID) {
        final DocumentReference docRef = dbSetUp.db.collection("users").document(followedByID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                user.setId(docRef.getId());
                int random;
                if (Math.random() < 0.5)
                    random = 0;
                else random = 1;

                int member_image = sampleImages[random];
                user.setImage(member_image);
                Followers.add(user);
                int number = Followers.size();
                String numberST = "(" + number + ")";
                followersNumTV.setText(numberST);
                Followers_adapter.notifyDataSetChanged();
            }
        });
    }//end getUsers()

    //get following user
    private void getFollowings() {
        final FirebaseUser userF = FirebaseAuth.getInstance().getCurrentUser();

        dbSetUp.db.collection("following")
                .whereEqualTo("followed_by_id", userF.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Following_adapter = new MembersAdapter(getContext(), Followings);
                            rvFollowing.setAdapter(Following_adapter);

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String followedByID = document.get("followed_id").toString();

                                getUsersFollowings(followedByID);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        ////

    }//end getFollowings()

    private void getUsersFollowings(String followedByID) {

        final DocumentReference docRef = dbSetUp.db.collection("users").document(followedByID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                user.setId(docRef.getId());
                int random;
                if (Math.random() < 0.5)
                    random = 0;
                else random = 1;

                int member_image = sampleImages[random];
                user.setImage(member_image);

                Followings.add(user);
                int number = Followings.size();
                String numberST = "(" + number + ")";
                followingNumTV.setText(numberST);
                Following_adapter.notifyDataSetChanged();
            }
        });
    }//end getUsersFollowings()

}
