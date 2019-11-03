package com.example.wereadv10.ui.clubs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wereadv10.MySharedPreference;
import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.clubs.oneClub.clubPage;
import com.example.wereadv10.ui.profile.profileTab.ProfileSettingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;

public class EditClubInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText clubNameET, clubDescriptionET;
    private ImageView clubImage;
    private Button editClubBtn, cancelClubBtn;
    private  String image_url = "https://firebasestorage.googleapis.com/v0/b/we-read-a8fd8.appspot.com/o/clubs_images%2Flogo.png?alt=media&token=cdc06ad3-eed9-42e8-977c-32b425bcb98b";
String clubID,clubOwnerID,clubName,clubDesc;
    private static final int GALLERY = 1;
    private boolean ImgSet = false;
    private FirebaseAuth mAuth;
    private String TAG = EditClubInfoActivity.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_club_info);
        initToolBar();
        clubNameET = findViewById(R.id.edit_club_clubName);
        clubDescriptionET = findViewById(R.id.edit_club_clubDescription);
        clubImage = findViewById(R.id.edit_club_ClubImage);
        editClubBtn = findViewById(R.id.edit_club_editClub);
        cancelClubBtn = findViewById(R.id.edit_club_cancelCreate);

        clubNameET.addTextChangedListener(EditTextWatcher);
        clubDescriptionET.addTextChangedListener(EditTextWatcher);

        clubImage.setOnClickListener(this);
        editClubBtn.setOnClickListener(this);
        cancelClubBtn.setOnClickListener(this);
        getExtras();
    }//end onCreate()
    private TextWatcher EditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String clubNameS = clubNameET.getText().toString().trim();
            String clubDescriptionS = clubDescriptionET.getText().toString().trim();

            editClubBtn.setEnabled(!clubNameS.equals("") && !clubDescriptionS.equals("") && (ImgSet == true) );
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
    }//end onSupportNavigateUp()


    private void initToolBar() {
        setTitle("Edit club information");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }//end initToolBar()

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_club_ClubImage:
                showPictureDialog();
                break;

            case R.id.edit_club_editClub:

                break;
            case R.id.edit_club_cancelCreate:
                AlertDialog alertDialog = new AlertDialog.Builder(EditClubInfoActivity.this).create();
                alertDialog.setMessage("Are you sure you want to leave?");
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
                break;


        }//end switch
    }
    private void getExtras() {

        Intent intent = getIntent();
        if (intent.getExtras() != null) {

            clubID = intent.getExtras().getString("CLUB_ID");
            if (intent.getExtras().getString("NAME") != null) {
                clubName = intent.getExtras().getString("NAME");
                clubNameET.setText(clubName);
            }
            if (intent.getExtras().getString("OWNER_ID") != null)
                clubOwnerID = intent.getExtras().getString("OWNER_ID");
            if (intent.getExtras().getString("DESCRIPTION") != null) {
                clubDesc = intent.getExtras().getString("DESCRIPTION");
                clubDescriptionET.setText(clubDesc);
            }
            if (intent.getExtras().getString("IMAGE") != null)
                Glide.with(EditClubInfoActivity.this).load(intent.getExtras().getString("IMAGE")).into(clubImage);

        }
    }//end getExtras()

    public void updateClubInfo(final String name) {
        DocumentReference userName = db.collection("clubs").document(clubID);
       // final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(ProfileSettingActivity.this);

// Set the "isCapital" field of the city 'DC'
        userName
                .update("club_name", name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //update FirebaseUser profile

                        FirebaseUser userf = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        userf.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User profile updated.");
/*                                            nameTV.setText(name);
                                            MySharedPreference.putString(ProfileSettingActivity.this, "userName", name);
                                            dialog.setMessage("the name is update");
                                            dialog.setPositiveButton("ok", null);
                                            dialog.show();*/
                                        }
                                    }
                                });
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
/*                        dialog.setMessage("can't update the name now please try again");
                        dialog.setPositiveButton("ok", null);
                        dialog.show();*/

                    }
                });
    }// updateName()
}//end class
