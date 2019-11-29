package com.example.kappapridesms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import java.util.Date;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;
import static android.provider.Telephony.Sms.Intents.getMessagesFromIntent;

public class SMSReceiver extends BroadcastReceiver
{
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
        }

        MessageActivity.getMessageViewAdapter().notifyDataSetChanged();
    }
}
