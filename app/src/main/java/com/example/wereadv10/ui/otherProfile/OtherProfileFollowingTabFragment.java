package com.example.wereadv10.ui.otherProfile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.clubs.oneClub.MembersAdapter;
import com.example.wereadv10.ui.profile.profileTab.User;
import com.example.wereadv10.ui.profile.profileTab.adapter.FollowingAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OtherProfileFollowingTabFragment extends Fragment  {
    private  FirebaseUser userF;
    private int[] sampleImages = new int[5];
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
    TextView followingNumTV,followersNumTV;
    private com.example.wereadv10.dbSetUp dbSetUp;
    private String TAG = OtherProfileFollowingTabFragment.class.getSimpleName();
    Button followBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_profile_following_tab, container, false);
        followBtn = ((OtherProfileActivity) getActivity()).followBtn;
        userF= FirebaseAuth.getInstance().getCurrentUser();


        //followings and followers num
        followersNumTV = view.findViewById(R.id.other_profile_followers_num);
        followingNumTV = view.findViewById(R.id.other_profile_following_num);

        dbSetUp = new dbSetUp();

        sampleImages[0] = R.drawable.man;
        sampleImages[1] = R.drawable.girl;
        //followers
        rvFollowers = view.findViewById(R.id.other_profile_followers_recyclerview);
        Followers_LayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFollowers.setLayoutManager(Followers_LayoutManager);
        getFollowers();
        //followings
        rvFollowing = view.findViewById(R.id.other_profile_followings_recyclerview);
        Following_LayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFollowing.setLayoutManager(Following_LayoutManager);
        getFollowings();
        return view;

    }
    //get following user
    private void getFollowings() {

        String otherUserID = "";
        if (getActivity().getIntent().getExtras().getString("otherUserID") != null)
            otherUserID = getActivity().getIntent().getExtras().getString("otherUserID");

        dbSetUp.db.collection("following")
                .whereEqualTo("followed_by_id", otherUserID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Following_adapter = new MembersAdapter(getContext(), Followings);
                            rvFollowing.setAdapter(Following_adapter);

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //get book id
                                String followedByID = document.get("followed_id").toString();

                                getUsersFollowings(followedByID);
                            }
//                            followingNumTV.setText(Followings.size());
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
/*                if (userF.getUid().toString().equals(user.getId()))
                    followBtn.setText("unFollow");*/

                Followings.add(user);
                int number = Followings.size();
                String numberST= "("+number+")";
                followingNumTV.setText( numberST);
                Following_adapter.notifyDataSetChanged();
            }
        });
    }//end getUsersFollowings()


    //get followers user
    private void getFollowers() {

        String otherUserID = "";
        if (getActivity().getIntent().getExtras().getString("otherUserID") != null)
            otherUserID = getActivity().getIntent().getExtras().getString("otherUserID");

        dbSetUp.db.collection("following")
                .whereEqualTo("followed_id", otherUserID)
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

                                getUsersFollowers(followedByID);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        ////

    }//end getFollowers()

    private void getUsersFollowers(String followedByID) {

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
                if (userF.getUid().equals(user.getId()))
                    followBtn.setText("unFollow");

                Followers.add(user);
                int number = Followers.size();
                String numberST= "("+number+")";
                followersNumTV.setText( numberST);

                Followers_adapter.notifyDataSetChanged();
            }
        });
    }//end getUsersFollowers()

}
