package com.example.kappapridesms;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

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

            nextMessage:
            for(SmsMessage message : messages)
            {
                Message receivedMessage = new Message(message.getTimestampMillis(), message.getMessageBody());
                TelephonyManager phoneNumberManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                String receiverPhoneNumber = null;

                if(context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                {
                    receiverPhoneNumber = phoneNumberManager.getLine1Number();
                    receiverPhoneNumber = receiverPhoneNumber.replaceAll("\\+", "");
                }

                String senderPhoneNumber = message.getOriginatingAddress();

                ConversationRepository instance = ConversationRepository.getInstance();

                if(senderPhoneNumber.length() == 10)
                {
                    senderPhoneNumber = "1" + senderPhoneNumber;
                }

                long receiverPhoneNumberLong = Long.parseLong(receiverPhoneNumber);
                long senderPhoneNumberLong = Long.parseLong(senderPhoneNumber);

                for(Conversation insertConversation : instance.getConversations())
                {
                    if(insertConversation.getAuthorPhone() == senderPhoneNumberLong && insertConversation.getReceiverPhone() == receiverPhoneNumberLong)
                    {
                        insertConversation.addMessage(receivedMessage);
                        continue nextMessage;
                    }
                }

                // No conversation detected, create a new one and add the message to it
                Conversation newConversation = new Conversation(senderPhoneNumberLong, receiverPhoneNumberLong);
                instance.addConversation(newConversation);
                newConversation.addMessage(receivedMessage);
            }
        }
    }
}
