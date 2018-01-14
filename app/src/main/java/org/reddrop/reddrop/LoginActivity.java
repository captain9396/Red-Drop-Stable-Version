package org.reddrop.reddrop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private EditText usernameLogin;
    private EditText passwordLogin;
    private Button buttonLogin;
    private TextView signUpTextView;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private CurrentSessionData currentSession;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameLogin = (EditText) findViewById(R.id.usernameLogin);
        passwordLogin = (EditText) findViewById(R.id.passwordLogin);


        // this two lines are added to avoid consolas font in the password edittext
        passwordLogin.setTypeface(Typeface.DEFAULT);
        passwordLogin.setTransformationMethod(new PasswordTransformationMethod());


        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);

        currentSession = CurrentSessionData.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");


        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    launchDrawerActivity();
                } else {

                    // User is signed out
                    //Log.d("onAuthStateChanged:signed_out");
                }
            }
        };









        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usernameLogin.getText().toString().trim();
                String password = passwordLogin.getText().toString().trim();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Please enter email and password",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.show();
                    loginToUserAccount(email, password);
                }
            }
        });


        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignUpActivity();
            }
        });


    }


    /**
     * this code snippet is returning app to Android Home Screen
     * when back button is pressed when user is a this activity
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);


    }

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










    @Override
    protected void onStart() {
       super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void launchProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


    private void launchSignUpActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


    private void launchDrawerActivity(){
        Intent intent = new Intent(this, Drawer.class);
        startActivity(intent);
    }



    private void loginToUserAccount(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();

                        if (!task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this, "wrong email or password",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Welcome",
                                    Toast.LENGTH_SHORT).show();

                            launchDrawerActivity();
                        }
                    }
                });
    }



}
