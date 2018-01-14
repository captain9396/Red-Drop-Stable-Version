package org.reddrop.reddrop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SignUpActivity extends AppCompatActivity {


    private EditText editTextName;
    private AutoCompleteTextView editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;


    private Spinner spinnerGender;
    private Spinner spinnerBloodGroup;
    private Spinner spinnerMonth;
    private Spinner spinnerDay;
    private Spinner spinnerYear;
    private Spinner spinnerDivision;
    private Spinner spinnerCity;
    private Spinner spinnerLocation;


    private EditText editTextContactNumber;
    private EditText editTextEmail;

    private Button buttonRegister;



    private DatabaseReference databaseUserInfo;

    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthStateListener;

    private Set<String> existingUserNames;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // getting reference of table USER_INFO
        // FirebaseDatabase.getInstance().getRefernce()  this portion gets the reference of the
        // root node of the JSON tree of our firebase database
        databaseUserInfo = FirebaseDatabase.getInstance().getReference().child("userinfo");




        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextUsername = (AutoCompleteTextView)findViewById(R.id.editTextUsername);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        editTextPassword.setTypeface(Typeface.DEFAULT);
        editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
        editTextConfirmPassword = (EditText)findViewById(R.id.editTextConfirmPassword);
        editTextConfirmPassword.setTypeface(Typeface.DEFAULT);
        editTextConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());


        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        spinnerBloodGroup = (Spinner) findViewById(R.id.spinnerBloodGroup);
        spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);
        spinnerDay = (Spinner) findViewById(R.id.spinnerDay);
        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
        spinnerDivision = (Spinner) findViewById(R.id.spinnerDivision);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        spinnerLocation = (Spinner) findViewById(R.id.spinnerLocation);


        editTextContactNumber = (EditText) findViewById(R.id.editTextContactNumber);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Please wait...");

        existingUserNames = new HashSet<>();

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener =  new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };












//        databaseUserInfo.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                checkDatabaseForExistingUsernames(dataSnapshot);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });




        ArrayList<Integer> items = new ArrayList<>();
        ArrayList<Integer> years = new ArrayList<>();



        for(int i = 1; i <= 31; i++) items.add(i);
        for(int i = 1998; i >= 1960; i--) years.add(i);


        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        spinnerDay.setAdapter(adapter);

        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, years);
        spinnerYear.setAdapter(adapter2);






        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                progressDialog.show();
                addUser();
            }
        });


    }




//    private void checkDatabaseForExistingUsernames(DataSnapshot dataSnapshot){
//        for(DataSnapshot userDataSnapshot: dataSnapshot.getChildren()){
//
//            UserInfo userInfo = userDataSnapshot.getValue(UserInfo.class);
//            existingUserNames.add(userInfo.getUsername());
//        }
//
//        ArrayList<String> usernameList = new ArrayList<>();
//        usernameList.addAll(existingUserNames);
//
//
//        ArrayAdapter<String> usernamesArrayAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, usernameList);
//        editTextUsername.setThreshold(1);
//        editTextUsername.setAdapter(usernamesArrayAdapter);
//
//    }






    /**
     *
     * THIS CODE SNIPPET HIDES THE
     * KEYBOARD WHEN ANYWHERE BUT
     * THE EDITTEXT IS TAPPED
     *
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP ||
                ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText
                && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }








    private void addUser(){
        String name = editTextName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        String gender = spinnerGender.getSelectedItem().toString();
        String bloodGroup = spinnerBloodGroup.getSelectedItem().toString();

        String month = spinnerMonth.getSelectedItem().toString();
        String day = spinnerDay.getSelectedItem().toString();
        String year = spinnerYear.getSelectedItem().toString();

        String division = spinnerDivision.getSelectedItem().toString();
        String city = spinnerCity.getSelectedItem().toString();
        String location = spinnerLocation.getSelectedItem().toString();


        String contactNumber = editTextContactNumber.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        String getNotifications = "yes";
        String isLoggedIn = "yes";



        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(contactNumber)){
            Toast.makeText(this, "Mandatory fields cannot be left blank", Toast.LENGTH_LONG).show();
        }

        else if(!password.equals(confirmPassword)){
            Toast.makeText(this, "Please enter the password correctly", Toast.LENGTH_LONG).show();
        }

        else if(existingUserNames.contains(username)){
            Toast.makeText(this, "This username already exists. Please enter a different one.", Toast.LENGTH_LONG).show();
        }

        else{
            createUserWithEmailAndPassword(email, password);
        }
    }


    private void launchProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void launchDrawerActivity(){
        Intent intent = new Intent(this, Drawer.class);
        startActivity(intent);
    }

    private void createUserWithEmailAndPassword(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this,
                                    "registration unsuccessful please try again",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else{
                            Toast.makeText(SignUpActivity.this,
                                    "Registration Successful", Toast.LENGTH_SHORT).show();

                            addProfile();  // add an entry in the USER_INFO table
                            progressDialog.dismiss();
                            launchDrawerActivity();  // switch to profile activity

                        }
                    }
                });
    }




    // adds a row in the USER_INFO table in the database
    // when a user registers
    private void addProfile(){
        String name = editTextName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        String gender = spinnerGender.getSelectedItem().toString();
        String bloodGroup = spinnerBloodGroup.getSelectedItem().toString();

        String month = spinnerMonth.getSelectedItem().toString();
        String day = spinnerDay.getSelectedItem().toString();
        String year = spinnerYear.getSelectedItem().toString();

        String division = spinnerDivision.getSelectedItem().toString();
        String city = spinnerCity.getSelectedItem().toString();
        String location = spinnerLocation.getSelectedItem().toString();


        String contactNumber = editTextContactNumber.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        String getNotifications = "yes";
        String isLoggedIn = "yes";


        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        UserInfo userInfo = new UserInfo(id, name, username,
                                        password, gender, bloodGroup,
                                        month, year, day,
                                        division, city, location,
                                        contactNumber, email, getNotifications, isLoggedIn
                                        );

        databaseUserInfo.child(id).setValue(userInfo);
    }

}
