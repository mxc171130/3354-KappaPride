package com.example.kappapridesms;

public class SendSMSResponse
{
    int balance;
    long batch_id;
    int cost;
    int num_messages;
    SendMessage message;
    SendMessages[] messages;
    String status;

    class SendMessage
    {
        int num_parts;
        String sender;
        String content;
    }

    class SendMessages
    {
        String id;
        long recipient;
    }
}
