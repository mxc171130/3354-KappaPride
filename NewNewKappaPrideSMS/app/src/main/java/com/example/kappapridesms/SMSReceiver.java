package com.example.kappapridesms;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;
import static android.provider.Telephony.Sms.Intents.getMessagesFromIntent;

public class SMSReceiver extends BroadcastReceiver
{
    public static final String NOTIFICATION = "kappa_notification";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent.getAction().equals(SMS_RECEIVED_ACTION))
        {
            SmsMessage[] messages = getMessagesFromIntent(intent);
            ConversationRepository instance = ConversationRepository.getInstance();
            Blacklist blacklist = instance.getBlacklist();

            nextMessage:
            for(SmsMessage message : messages)
            {
                Message receivedMessage = new Message(new Date().getTime(), false, message.getMessageBody());

                String senderPhoneNumber = message.getOriginatingAddress();

                if(senderPhoneNumber.length() == 10)
                {
                    senderPhoneNumber = "1" + senderPhoneNumber;
                }

                long senderPhoneNumberLong = Long.parseLong(senderPhoneNumber);

                for(int i = 0; i < blacklist.size(); i++)
                {
                    if(senderPhoneNumberLong == blacklist.getBlacklistedContact(i).getPhoneNumber())
                    {
                        continue nextMessage;
                    }
                }

                for(Conversation insertConversation : instance.getConversations())
                {
                    if(insertConversation.getRecipientPhone() == senderPhoneNumberLong)
                    {
                        insertConversation.addMessage(receivedMessage);
                        continue nextMessage;
                    }
                }

                // No conversation detected, create a new one and add the message to it
                Conversation newConversation = new Conversation(senderPhoneNumberLong);
                instance.addConversation(newConversation);
                newConversation.addMessage(receivedMessage);
            }

            FileSystem.getInstance().saveConversations(instance.getConversations());

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                NotificationManager m_manger = context.getSystemService(NotificationManager.class);

                NotificationChannel popUp = new NotificationChannel(NOTIFICATION, "popUp", NotificationManager.IMPORTANCE_DEFAULT);
                popUp.setDescription("SMS notification");
                m_manger.createNotificationChannel(popUp);
            }

            Notification m_notification= new NotificationCompat.Builder(context, NOTIFICATION)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Message")
                    .setContentText("Has sent you a message")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setChannelId(NOTIFICATION)
                    .setAutoCancel(true)
                    .build();

            NotificationManagerCompat displayManager = NotificationManagerCompat.from(context);
            displayManager.notify(15234,m_notification);
        }

        if(MessageFragment.getMessageViewAdapter() != null)
        {
            MessageFragment.getMessageViewAdapter().notifyDataSetChanged();
        }

        ConversationActivity.getConversationViewAdapter().notifyDataSetChanged();
    }
}
