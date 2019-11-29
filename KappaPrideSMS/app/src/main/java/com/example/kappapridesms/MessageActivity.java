package com.example.kappapridesms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class MessageActivity extends AppCompatActivity implements ForwardDialog.ForwardDialogListener, View.OnTouchListener, SearchView.OnCloseListener, SentReceiver.OnFailedSendListener, WarningDialog.WarningDialogListener
{
    public static final int PERM_REQUEST_CODE = 227;

    private static MessageViewAdapter s_messageViewAdapter;

    private boolean m_deleteActive;
    private View m_deleteView;
    private WarningDialog m_warningDialog;

    private boolean m_forwardActive;
    private String m_forwardContent;
    private ForwardDialog m_forwardDialog;

    private ErrorDialog m_errorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);

        RecyclerView messageRecyclerView = (RecyclerView) findViewById(R.id.message_recycler);
        messageRecyclerView.setOnTouchListener(this);
        messageRecyclerView.setNestedScrollingEnabled(false);
        messageRecyclerView.setHasFixedSize(true);

        LinearLayoutManager myRecyclerLinearLayout = new LinearLayoutManager(this);
        messageRecyclerView.setLayoutManager(myRecyclerLinearLayout);

        m_forwardDialog = new ForwardDialog();
        m_errorDialog = new ErrorDialog();
        m_warningDialog = new WarningDialog();

        s_messageViewAdapter = new MessageViewAdapter();
        s_messageViewAdapter.setHasStableIds(true);
        messageRecyclerView.setAdapter(s_messageViewAdapter);

        Toolbar mainTool = (Toolbar) findViewById(R.id.message_toolbar);
        setSupportActionBar(mainTool);

        SentReceiver contextRegisteredSentReceiver = new SentReceiver(this);
        IntentFilter sentReceiverFilter = new IntentFilter();
        sentReceiverFilter.addAction("SMS_SENT");

        registerReceiver(contextRegisteredSentReceiver, sentReceiverFilter);

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

        // TEST CODE DOWN BELOW
        ConversationRepository testRepo = ConversationRepository.getInstance();
        testRepo.addConversation(new Conversation(19183521183L));
        testRepo.setTargetConversation(0);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        ConversationRepository.getInstance().loadConversations();
        s_messageViewAdapter.notifyDataSetChanged();
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
    public boolean onClose()
    {
        ViewGroup recyclerView = (ViewGroup) findViewById(R.id.message_recycler);
        ViewGroup recyclerElement;

        for(int i = 0; i < recyclerView.getChildCount(); i++)
        {
            recyclerElement = (ViewGroup) recyclerView.getChildAt(i);

            for(int j = 0; j < recyclerElement.getChildCount(); j++)
            {
                recyclerElement.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
            }
        }

        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.message_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnCloseListener(this);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch(item.getItemId())
        {
            case R.id.message_delete:
                m_deleteActive = true;
                return true;
            case R.id.message_forward:
                m_forwardActive = true;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onFailedSend()
    {
        m_errorDialog.show(getSupportFragmentManager(), "error_dialog");
    }


    @Override
    public void onForwardPositiveClickListener()
    {
        TextView messageText = (TextView) m_forwardDialog.getForwardContent().findViewById(R.id.forward_phone);
        String stringNumber = "1" + messageText.getText();
        long phoneNumber = Long.parseLong(stringNumber);

        ConversationRepository instance = ConversationRepository.getInstance();

        for(Conversation testConversation : instance.getConversations())
        {
            if(testConversation.getRecipientPhone() == phoneNumber)
            {
                sendMessage(testConversation, m_forwardContent);
                return;
            }
        }

        Conversation newConversation = new Conversation(phoneNumber);
        instance.addConversation(newConversation);
        sendMessage(newConversation, m_forwardContent);
    }


    @Override
    public void onForwardNegativeClickListener()
    {

    }

    @Override
    public void onWarningPositiveClick()
    {
        LinearLayout messageLayout = (LinearLayout) m_deleteView;
        TextView messageView = (TextView) messageLayout.getChildAt(1);
        String messageContent = (String) messageView.getText();

        ConversationRepository instance = ConversationRepository.getInstance();
        Conversation deleteFromConversation = instance.getTargetConversation();

        for(int i = 0; i < deleteFromConversation.size(); i++)
        {
            Message testMessage = deleteFromConversation.getMessage(i);

            if(testMessage.getContent().equals(messageContent))
            {
                deleteFromConversation.deleteMessage(testMessage);
                FileSystem.getInstance().saveConversations(instance.getConversations());
                s_messageViewAdapter.notifyDataSetChanged();

                break;
            }
        }
    }

    @Override
    public void onWarningNegativeClick()
    {

    }

    @Override
    public boolean onTouch(View v, MotionEvent ev)
    {
        if(m_deleteActive)
        {
            if(v instanceof RecyclerView)
            {
                RecyclerView recyclerView = (RecyclerView) v;
                m_deleteView = recyclerView.findChildViewUnder(ev.getX(), ev.getY());

                m_warningDialog.show(getSupportFragmentManager(), "warning_dialog");
                m_deleteActive = false;
            }
        }
        else if(m_forwardActive)
        {
            if(v instanceof RecyclerView)
            {
                RecyclerView recyclerView = (RecyclerView) v;
                View childView = recyclerView.findChildViewUnder(ev.getX(), ev.getY());

                LinearLayout messageLayout = (LinearLayout) childView;
                TextView messageView = (TextView) messageLayout.getChildAt(1);
                m_forwardContent = (String) messageView.getText();

                m_forwardDialog.show(getSupportFragmentManager(), "forward_dialog");

                m_forwardActive = false;
            }
        }

        return true;
    }


    public void onSendClick(View view)
    {
        EditText messageText = (EditText) findViewById(R.id.message_text);
        Conversation targetConversation = ConversationRepository.getInstance().getTargetConversation();
        String messageContent = messageText.getText().toString();

        sendMessage(targetConversation, messageContent);
    }


    public void searchMessages(String query)
    {
        ArrayList<View> allViewsWithQuery = new ArrayList<>();

        findViewById(R.id.message_recycler).findViewsWithText(allViewsWithQuery, query, View.FIND_VIEWS_WITH_TEXT);

        for(View highlightView : allViewsWithQuery)
        {
            highlightView.setBackgroundColor(Color.YELLOW);
        }

        /*
        ConversationRepository instance = ConversationRepository.getInstance();
        Conversation targetConversation = instance.getTargetConversation();

        for(int i = 0; i < targetConversation.size(); i++)
        {
            if(targetConversation.getMessage(i).getContent().contains(query))
            {
                // Query matches this message, update its view holder

                getCurrentFocus().findViewById(R.id.message_recycler).find
            }
        }

        */
    }


    public void sendMessage(Conversation targetConversation, String messageContent)
    {
        long timestamp = new Date().getTime();

        targetConversation.addMessage(new Message(timestamp, true, messageContent));
        FileSystem.getInstance().saveConversations(ConversationRepository.getInstance().getConversations());

        PendingIntent sentIntent = PendingIntent.getBroadcast(this,0, new Intent("SMS_SENT"),0);


        SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(SmsManager.getDefaultSmsSubscriptionId());
        smsManager.sendTextMessage("" + targetConversation.getRecipientPhone(), null, messageContent, sentIntent, null);

        s_messageViewAdapter.notifyDataSetChanged();
    }


    public static MessageViewAdapter getMessageViewAdapter()
    {
        return s_messageViewAdapter;
    }
}

/*
package com.example.dialog;

        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

public class MessageActivity extends AppCompatActivity {
    private Button alertBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onButtonClickListener();

    }
    public void onButtonClickListener()
    {   final ErrorDialog errorDialog=new ErrorDialog();
        alertBtn=findViewById(R.id.alertBtn);
        alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder warning= new AlertDialog.Builder(MessageActivity.this);
                warning.setMessage(errorDialog.getContent())
                        .setCancelable(false)
                        .setPositiveButton("Close app", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                AlertDialog alertDialog=warning.create();
                alertDialog.show();

            }
        });
    }

}*/
