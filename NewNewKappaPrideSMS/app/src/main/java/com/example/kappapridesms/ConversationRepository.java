package com.example.kappapridesms;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.*;

public class ConversationRepository
{
    private static final ConversationRepository m_instance = new ConversationRepository();

    private ArrayList<Conversation> m_conversations;
    private Conversation m_targetConversation;
    private ContactManager m_contactManager;
    private Blacklist m_blacklist;


    public void addConversation(Conversation addConversation)
    {
        m_conversations.add(addConversation);
    }


    public static ConversationRepository getInstance()
    {
        return m_instance;
    }


    public Blacklist getBlacklist()
    {
        return m_blacklist;
    }


    public ArrayList<Conversation> getConversations()
    {
        return m_conversations;
    }


    public ContactManager getContactManager()
    {
        return m_contactManager;
    }


    public Conversation getTargetConversation()
    {
        return m_targetConversation;
    }


    public void loadConversations()
    {
        ArrayList<Conversation> loadedConversations = new ArrayList<Conversation>();
        FileSystem.getInstance().loadConversations(loadedConversations);

        for(int i = 0; i < loadedConversations.size(); i++)
        {
            if(loadedConversations.get(i).getRecipientPhone() == getTargetConversation().getRecipientPhone())
            {
                m_conversations = loadedConversations;
                setTargetConversation(i);
                break;
            }
        }
    }


    public void setTargetConversation(int index)
    {
        m_targetConversation = m_conversations.get(index);
    }

    private ConversationRepository()
    {
        m_conversations = new ArrayList<Conversation>();
        m_contactManager = new ContactManager();
        m_blacklist = new Blacklist();
    }

    public String getContactName(final String phoneNumber, Context context)
    {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName = "";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                contactName = cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }

}
