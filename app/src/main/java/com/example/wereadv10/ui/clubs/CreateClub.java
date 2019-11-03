package com.example.wereadv10.ui.clubs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateClub extends AppCompatActivity {


    private EditText clubName, clubDescription;
    private ImageView clubImage;
    private Button createClub, cancelClub;
    private String clubId, ownerId;
    private  String image_url = "https://firebasestorage.googleapis.com/v0/b/we-read-a8fd8.appspot.com/o/clubs_images%2Flogo.png?alt=media&token=cdc06ad3-eed9-42e8-977c-32b425bcb98b";

    private static final int GALLERY = 1;
    private boolean ImgSet = false;

    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);

        clubName = findViewById(R.id.clubName);
        clubDescription = findViewById(R.id.clubDescription);
        clubImage = findViewById(R.id.UploadClubImage);
        createClub = findViewById(R.id.createClub);
        cancelClub = findViewById(R.id.cancelCreate);

        clubName.addTextChangedListener(loginTextWatcher);
        clubDescription.addTextChangedListener(loginTextWatcher);

        clubImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();

            }
        });

        createClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClub();
            }
        });
        initToolBar();
        cancelClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(CreateClub.this).create();
                alertDialog.setMessage("Discard this club?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String clubNameS = clubName.getText().toString().trim();
            String clubDescriptionS = clubDescription.getText().toString().trim();

            createClub.setEnabled(!clubNameS.equals("") && !clubDescriptionS.equals("") && (ImgSet == true) );
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        //title
        String[] pictureDialogItems = {
                getString(R.string.select_photo_from_gallery)};

        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                        }//end switch
                    }//end onClick
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void addClub(){

        if( !clubName.getText().toString().equals("") && !clubDescription.getText().toString().equals("") && (ImgSet == true) )
        {

            ownerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            clubId = getRandomID();

            final Map<String, Object> club = new HashMap<>();
            club.put("club_name", clubName.getText().toString());
            club.put("club_description", clubDescription.getText().toString());
            club.put("club_owner", ownerId);
            club.put("club_id", clubId);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapDrawable drawable = (BitmapDrawable) clubImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] bytes = baos.toByteArray();
            String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
            final StorageReference ref = dbSetUp.storageRef.child("/clubs_images/"+clubName.getText().toString());

            final UploadTask uploadTask = ref.putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()) {
                                Uri downUri  = task.getResult();
                                image_url = downUri .toString();
                                club.put("club_image", image_url);
                                Log.d("Final URL", "onComplete: Url: " + downUri.toString());
                                dbSetUp.db.collection("clubs").document(clubId)
                                        .set(club)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                addOwnerAsMember();
                                                Toast.makeText(CreateClub.this, "Club Created Successfully", Toast.LENGTH_LONG).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Error writing document", e);
                                            }
                                        });

                                finish();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });



        }
        else{
            Toast.makeText(getApplicationContext(),"You Cannot Leave This Empty!",Toast.LENGTH_SHORT).show();
        }
    }

    private void addOwnerAsMember(){

        final Map<String, Object> joinMember = new HashMap<>();
        joinMember.put("member_id", ownerId);
        joinMember.put("club_id", clubId);
        dbSetUp.db.collection("club_members")
                .document(getRandomID())
                .set(joinMember).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error writing document", e);
                    }
                });

    }

    private String getRandomID(){
        return UUID.randomUUID().toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_CANCELED) {
            return;
        }//end if

        final Uri contentURI = data.getData();
        if (requestCode == GALLERY) {

            if (data != null) {

                try {
                    final InputStream inputStream = getContentResolver().openInputStream(contentURI);
                    final Bitmap bm = BitmapFactory.decodeStream(inputStream);
                    clubImage.setImageBitmap(bm);
                    ImgSet = true;
                }//end try
                catch (IOException e) {
                    e.printStackTrace();
                }//end catch

            }//end if

        }
    }//end onActivityResult

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initToolBar() {
        setTitle("Create Club");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
