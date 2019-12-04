package com.example.kappapridesms;

import java.util.*;

/**
 * ConversationRepository functions as a central hub for all the message data,
 * conversation data, contact data, and blacklist data. Users of this class
 * retrieve a singleton instance through the getInstance() method. Hence, all
 * modifications to the data are reflected in every retrieved instance since
 * there is only one instance.
 *
 * @author Nathan Beck
 */
public class ConversationRepository
{
    /**
     * The sole instance of ConversationRepository, retrieved through getInstance()
     */
    private static final ConversationRepository m_instance = new ConversationRepository();

    /**
     * The active conversations, holding all message data and conversation data
     */
    private ArrayList<Conversation> m_conversations;

    /**
     * The target conversation to be displayed and queried for message additions
     */
    private Conversation m_targetConversation;

    /**
     * The manager for all the contacts, holding all the contact data
     */
    private ContactManager m_contactManager;

    /**
     * The blacklist, holding all blacklisted contacts
     */
    private Blacklist m_blacklist;


    /**
     * Adds a new conversation to the central repository.
     *
     * @param addConversation The conversation to be added
     */
    public void addConversation(Conversation addConversation)
    {
        m_conversations.add(addConversation);
    }


    /**
     * Returns the sole instance of ConversationRepository.
     *
     * @return The sole instance of ConversationRepository.
     */
    public static ConversationRepository getInstance()
    {
        return m_instance;
    }


    /**
     * Retrieves the blacklist of this ConversationRepository
     *
     * @return The blacklist of this ConversationRepository
     */
    public Blacklist getBlacklist()
    {
        return m_blacklist;
    }


    /**
     * Retrieves all conversations of this ConversationRepository
     *
     * @return All Conversations held by this ConversationRepository
     */
    public ArrayList<Conversation> getConversations()
    {
        return m_conversations;
    }


    /**
     * Retrieves the ContactManager of this ConversationRepository
     *
     * @return The ContactManager of this ConversationRepository
     */
    public ContactManager getContactManager()
    {
        return m_contactManager;
    }


    /**
     * Retrieves the current targeted conversation of this ConversationRepository
     *
     * @return The targeted conversation of this ConversationRepository
     */
    public Conversation getTargetConversation()
    {
        return m_targetConversation;
    }


    /**
     * Loads any conversation data present in the file system using FileSystem.
     */
    public void loadConversations()
    {
        // Load the conversations into a new ArrayList
        ArrayList<Conversation> loadedConversations = new ArrayList<Conversation>();
        FileSystem.getInstance().loadConversations(loadedConversations);

        // Scan through the new ArrayList. Update the target conversation to the conversation in the new
        // ArrayList with the matching number, also setting the conversations to the loaded ArrayList if
        // successful.

        m_conversations = loadedConversations;

        for(int i = 0; i < loadedConversations.size(); i++)
        {
            if(loadedConversations.get(i).getRecipientPhone() == getTargetConversation().getRecipientPhone())
            {
                setTargetConversation(i);
                break;
            }
        }
    }


    /**
     * Sets the target conversation to that specified by the passed index into the conversation ArrayList
     * of this ConversationRepository.
     *
     * @param index The index of this ConversationRepository's conversations ArrayList in which to select
     *              the new target conversation
     */
    public void setTargetConversation(int index)
    {
        m_targetConversation = m_conversations.get(index);
    }

    /**
     * Creates the sole instance of ConversationRepository, initializing its fields
     */
    private ConversationRepository()
    {
        m_conversations = new ArrayList<Conversation>();
        m_contactManager = new ContactManager();
        m_blacklist = new Blacklist();
        m_targetConversation = new Conversation(0);
    }
}
