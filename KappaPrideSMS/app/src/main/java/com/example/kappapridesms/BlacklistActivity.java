package com.example.kappapridesms;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import androidx.appcompat.widget.Toolbar;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BlacklistActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blacklist_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.blacklist_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.blacklist_menu, menu);*/
        getMenuInflater().inflate(R.menu.blacklist_menu, menu);
        return true;
    }
}
