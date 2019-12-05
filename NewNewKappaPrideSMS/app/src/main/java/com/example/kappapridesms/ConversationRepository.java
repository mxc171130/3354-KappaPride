package com.example.kappapridesms;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.*;

/**
 * ConversationRepository functions as a central hub for all the message data,
 * conversation data, contact data, and blacklist data. Users of this class
 * retrieve a singleton instance through the getInstance() method. Hence, all
 * modifications to the data are reflected in every retrieved instance since
 * there is only one instance.
 *
 * <p>
 *     Contains four private attributes:
 *     <p>
 *         - m_instance
 *     </p>
 *     <p>
 *         - m__conversations
 *     </p>
 *     <p>
 *         - m_targetConversation
 *     </p>
 *     <p>
 *         - m_blacklist
 *     </p>
 * </p>
 * <p>
 *     Contains nine public methods:
 *     <p>
 *         - addConversation()
 *     </p>
 *     <p>
 *         - getInstance()
 *     </p>
 *     <p>
 *         - getBlacklist()
 *     </p>
 *     <p>
 *         - getConversation()
 *     </p>
 *     <p>
 *         - getTargetConversation()
 *     </p>
 *     <p>
 *         - loadConversations()
 *     </p>
 *     <p>
 *         - setTargetConversation()
 *     </p>
 *     <p>
 *         - getContactName()
 *     </p>
 *     <p>
 *         - getPhoneNumber()
 *     </p>
 * </p>
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
        m_blacklist = new Blacklist();
        m_targetConversation = new Conversation(0);
    }

    /**
     * Method that gets the contact name by using two parameters received: phoneNumber and context. phoneNumber being the phone number of the contact and the context being the Activity.
     *
     * @param phoneNumber String that holds the value of the phone number.
     * @param context Current state of the application.
     * @return String value of the name of the contact retrieved from the Android ContactsContract class.
     */
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

    /**
     * Method that gets the phone number using two parameters: name and context. name being the name of the contact and context being the current state of the application.
     *
     * @param name String value of the name of the contact.
     * @param context Current state of the application.
     * @return Long value of the phone number retrieved from the Android ContactsContract class.
     */
    public Long getPhoneNumber(final String name, Context context)
    {
        String number="";


        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor people = context.getContentResolver().query(uri, projection, null, null, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        people.moveToFirst();

        do
        {
            String perName = people.getString(indexName);
            String perNumber = people.getString(indexNumber);

            if(perName.equalsIgnoreCase(name))
            {
                perNumber = perNumber.replace(" ", "");
                perNumber = perNumber.replace("(", "");
                perNumber = perNumber.replace(")", "");
                perNumber = perNumber.replace("-", "");
                perNumber = "1" + perNumber;
                return Long.parseLong(perNumber);
            }
        } while (people.moveToNext());


        if(!number.equalsIgnoreCase(""))
        {
            number = number.replace(" ", "");
            number = number.replace("(", "");
            number = number.replace(")", "");
            number = number.replace("-", "");
            number = "1" + number;
            return Long.parseLong(number);
        }
        else return 0L;
    }
}
