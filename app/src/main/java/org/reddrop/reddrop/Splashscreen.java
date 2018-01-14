package org.reddrop.reddrop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

public class Splashscreen extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splashscreen);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                boolean isConnected = isNetworkAvailable();
                if(isConnected == true) {
                    Intent mainIntent = new Intent(Splashscreen.this, LoginActivity.class);
                    Splashscreen.this.startActivity(mainIntent);
                    Splashscreen.this.finish();
                }else{
                    launchNoConnectionActivity();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


    boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void launchNoConnectionActivity() {
        Intent intent = new Intent(this, NoConnectionActivity.class);
        startActivity(intent);
    }
}