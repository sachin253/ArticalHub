package com.example.articalhub.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.articalhub.Adapter.HomeAdapter;
import com.example.articalhub.Model.Muser;
import com.example.articalhub.Model.Post;
import com.example.articalhub.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class Profile extends Fragment
{
    RecyclerView recyclerView;
    private List<Post> post;
    FirebaseUser fuser;
    DatabaseReference reference;
    HomeAdapter homeAdapter;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    private  static  final int IMAGE_REQUEST=1;
    private Uri imageUri;
    private StorageTask uploadTask;
    ImageView uploadImage;
    CircleImageView ImageURL;
    TextView UserName;
    DatabaseReference reference1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // profile information
        ImageURL=view.findViewById(R.id.PFprofile_image);
        uploadImage=view.findViewById(R.id.UploadImageId);
        UserName=view.findViewById(R.id.PFusernameTV);
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference1= FirebaseDatabase.getInstance().getReference("UserData").child(fuser.getUid());

       reference1.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                try{
                   // Muser u=snapshot.getValue(Muser.class);
                    String s=snapshot.getValue(Muser.class).getUserName();
                    UserName.setText(s);
                  //  Glide.with(getContext()).load(u.getImageURL()).into(ImageURL);


                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Own post
        recyclerView = view.findViewById(R.id.ProfileRvId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        post = new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("Profile").child(fuser.getUid());

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