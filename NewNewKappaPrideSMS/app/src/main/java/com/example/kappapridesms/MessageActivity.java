package com.example.kappapridesms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;


import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity implements SentReceiver.OnFailedSendListener
{
    public static final int PERM_REQUEST_CODE = 227;

    private MessageFragment m_messageFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        m_messageFragment = new MessageFragment();

        fragmentTransaction.replace(R.id.fragment_container, m_messageFragment);
        fragmentTransaction.commit();

        Toolbar mainTool = initializeToolBar();
        getWindow().setStatusBarColor(getColor(R.color.colorPrimaryDark));

        SentReceiver contextRegisteredSentReceiver = new SentReceiver(this);
        IntentFilter sentReceiverFilter = new IntentFilter();
        sentReceiverFilter.addAction("SMS_SENT");

        registerReceiver(contextRegisteredSentReceiver, sentReceiverFilter);

        setUpPermissions();

        ConversationRepository instance = ConversationRepository.getInstance();
        Blacklist blacklist = instance.getBlacklist();

        FileSystem fileSystem = FileSystem.getInstance();

        ArrayList<Long> blacklistedNumbers = fileSystem.loadBlacklistNumbers();

        if(blacklistedNumbers != null)
        {
            for (Long blacklistedNumber : blacklistedNumbers)
            {
                blacklist.addBlacklistedNumber(blacklistedNumber);
            }
        }
    }

    public MessageFragment getMessageFragment()
    {
        return m_messageFragment;
    }

    private void setUpPermissions() {
        boolean[] perms = new boolean[6];
        perms[0] = checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
        perms[1] = checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        perms[2] = checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
        perms[3] = checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
        perms[4] = checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        perms[5] = checkSelfPermission(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED;

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

        if(!perms[4])
        {
            requestPermissions[requestPointer] = Manifest.permission.READ_CONTACTS;
            requestPointer++;
        }

        if(!perms[5])
        {
            requestPermissions[requestPointer] = Manifest.permission.WRITE_CONTACTS;
            requestPointer++;
        }

        if(numbPerms != 0)
        {
            requestPermissions(requestPermissions, PERM_REQUEST_CODE);
        }
    }

    private Toolbar initializeToolBar()
    {
        Toolbar mainTool = findViewById(R.id.message_toolbar);
        setSupportActionBar(mainTool);
        return mainTool;
    }


    @Override
    protected void onNewIntent(Intent receivedIntent)
    {
        setIntent(receivedIntent);

        if(receivedIntent.getAction().equals(Intent.ACTION_SEARCH))
        {
            searchMessages(receivedIntent.getStringExtra(SearchManager.QUERY));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.message_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnCloseListener(m_messageFragment);

        return true;
    }


    @Override
    public void onFailedSend()
    {
        m_messageFragment.onFailedSend();
    }


    public void searchMessages(String query)
    {
        ArrayList<View> allViewsWithQuery = new ArrayList<>();

        findViewById(R.id.message_recycler).findViewsWithText(allViewsWithQuery, query, View.FIND_VIEWS_WITH_TEXT);

        for(View highlightView : allViewsWithQuery)
        {
            highlightView.setBackgroundColor(Color.YELLOW);
        }
    }

    public void onSendClick(View view)
    {
        EditText messageText = (EditText) findViewById(R.id.message_text);
        Conversation targetConversation = ConversationRepository.getInstance().getTargetConversation();
        String messageContent = messageText.getText().toString();

        m_messageFragment.sendMessage(targetConversation, messageContent);

        messageText.setText("");
    }
}
