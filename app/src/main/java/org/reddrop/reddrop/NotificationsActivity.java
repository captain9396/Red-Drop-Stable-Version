package org.reddrop.reddrop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class NotificationsActivity extends AppCompatActivity {

    private ImageView backButtonAccountInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        backButtonAccountInfo = (ImageView)findViewById(R.id.backButtonActivityLog);

        backButtonAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDrawerActivity();
            }
        });
    }

    private void launchDrawerActivity(){
        Intent intent = new Intent(this, Drawer.class);
        startActivity(intent);
    }

}
