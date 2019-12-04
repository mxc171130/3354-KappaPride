package com.example.kappapridesms;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;

public class ConversationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ConversationDialog.ConversationDialogListener
{
    private static ConversationViewAdapter s_conversationViewAdapter;
    public static final int PERM_REQUEST_CODE = 227;

    private ConversationDialog m_conversationDialog;

    //TODO
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_activity);

        m_conversationDialog = new ConversationDialog();

        RecyclerView conversationRecyclerView = initializeRecyclerView();
        initializeLayoutManager(conversationRecyclerView);
        initializeViewAdapter(conversationRecyclerView);

        Toolbar mainTool = initializeToolBar();
        getWindow().setStatusBarColor(getColor(R.color.colorPrimaryDark));

        setUpPermissions();
    }


    private Toolbar initializeToolBar()
    {
        Toolbar mainTool = findViewById(R.id.conversation_toolbar);
        setSupportActionBar(mainTool);
        return mainTool;
    }


    private void setUpPermissions()
    {
        boolean[] perms = new boolean[4];
        perms[0] = checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
        perms[1] = checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        perms[2] = checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
        perms[3] = checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;

        int numbPerms = 0;

        for(boolean perm : perms)
        {
            if(!perm)
            {
                numbPerms++;
            }
        }

        String[] requestPermissions = new String[numbPerms];
        int requestPointer = 0;

        if(!perms[0])
        {
            requestPermissions[requestPointer] = Manifest.permission.RECEIVE_SMS;
            requestPointer++;
        }

        if(!perms[1])
        {
            requestPermissions[requestPointer] = Manifest.permission.SEND_SMS;
            requestPointer++;
        }

        if(!perms[2])
        {
            requestPermissions[requestPointer] = Manifest.permission.READ_PHONE_STATE;
            requestPointer++;
        }

        if(!perms[3])
        {
            requestPermissions[requestPointer] = Manifest.permission.READ_SMS;
            requestPointer++;
        }

        if(numbPerms != 0)
        {
            requestPermissions(requestPermissions, PERM_REQUEST_CODE);
        }
    }


    private RecyclerView initializeRecyclerView()
    {
        RecyclerView conversationRecyclerView = (RecyclerView) findViewById(R.id.conversation_recycler);
        conversationRecyclerView.setNestedScrollingEnabled(true);
        conversationRecyclerView.setHasFixedSize(true);
        return conversationRecyclerView;
    }

    private void initializeLayoutManager(RecyclerView conversationRecyclerView)
    {
        LinearLayoutManager myRecyclerLinearLayout = new LinearLayoutManager(this);
        conversationRecyclerView.setLayoutManager(myRecyclerLinearLayout);
    }


    private void initializeViewAdapter(RecyclerView conversationRecyclerView)
    {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.conversation_menu, menu);

        return true;
    }

    //TODO
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.conversation_add)
        {
            m_conversationDialog.show(getSupportFragmentManager(), "conversation_dialog");
        }
        else
        {
            super.onOptionsItemSelected(item);
        }

        return true;
    }


    @Override
    public void onConversationPositiveClick()
    {
        TextView view = (TextView) m_conversationDialog.getConversationContent().findViewById(R.id.conversation_field);
        Long phoneNumber = Long.parseLong("1" + view.getText().toString());

        ConversationRepository instance = ConversationRepository.getInstance();
        ArrayList<Conversation> conversations = instance.getConversations();

        for(int i = 0; i < conversations.size(); i++)
        {
            Conversation queryConversation = conversations.get(i);

            if(phoneNumber == queryConversation.getRecipientPhone())
            {
                instance.setTargetConversation(i);

                Intent startMessageActivityIntent = new Intent(this, MessageActivity.class);
                startMessageActivityIntent.setAction("switch");
                startActivity(startMessageActivityIntent);
                return;
            }
        }

        Conversation newConversation = new Conversation(phoneNumber);
        instance.addConversation(newConversation);
        instance.setTargetConversation(conversations.size() - 1);
        FileSystem.getInstance().saveConversations(conversations);

        Intent startMessageActvityIntent = new Intent(this, MessageActivity.class);
        startMessageActvityIntent.setAction("switch");
        startActivity(startMessageActvityIntent);
    }

    @Override
    public void onConversationNegativeClick()
    {

    }

    public static ConversationViewAdapter getConversationViewAdapter()
    {
        return s_conversationViewAdapter;
    }
}