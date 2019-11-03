package com.example.wereadv10.ui.clubs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wereadv10.MySharedPreference;
import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.clubs.oneClub.clubPage;
import com.example.wereadv10.ui.profile.profileTab.ProfileSettingActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditClubInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText clubNameET, clubDescriptionET;
    private ImageView clubImage;
    private Button editClubBtn, cancelClubBtn;
    private String image_url = "https://firebasestorage.googleapis.com/v0/b/we-read-a8fd8.appspot.com/o/clubs_images%2Flogo.png?alt=media&token=cdc06ad3-eed9-42e8-977c-32b425bcb98b";
    String clubID, clubOwnerID, clubName, clubDesc;
    private static final int GALLERY = 1;
    private boolean ImgSet = false;
    private FirebaseAuth mAuth;
    private String TAG = EditClubInfoActivity.class.getSimpleName();
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();

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

            editClubBtn.setEnabled(!clubNameS.equals("") && !clubDescriptionS.equals(""));
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
        switch (view.getId()) {
            case R.id.edit_club_ClubImage:
                showPictureDialog();
                break;

            case R.id.edit_club_editClub:
                updateClubInfo();
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

    public void updateClubInfo() {
        // final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(ProfileSettingActivity.this);
        final Map<String, Object> club = new HashMap<>();
        club.put("club_name", clubNameET.getText().toString());
        club.put("club_description", clubDescriptionET.getText().toString());
//
        if (ImgSet) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapDrawable drawable = (BitmapDrawable) clubImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] bytes = baos.toByteArray();
            String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
            final StorageReference ref = dbSetUp.storageRef.child("/clubs_images/" + clubName);

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

                                Uri downUri = task.getResult();
                                image_url = downUri.toString();
                                club.put("club_image", image_url);
                                update(club);
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }else{
            update(club);
        }
//

    }// updateClubInfo()

    private void update(Map<String, Object> club) {
        DocumentReference userClubInfo = dbSetUp.db.collection("clubs").document(clubID);

        userClubInfo
                .update(club)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(getApplicationContext(), "the club information has been update successfully  ", Toast.LENGTH_SHORT).show();

                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        Toast.makeText(getApplicationContext(), "can't update the club now please try again", Toast.LENGTH_SHORT).show();


                     }
                 });
    }//end update()

}//end class
