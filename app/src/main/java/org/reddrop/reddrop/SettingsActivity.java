package org.reddrop.reddrop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class SettingsActivity extends AppCompatActivity {

    private Bitmap compressedBitmap;
    private File file;
    private static final int SELECT_PICTURE = 100;
    private Uri selectedImageUri;
    private ImageView backButtonAccountInfo;
    private ImageView profilePictureImageView;
    private TextView username;
    private TextView email;
    private TextView password;
    private TextView bloodGroup;
    private TextView dataOfBirth;
    private TextView division;
    private TextView city;
    private TextView location;
    private Button saveChangesButton;
    private Bitmap my_image;
    private DatabaseReference userDatabaseReference;
    private StorageReference mStorageRef;
    private String currentUserId;
    private ProgressDialog saveChangesProgressDialog;
    private ProgressDialog loadingProfileProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        compressedBitmap = null;
        try {
            file = File.createTempFile("image", ".jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        backButtonAccountInfo = (ImageView)findViewById(R.id.backButtonActivityLog);
        profilePictureImageView = (ImageView) findViewById(R.id.profile_picture);
        username = (TextView)findViewById(R.id.settings_username);
        email = (TextView)findViewById(R.id.settings_email);
        password = (TextView)findViewById(R.id.settings_password);
        bloodGroup = (TextView)findViewById(R.id.settings_blood_group);
        dataOfBirth = (TextView)findViewById(R.id.settings_date_of_birth);
        division  = (TextView)findViewById(R.id.settings_division);
        city = (TextView)findViewById(R.id.settings_city);
        location = (TextView)findViewById(R.id.settings_location);
        saveChangesButton = (Button) findViewById(R.id.save_changes_button);




        saveChangesProgressDialog = new ProgressDialog(this);
        loadingProfileProgressDialog = new ProgressDialog(this);
        saveChangesProgressDialog.setMessage("Saving changes...");
        loadingProfileProgressDialog.setMessage("Loading...");




        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userDatabaseReference =FirebaseDatabase.getInstance().getReference().child("userinfo").child(currentUserId);
        mStorageRef = FirebaseStorage.getInstance().getReference();  // getting firebase storage reference




        // setting image same as the profile picture of the current user
        try {

            profilePictureImageView.setImageBitmap(BitmapFactory.decodeFile(CurrentSessionData.getInstance()
                            .getCurrentUserProfilePicture().getAbsolutePath()));
            profilePictureImageView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        profilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });


        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChangesProgressDialog.show();
                try {
                    uploadProfilePhoto();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });


        backButtonAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDrawerActivity();
            }
        });

        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                setSettingsPageFields(userInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void launchDrawerActivity(){
        Intent intent = new Intent(this, Drawer.class);
        startActivity(intent);
    }

    private void setSettingsPageFields(UserInfo userInfo){
        username.setText(userInfo.getUsername());
        email.setText(userInfo.getEmail());
        password.setText(userInfo.getPassword());
        bloodGroup.setText(userInfo.getBloodgroup());
        dataOfBirth.setText(userInfo.getDay() + "-" + userInfo.getMonth() + "-" + userInfo.getYear());
        division.setText(userInfo.getDivision());
        city.setText(userInfo.getCity());
        location.setText(userInfo.getLocation());
    }




    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                selectedImageUri = data.getData();
                compressImageData(selectedImageUri);
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);

                    compressImageData(selectedImageUri);
                    System.out.println("AMORON ONOSHON " + selectedImageUri);
                    // Set the image in ImageView
                    profilePictureImageView.setImageBitmap(compressedBitmap);
                }
            }
        }
    }
    
    
    
    private void compressImageData(Uri s){
        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.print("");
            printWriter.close();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, fos);
            compressedBitmap = BitmapFactory.decodeFile(file.getPath());
            System.out.println("COMPRESSED "  + Uri.fromFile(file));
//            selectedImageUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    /* Get the real path from the URI */
    private String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }









    private void uploadProfilePhoto() throws Exception {
        StorageReference imageReference = mStorageRef.child("profile_photo_red_drop/"+ currentUserId + ".jpg");

        System.out.println("BORO DADA " + Uri.fromFile(file));
        try {

            imageReference.putFile(Uri.fromFile(file))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // profile picture successfully uploaded
                            try {
                                downloadProfilePhoto();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            saveChangesProgressDialog.dismiss();
                            Snackbar.make((RelativeLayout)findViewById(R.id.settings_relative_layout),
                                    "Changes saved", Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            saveChangesProgressDialog.dismiss();
                            Toast.makeText(SettingsActivity.this, "Unsuccessful attempt to save changes", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception e){
            System.out.println("NO PROFILE PICTURE FOUND");
        }
    }


    private void downloadProfilePhoto() throws Exception{

        StorageReference imageReference = mStorageRef.child("profile_photo_red_drop/"+  currentUserId + ".jpg");
        final File localFile = File.createTempFile("images", "jpg");
        imageReference.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        CurrentSessionData.getInstance().setCurrentUserProfilePicture(localFile);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }




}
