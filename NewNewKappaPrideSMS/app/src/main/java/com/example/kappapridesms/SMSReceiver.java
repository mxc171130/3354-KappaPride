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

    /**
     *
     * @param context
     * @param intent the intent is the SMS message that will be sent
     *This program runs in the background and whenever a message is sent to the phone it gets called.
     *The program then checks to see if the number is black listed,if not then the phone will get the message
     * and the phone will send the user a notification
     */

    @Override
    public void onReceive(Context context, Intent intent)
    {   String userNumber="";
        //Checks to see if the intent being sent is a SMS message
        if(intent.getAction().equals(SMS_RECEIVED_ACTION))
        {   //The message is then placed in an array of SMSmessage,as well as the blacklist numbers
            SmsMessage[] messages = getMessagesFromIntent(intent);
            ConversationRepository instance = ConversationRepository.getInstance();
            Blacklist blacklist = instance.getBlacklist();
            //for each message the phone will check the date,if the message is self sent
            //and the actual message itself
            nextMessage:
            for(SmsMessage message : messages)
            {
                Message receivedMessage = new Message(new Date().getTime(), false, message.getMessageBody());

                String senderPhoneNumber = message.getOriginatingAddress();
                userNumber=senderPhoneNumber;
                //Checks if the phonenumber is of valid length
                if(senderPhoneNumber.length() == 10)
                {
                    senderPhoneNumber = "1" + senderPhoneNumber;
                }
                //converts number into Long
                long senderPhoneNumberLong = Long.parseLong(senderPhoneNumber);
                //Checks to see if the phone number is blacklisted
                for(int i = 0; i < blacklist.size(); i++)
                {
                    if(senderPhoneNumberLong == blacklist.getBlacklistedContact(i).getPhoneNumber())
                    {
                        continue nextMessage;
                    }
                }
                //Places the message in the conversation
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
            //Saves Conversation in phones memory
            FileSystem.getInstance().saveConversations(instance.getConversations());
            //Creating Notification
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {    //First Creating Notifcation Channel
                NotificationManager m_manger = context.getSystemService(NotificationManager.class);

                NotificationChannel popUp = new NotificationChannel(NOTIFICATION, "popUp", NotificationManager.IMPORTANCE_DEFAULT);
                popUp.setDescription("SMS notification");
                m_manger.createNotificationChannel(popUp);
            }
            //Places the settings for the notification
            Notification m_notification= new NotificationCompat.Builder(context, NOTIFICATION)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(userNumber)
                    .setContentText("Has sent you a message")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setChannelId(NOTIFICATION)
                    .setAutoCancel(true)
                    .build();
            //Displays Notification
            NotificationManagerCompat displayManager = NotificationManagerCompat.from(context);
            displayManager.notify(15234,m_notification);
        }

        MessageFragment.getMessageViewAdapter().notifyDataSetChanged();
    }
}
