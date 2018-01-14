package org.reddrop.reddrop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NoConnectionActivity extends AppCompatActivity {


    private Button refreshButton;
    private TextView noConnectionTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);

        refreshButton = (Button)findViewById(R.id.refreshButton);
        noConnectionTextView = (TextView)findViewById(R.id.noConnectionTextView);



        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSplashScreenActivity();
            }
        });

    }

    private void launchSplashScreenActivity(){
        Intent intent = new Intent(this, Splashscreen.class);
        startActivity(intent);
    }
}
