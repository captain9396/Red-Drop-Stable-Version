package org.reddrop.reddrop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {


    private Button logoutButton;
    private TextView profileEmail;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logoutButton = (Button)findViewById(R.id.logoutButton);
        profileEmail = (TextView)findViewById(R.id.currentEmail);

        profileEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        logoutButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    launchLoginActivity();
                }
            }
        );

    }

    private void launchLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }



}
