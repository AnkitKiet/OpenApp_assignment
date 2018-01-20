package edu.openapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ankit on 19/01/18.
 */

public class BaseActivity extends AppCompatActivity {

    public static Fragment currentFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public Intent openActivity(Context first, Class<? extends AppCompatActivity> second) {

        Intent intent = new Intent(first, second);
        return intent;
    }

}
