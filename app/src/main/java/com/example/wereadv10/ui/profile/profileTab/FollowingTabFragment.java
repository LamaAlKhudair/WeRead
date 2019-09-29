package com.example.wereadv10.ui.profile.profileTab;

import android.os.Bundle;
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
import com.example.wereadv10.ui.profile.profileTab.adapter.FollowingAdapter;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FollowingTabFragment extends Fragment {
    private List<User> listData;
    private RecyclerView rv;
    private FollowingAdapter adapter;
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following_tab, container, false);
        rv=view.findViewById(R.id.following_recyclview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        listData=new ArrayList<>();
        Query query = dbSetUp.db.collection("users");
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
        })
/*        FirestoreRecyclerOptions<User> response = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build()*/;


        return view;

    }
}
