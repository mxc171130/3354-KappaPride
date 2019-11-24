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

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append("Inbox Count: ");
        builder.append(num_imboxes);
        builder.append("\n");

        builder.append("Status: ");
        builder.append(status);
        builder.append("\n");

        return builder.toString();
    }
}
