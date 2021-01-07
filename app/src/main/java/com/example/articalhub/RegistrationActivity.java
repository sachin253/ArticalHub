package com.example.articalhub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.articalhub.Model.Muser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity
{
    FirebaseAuth fAuth;
    DatabaseReference dbrefrence;
    MaterialEditText userName,email,password,cPassword;
    String Semail, Sname,ScPassword, Spassword,userId;
    private  String mUri="default";
    Button RegBtn;
    ImageView UploadImage;
    CircleImageView pImage;
    FirebaseStorage firebaseStorage;
    private  static  final int IMAGE_REQUEST=1;
    private Uri imageUri;
    private  StorageReference storagReference;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userName=findViewById(R.id.userNameId);
        email=findViewById(R.id.emaiId);
        password=findViewById(R.id.passwordId);
        cPassword=findViewById(R.id.conformPasswordId);
        RegBtn=findViewById(R.id.regBtnId);
        UploadImage=findViewById(R.id.RUploadImageId);
        pImage=findViewById(R.id.registration_image);
        fAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storagReference=firebaseStorage.getReference();


        RegBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(validate()){
                    String user_email = email.getText().toString().trim();
                    String user_password = password.getText().toString().trim();

                    fAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {

                                sendEmailVerification();
                            }
                            else
                            {
                                Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        UploadImage.setOnClickListener(new View.OnClickListener()
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

    }
    private Boolean validate()
    {
        Boolean result = false;

        Sname = userName.getText().toString();
        Spassword = password.getText().toString();
        Semail = email.getText().toString();
        ScPassword=cPassword.getText().toString();



        if(Sname.isEmpty() || Spassword.isEmpty() || Semail.isEmpty() || ScPassword.isEmpty() || imageUri == null )
        {
            if(imageUri == null)
            {
                Toast.makeText(this, "Please select profile image", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }

        else
        {
            result = true;
        }

        return result;
    }


    private void sendEmailVerification()
    {
        final FirebaseUser firebaseUser =fAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegistrationActivity.this, "Verification mail sent!", Toast.LENGTH_LONG).show();
                        userId=firebaseUser.getUid();
                        sendUserData();
                        fAuth.signOut();
                        finish();
                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                    }else
                    {

                        Toast.makeText(RegistrationActivity.this, "Verification mail has'nt been sent!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void sendUserData()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        dbrefrence = firebaseDatabase.getReference("UserData");
        final StorageReference imageRefrence= storagReference.child(fAuth.getUid()).child("Images").child("Profile Pic");


        uploadTask = imageRefrence.putFile(imageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener()
        {
            @Override
            public void onSuccess(Object o)
            {
                Toast.makeText(RegistrationActivity.this, "Image uploded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(RegistrationActivity.this, "Image not uploded", Toast.LENGTH_SHORT).show();

            }
        });

        Muser user;
        if(imageUri== null)
        {
            user=new Muser(Semail,Sname,"default",userId);
        }

        else
        {
            user=new Muser(Semail,Sname,imageUri.toString(),userId);
        }

        dbrefrence.child(userId).setValue(user);
        Toast.makeText(this, "Data successfully stored in database", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri=data.getData();

            try
            {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);

                pImage.setImageBitmap(bitmap);


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}