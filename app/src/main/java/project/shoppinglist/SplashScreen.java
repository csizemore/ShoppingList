package project.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Crystal on 11/13/2016.
 */
public class SplashScreen extends FragmentActivity {

    Thread thread = new Thread() {
        public void run() {
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(SplashScreen.this,MainActivity.class));
            finish();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        thread.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
