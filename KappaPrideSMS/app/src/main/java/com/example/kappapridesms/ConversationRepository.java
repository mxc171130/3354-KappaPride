package com.example.kappapridesms;

import java.util.*;

public class ConversationRepository
{
    private static final ConversationRepository m_instance = new ConversationRepository();

    private ArrayList<Conversation> m_conversations;
    private Conversation m_targetConversation;
    private ContactManager m_contactManager;


    public void addConversation(Conversation addConversation)
    {
        m_conversations.add(addConversation);
    }


    public static ConversationRepository getInstance()
    {
        return m_instance;
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
    }
}
