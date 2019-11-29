package com.example.kappapridesms;

import android.app.Activity;
import java.util.List;
import java.util.Iterator;

public class ConversationActivity extends Activity
{
    private ConversationRepository m_conversationRep = ConversationRepository.getInstance();

    public void deleteConversation(Conversation conversationToDelete)
    {
        Iterator<Conversation> iterator = m_conversationRep.getConversations().iterator();
        while (iterator.hasNext())
        {
            Conversation conversation = iterator.next();
            if (conversation.equals(conversationToDelete))
            {
                iterator.remove();
            }
        }

    }

    //TODO
    public void displayDeleteWarning(String warning)
    {

    }

    //TODO
    public void searchConversationsForMessages(String query)
    {

    }

    //TODO
    public void viewConversations()
    {

    }

    //TODO
    public void scrollConversationList(boolean up)
    {

    }

    //TODO
    public void zoom(double scale)
    {

    }

    //TODO
    public void loadConversation()
    {
        // not sure what we are going to do here
    }

    //TODO
    public void createContact()
    {

    }





}