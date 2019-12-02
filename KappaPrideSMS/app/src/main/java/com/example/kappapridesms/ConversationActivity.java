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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Iterator;

public class ConversationActivity extends AppCompatActivity implements View.OnTouchListener, NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout m_drawer;

    private static ConversationViewAdapter s_conversationViewAdapter;

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

        initializeNavigationView();

        initializeViewAdapter(conversationRecyclerView);


    }

    private RecyclerView initializeRecyclerView()
    {
        RecyclerView conversationRecyclerView = (RecyclerView) findViewById(R.id.conversation_recycler);
        conversationRecyclerView.setOnTouchListener(this);
        conversationRecyclerView.setNestedScrollingEnabled(true);
        conversationRecyclerView.setHasFixedSize(true);
        return conversationRecyclerView;
    }

    private void initializeLayoutManager(RecyclerView conversationRecyclerView)
    {
        LinearLayoutManager myRecyclerLinearLayout = new LinearLayoutManager(this);
        conversationRecyclerView.setLayoutManager(myRecyclerLinearLayout);
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

    private void initializeNavigationView()
    {
        m_drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initializeViewAdapter(RecyclerView conversationRecyclerView) {
        s_conversationViewAdapter = new ConversationViewAdapter();
        s_conversationViewAdapter.setHasStableIds(true);
        conversationRecyclerView.setAdapter(s_conversationViewAdapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        ConversationRepository.getInstance().loadConversations();
        s_conversationViewAdapter.notifyDataSetChanged();
    }

    //TODO
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

    //TODO
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        return false;
    }


    public static ConversationViewAdapter getConversationViewAdapter()
    {
        return s_conversationViewAdapter;
    }




}