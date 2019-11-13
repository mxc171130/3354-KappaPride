package com.example.kappapridesms;

import java.util.*;

public class ConversationRepository
{
    private static final ConversationRepository instance = new ConversationRepository();

    private ArrayList<Conversation> conversations;
    private ContactManager contactManager;


    public static ConversationRepository getInstance()
    {
        return instance;
    }


    public ArrayList<Conversation> getConversations()
    {
        return conversations;
    }


    public ContactManager getContactManager()
    {
        return contactManager;
    }


    private ConversationRepository()
    {
        conversations = new ArrayList<Conversation>();
        contactManager = new ContactManager();
    }
}
