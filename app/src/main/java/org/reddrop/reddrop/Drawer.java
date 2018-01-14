package org.reddrop.reddrop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private TextView navHeaderUsername;
    private TextView navHeaderEmail;
    private ImageView navHeaderImageView;
    private String requiredBloodGroup;
    private CurrentSessionData currentSession = CurrentSessionData.getInstance();

    private Button findDonorButton;
    private Spinner bloodGroupSpinner;
    private ProgressDialog progressDialog;

    private Bitmap my_image;

    private String currentUid;

    private DatabaseReference userDatabaseReference;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(Drawer.this);
        progressDialog.setMessage("Loading...");
        if(currentSession.getCurrentUserInfo() == null){
            System.out.println("DOWN----");
            progressDialog.show();
        }
        else {
            System.out.println(currentSession.getCurrentUserInfo().getUsername());
        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        navHeaderUsername = (TextView) headerView.findViewById(R.id.navHeaderUsername);
        navHeaderEmail = (TextView) headerView.findViewById(R.id.navHeaderEmail);
        navHeaderImageView = (ImageView) headerView.findViewById(R.id.nav_header_profile_picture);





        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("userinfo").child(currentUid);
        mStorageRef = FirebaseStorage.getInstance().getReference();  // getting firebase storage reference





        userDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                    setUsernameAndEmail(userInfo.getEmail(), userInfo.getUsername());
                    currentSession.setCurrentUserInfo(userInfo);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }
        );
        navigationView.setNavigationItemSelectedListener(this);



        findDonorButton = (Button)findViewById(R.id.find_donor_button);
        bloodGroupSpinner = (Spinner) findViewById(R.id.map_page_blood_group_spinner);

        findDonorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAvailableDonorsActivity();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account_info) {
            launchAccountInfoActivity();
        }
        else if (id == R.id.nav_activity_log) {
            launchActivityLogActivity();
        }
        else if (id == R.id.nav_notifications) {
            launchNotificationsActivity();
        }
        else if (id == R.id.nav_settings) {
            launchSettingsActivity();
        }
        else if (id == R.id.nav_help) {
            launchHelpActivity();
        }
        else if (id == R.id.nav_about) {
            launchAboutActivity();
        }
        else if(id == R.id.nav_logout){
            currentSession.setCurrentUserInfo(null);
            currentSession.setCurrentUserProfilePicture(null);
            FirebaseAuth.getInstance().signOut();
            launchLoginActivity();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void launchLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void launchActivityLogActivity(){
        Intent intent = new Intent(this, ActivityLog.class);
        startActivity(intent);
    }

    private void launchNotificationsActivity(){
        Intent intent = new Intent(this, NotificationsActivity.class);
        startActivity(intent);
    }

    private void launchSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void launchHelpActivity(){
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    private void launchAboutActivity(){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void launchAccountInfoActivity(){
        Intent intent = new Intent(this, AccountInfoActivity.class);
        startActivity(intent);
    }

    private void launchAvailableDonorsActivity(){
        requiredBloodGroup = bloodGroupSpinner.getSelectedItem().toString().trim();
        Intent intent = new Intent(this, AvailableDonors.class);
        Bundle bundle = new Bundle();
        bundle.putString("blood", requiredBloodGroup);  // passing the selected blood group to the next activity
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void setUsernameAndEmail(String email, String username){
        navHeaderUsername.setText(username);
        navHeaderEmail.setText(email);
        try {

            if(currentSession.getCurrentUserInfo() == null) {
                downloadProfilePhoto();
                System.out.println("DOWNLOADING AGAIN...");

            }
            else {
                System.out.println("CURRENT USER " + currentSession.getCurrentUserInfo().getUsername());
                navHeaderImageView.setImageBitmap(BitmapFactory.
                        decodeFile(currentSession.getCurrentUserProfilePicture().getAbsolutePath()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void downloadProfilePhoto() throws Exception{

        StorageReference imageReference = mStorageRef.child("profile_photo_red_drop/"+  currentUid + ".jpg");
        final File localFile = File.createTempFile("images", "jpg");
        imageReference.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        currentSession.setCurrentUserProfilePicture(localFile);
                        navHeaderImageView.setImageBitmap(my_image);
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

}




