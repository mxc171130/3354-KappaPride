package com.example.kappapridesms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

public class MessageActivity extends AppCompatActivity
{
    public static final int PERM_REQUEST_CODE = 227;

    private static MessageViewAdapter s_messageViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);

        RecyclerView messageRecyclerView = (RecyclerView) findViewById(R.id.message_recycler);

        messageRecyclerView.setHasFixedSize(true);

        LinearLayoutManager myRecyclerLinearLayout = new LinearLayoutManager(this);
        messageRecyclerView.setLayoutManager(myRecyclerLinearLayout);


        s_messageViewAdapter = new MessageViewAdapter();
        s_messageViewAdapter.setHasStableIds(true);
        messageRecyclerView.setAdapter(s_messageViewAdapter);

        Toolbar mainTool = (Toolbar) findViewById(R.id.message_toolbar);
        setSupportActionBar(mainTool);


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
        testRepo.addConversation(new Conversation(16505551212L));
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.message_menu, menu);
        return true;
    }


    public void onSendClick(View view)
    {
        EditText messageText = (EditText) findViewById(R.id.message_text);
        Conversation targetConversation = ConversationRepository.getInstance().getTargetConversation();

        String messageContent = messageText.getText().toString();
        long timestamp = new Date().getTime();

        targetConversation.addMessage(new Message(timestamp, true, messageContent));
        FileSystem.getInstance().saveConversations(ConversationRepository.getInstance().getConversations());

        SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(SmsManager.getDefaultSmsSubscriptionId());
        smsManager.sendTextMessage("" + targetConversation.getRecipientPhone(), null, messageContent, null, null);

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
