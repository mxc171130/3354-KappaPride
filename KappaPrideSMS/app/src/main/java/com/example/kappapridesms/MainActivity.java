package com.example.kappapridesms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);

        RecyclerView myRecyclerView = (RecyclerView) findViewById(R.id.message_recycler);

        myRecyclerView.setHasFixedSize(true);

        LinearLayoutManager myRecyclerLinearLayout = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myRecyclerLinearLayout);

        MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter();
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
    }
}
