package com.example.kappapridesms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

public class MessageActivity extends AppCompatActivity
{
    class SendThread implements Runnable
    {
        private long m_receiverPhone;
        private String m_messageContent;

        public void run()
        {
            RESTInterface.sendMessage(m_messageContent, m_receiverPhone);
        }

        private SendThread(long receiverPhone, String messageContent)
        {
            m_receiverPhone = receiverPhone;
            m_messageContent = messageContent;
        }
    }

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

        Intent serviceIntent = new Intent(this, ReceiverService.class);
        startService(serviceIntent);

        // TEST CODE DOWN BELOW
        ConversationRepository testRepo = ConversationRepository.getInstance();
        testRepo.addConversation(new Conversation(19183521183L, 19183521183L));
        testRepo.setTargetConversation(0);
    }


    public void onSendClick(View view)
    {
        EditText messageText = (EditText) findViewById(R.id.message_text);
        Conversation targetConversation = ConversationRepository.getInstance().getTargetConversation();

        String messageContent = messageText.getText().toString();
        long timestamp = new Date().getTime();

        targetConversation.addMessage(new Message(timestamp, messageContent));

        Thread sendThread = new Thread(new SendThread(targetConversation.getReceiverPhone(), messageContent));
        sendThread.start();
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
