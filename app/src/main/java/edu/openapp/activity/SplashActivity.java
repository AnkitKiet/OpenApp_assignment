package edu.openapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.openapp.R;

import static java.lang.Thread.sleep;

public class SplashActivity extends BaseActivity {

    @Bind(R.id.imageView)
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Glide.with(SplashActivity.this).load(R.drawable.aadhar).asBitmap().into(img);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    startActivity(new Intent(SplashActivity.this,DashboardActivity.class));
                }
            }
        });
        thread.start();


    }
}
