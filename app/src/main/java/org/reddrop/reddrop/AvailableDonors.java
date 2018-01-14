package org.reddrop.reddrop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

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
import java.util.ArrayList;

public class AvailableDonors extends AppCompatActivity {

    private ImageView backButtonActivityLog;

    private DatabaseReference users;
    private ArrayList<UserInfo> availableDonors;
    private ListView donorList;
    private ProgressDialog progressDialog;



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_donors);

        Bundle bundle = getIntent().getExtras();
        final String requiredBloodGroup = bundle.getString("blood");




        backButtonActivityLog = (ImageView)findViewById(R.id.backButtonActivityLog);
        donorList = (ListView)findViewById(R.id.donor_list);
        users = FirebaseDatabase.getInstance().getReference().child("userinfo");
        availableDonors = new ArrayList<>();
        final String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        progressDialog = new ProgressDialog(AvailableDonors.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();



        backButtonActivityLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDrawerActivity();
            }
        });


        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    UserInfo userInfo = snapshot.getValue(UserInfo.class);
                    if(!userInfo.getId().equals(currentUid) && userInfo.getBloodgroup().equals(requiredBloodGroup)) {
                        availableDonors.add(userInfo);

                    }
                }

                updateListView();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }


    private void addToAvailableDonors(UserInfo userInfo){
        UserInfo availableDonor = new UserInfo(userInfo.getId(), userInfo.getName(), userInfo.getUsername(),
                userInfo.getPassword(), userInfo.getGender(), userInfo.getBloodgroup(),
                userInfo.getMonth(), userInfo.getYear(), userInfo.getDay(),
                userInfo.getDivision(), userInfo.getCity(), userInfo.getLocation(),
                userInfo.getContact(), userInfo.getEmail(), userInfo.getGetNotifications(),
                userInfo.getIsLoggedIn());


        availableDonors.add(availableDonor);
    }

    private void launchDrawerActivity(){
        Intent intent = new Intent(this, Drawer.class);
        startActivity(intent);
    }


    private void updateListView(){
        ListAdapter myListAdapter = new CustomAdapterAvailableDonors(this, availableDonors);
        donorList.setAdapter(myListAdapter);
        donorList.setClickable(true);
        donorList.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Ads food = (Ads)(parent.getItemAtPosition(position));
//                        currentSingletonAd.setAds( food );
//                        launchShowAd();
                        //Toast.makeText(getApplicationContext() , food.getLocation(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }



}
