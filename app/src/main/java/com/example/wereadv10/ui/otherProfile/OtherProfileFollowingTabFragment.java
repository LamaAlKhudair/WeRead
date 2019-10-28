package com.example.wereadv10.ui.otherProfile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.notification.Data;
import com.example.wereadv10.notification.Sender;
import com.example.wereadv10.notification.Token;
import com.example.wereadv10.ui.clubs.oneClub.MembersAdapter;
import com.example.wereadv10.ui.profile.profileTab.User;
import com.example.wereadv10.ui.profile.profileTab.adapter.FollowingAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherProfileFollowingTabFragment extends Fragment {
    private FirebaseUser userF;
    private int[] sampleImages = new int[5];
    // Followers recycler view
    private RecyclerView rvFollowers;
    MembersAdapter Followers_adapter;
    private RecyclerView.LayoutManager Followers_LayoutManager;
    List<User> Followers = new ArrayList<>();
    // Following recycler view
    private RecyclerView rvFollowing;
    private MembersAdapter Following_adapter;
    private RecyclerView.LayoutManager Following_LayoutManager;
    private List<User> Followings = new ArrayList<>();

    //followings and followers num
    TextView followingNumTV, followersNumTV;
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();
    private String TAG = OtherProfileFollowingTabFragment.class.getSimpleName();
    Button followBtn;
    String otherUserID = "";

    //volley
    private RequestQueue requestQueue;
    private boolean notify = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_profile_following_tab, container, false);
        followBtn = ((OtherProfileActivity) getActivity()).followBtn;
        userF = FirebaseAuth.getInstance().getCurrentUser();
        requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());

        if (getActivity().getIntent().getExtras().getString("otherUserID") != null)
            otherUserID = getActivity().getIntent().getExtras().getString("otherUserID");

        //followings and followers num
        followersNumTV = view.findViewById(R.id.other_profile_followers_num);
        followingNumTV = view.findViewById(R.id.other_profile_following_num);

        //dbSetUp = new dbSetUp();

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
        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (followBtn.getText().toString().equals("Follow")) {
                    notify = true;
                    followUser();
                } else {
                    unFollowUser();
                }
            }
        });
        return view;

    }

    private void followUser() {
        //to send notification when user follow other user

        final DocumentReference docRef = dbSetUp.db.collection("users").document(userF.getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    String userName = snapshot.getString("name");
                    if (notify) {
                        sendNotification(otherUserID, userName);
                    }
                    notify = false;

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }


        });

        ///
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final Map<String, Object> follow = new HashMap<>();
        follow.put("followed_by_id", user.getUid());//uerId
        follow.put("followed_id", otherUserID);//otherUser
        dbSetUp.db.collection("following")
                .add(follow)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        followBtn.setText("Unfollow");
                        Followers.clear();
                        getFollowers();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }//end followUser()


    private void sendNotification(final String otherUserID, final String userName) {
///

        dbSetUp.db.collection("Tokens")
                .document(otherUserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getString("token") != null) {
                            //  cities.add(doc.getString("name"));
                            Token token = new Token(documentSnapshot.getString("token"));
                            Data data = new Data(userF.getUid(), userName + ":" + "is now follow you", "New followers", otherUserID, R.drawable.logo);

                            Sender sender = new Sender(data, token.getToken());
                            //from json object request
                            try {
                                JSONObject senderJsonObj = new JSONObject(new Gson().toJson(sender));
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", senderJsonObj,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                //respose of the request
                                                Log.d("JSON_RESPONSE", "onResponse: " + response.toString());

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("JSON_RESPONSE", "onResponse: " + error.toString());

                                    }
                                }) {
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        //put params
                                        Map<String, String> headers = new HashMap<>();
                                        headers.put("Content-Type", "application/json");
                                        headers.put("Authorization", "key=AAAAyQvrTlo:APA91bGN0XRUWpYsynAYyOkiEzP1tmR9pgWsm2lloHhfTxYim3QblFBGLmIP6An9mDA67hX451si1b7O3kLnspv9shwmj7PrJidrtiY4cmov_67uevHeNnrxGxyFmnoOACNVmB3znBQT");


                                        return headers;
                                    }
                                };
                                //add the request to queue
                                requestQueue.add(jsonObjectRequest);


                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }

                });


    }//end sendNotification

    private void unFollowUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dbSetUp.db.collection("following")
                .whereEqualTo("followed_id", otherUserID)
                .whereEqualTo("followed_by_id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //get book id
                                String docId = document.getId();
                                deleteDoc(docId);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
    }//end unFollowUser()

    private void deleteDoc(String id) {
        dbSetUp.db.collection("following").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        followBtn.setText("Follow");
                        Followers.clear();
                        getFollowers();

                        //           followingTabFragment.updateFollowersList(otherUserID);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "pleas try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //get following user
    private void getFollowings() {


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
                String numberST = "(" + number + ")";
                followingNumTV.setText(numberST);
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
                String numberST = "(" + number + ")";
                followersNumTV.setText(numberST);

                Followers_adapter.notifyDataSetChanged();
            }
        });
    }//end getUsersFollowers()

    //get followers user
    private void getFollowers(String otherUserID) {

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

    public void updateFollowersList(String otherUserID) {
        Followers.clear();
        getFollowers(otherUserID);
    }//end updateFollowersList()

}
