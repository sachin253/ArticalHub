package com.example.articalhub.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.articalhub.Adapter.HomeAdapter;
import com.example.articalhub.Model.Post;
import com.example.articalhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Home extends Fragment
{

    RecyclerView recyclerView;
    private List<Post> post;
    FirebaseUser fuser;
    DatabaseReference reference;
    HomeAdapter homeAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.ChatsRvId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        post = new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("Post");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                post.clear();
                if (snapshot.exists())
                {
                    for (DataSnapshot npsnapshot : snapshot.getChildren())
                    {
                        Post l = npsnapshot.getValue(Post.class);
                        post.add(l);
                    }
                    Collections.reverse(post);
                    homeAdapter = new HomeAdapter(getContext(),post);
                    recyclerView.setAdapter(homeAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
           return view;
        }
    }


