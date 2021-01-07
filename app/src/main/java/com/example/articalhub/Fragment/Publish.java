package com.example.articalhub.Fragment;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.articalhub.Model.Muser;
import com.example.articalhub.Model.Post;
import com.example.articalhub.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import static android.app.Activity.RESULT_OK;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Publish extends Fragment
{
    List<Muser> mChat;
    RecyclerView recyclerView;
    ValueEventListener seenListener;
    ImageView btn_gallery;
    ImageView art_image;
    Intent intent;
    Button proceed;
    EditText title;
    EditText content;
    private Uri imageUri;
    private StorageReference storageReference;
    FirebaseUser fuser;
    private FirebaseStorage firebaseStorage;
    private StorageTask uploadTask;
    private  static  final int IMAGE_REQUEST=1;
    String mUri="";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.fragment_publish,container,false);
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        title= view.findViewById(R.id.TitleEditText1);
        content=view.findViewById(R.id.ContentId);
        proceed=view.findViewById(R.id.poceedBtn);
        btn_gallery=view.findViewById(R.id.UploadImageId);
        art_image=view.findViewById(R.id.ArticleImageViewId);
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_gallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMAGE_REQUEST);
            }
        });



        proceed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                String tittle=title.getText().toString();
                String cont=content.getText().toString();
                if(!tittle.equals("") && !cont.equals("") && !mUri.equals(""))
                {
                    sendMessage(fuser.getUid(),tittle,cont,mUri);
                }
                else
                {
                    Toast.makeText(getActivity(),"You can't leave any field empty",Toast.LENGTH_LONG).show();
                }
                title.setText("");
                content.setText("");
                art_image.setImageURI(null);
            }
        });
        return  view;
    }




    private void sendMessage(String sender,String title,String content, String mUri)
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference2= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("userID",sender);
        hashMap.put("title",title);
        hashMap.put("content",content);
        hashMap.put("imageURL",mUri);
        reference.child("Post").push().setValue(hashMap);

        Post post;
        post=new Post(title,content,sender,mUri);
        reference2.child("Profile").child(fuser.getUid()).push().setValue(post);
        Toast.makeText(getActivity(), "Your artical sucssfully published", Toast.LENGTH_SHORT).show();
    }


    private void uploadImage()
    {
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Uploading...");
        pd.show();

        if(imageUri !=null)
        {
            Random rand = new Random();
            int upperbound = 250000;
            int int_random=rand.nextInt(upperbound);
            final  StorageReference fileReference= storageReference.child(fuser.getUid()).child("PostImage").child("Article Pic"+int_random);
            uploadTask=fileReference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task task) throws Exception
                {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if(task.isSuccessful())
                    {
                        Uri downloadFile=task.getResult();
                        mUri=downloadFile.toString();
                        pd.dismiss();
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Faild",Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                }
            });
        }
        else
        {
            Toast.makeText(getContext(),"No image selected",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri=data.getData();
            Bitmap bitmap= null;
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageUri);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            art_image.setImageBitmap(bitmap);
            if(uploadTask!=null && uploadTask.isInProgress())
            {
                Toast.makeText(getContext(),"Upload to progress",Toast.LENGTH_LONG).show();
            }
            else
            {
                uploadImage();
            }
        }
    }


}