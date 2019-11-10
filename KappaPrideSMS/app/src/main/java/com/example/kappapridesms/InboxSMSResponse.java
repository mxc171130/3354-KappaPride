package com.example.kappapridesms;

public class InboxSMSResponse
{
    int num_imboxes;
    Inbox[] inboxes;
    String status;

    class Inbox
    {
        String id;
        long number;
        String keyword;
        int num_messages;
        int num_contacts;
        int newMsgs;
    }
}
