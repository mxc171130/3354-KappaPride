package com.example.kappapridesms;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Iterator;

public class ConversationActivity extends AppCompatActivity implements View.OnTouchListener
{
    //TODO
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_activity);

        RecyclerView conversationRecyclerView = initializeRecyclerView();
        initializeLayoutManager(conversationRecyclerView);


        // Need to TEST
        // switching between the two activities
        configureMessageActivity();


    }

    // code that will take us to the Message activity
    private void configureMessageActivity()
    {
        TextView conversationTxt = (TextView) findViewById(R.id.conv_bubble_preview);
        conversationTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConversationActivity.this, MessageActivity.class));
            }
        });

    }

    private void initializeLayoutManager(RecyclerView conversationRecyclerView) {
        LinearLayoutManager myRecyclerLinearLayout = new LinearLayoutManager(this);
        conversationRecyclerView.setLayoutManager(myRecyclerLinearLayout);
    }

    private RecyclerView initializeRecyclerView() {
        RecyclerView conversationRecyclerView = (RecyclerView) findViewById(R.id.conversation_recycler);
        conversationRecyclerView.setOnTouchListener(this);
        conversationRecyclerView.setNestedScrollingEnabled(true);
        conversationRecyclerView.setHasFixedSize(true);
        return conversationRecyclerView;
    }


    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}