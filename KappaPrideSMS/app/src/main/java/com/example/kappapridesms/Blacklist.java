package com.example.kappapridesms;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages the blacklisted contacts. It allows for the addition and removal of contacts, as well as, changing, loading, and getting the blacklist; you can also get the size of the blacklist.
 * <p>
 *     Contains two private attributes:
 *     <p>
 *         - m_blacklistedContacts
 *     </p>
 *     <p>
 *         - m_size
 *     </p>
 * </p>
 * <p>
 *     Contains six public methods:
 *     <p>
 *         - addBlacklistedContact()
 *     </p>
 *     <p>
 *         - removeBlacklistedContact()
 *     </p>
 *     <p>
 *         - changeBlackListedContact()
 *     </p>
 *     <p>
 *         - getSize()
 *     </p>
 *     <p>
 *         - loadBlacklist()
 *     </p>
 *     <p>
 *         - getBlacklistedContact()
 *     </p>
 * </p>
 *
 * @author Michael Chang
 */

public class Blacklist
{
    /**
     * This is the list of blacklisted contacts
     */
    private ArrayList<Contact> m_blacklistedContacts = new ArrayList<Contact>();

    /**
     * This is used to keep track of the size of the blacklist, with an initial value of zero.
     */
    private int m_size = 0;

    /**
     * Method that adds a preexisting contact to the blacklist
     *
     * @param blContact the contact to be blacklisted
     */
    public void addBlacklistedContact(Contact blContact)
    {
        m_blacklistedContacts.add(blContact);
    }

    /**
     * Method that removes a contact from the blacklist
     *
     * @param blContact the contact that is to be removed from the blacklist
     */
    public void removeBlacklistedContact(Contact blContact)
    {
        for(Iterator<Contact> iterator = m_blacklistedContacts.listIterator(); iterator.hasNext(); )
        {
            Contact contact = iterator.next();
            if(contact.equals(blContact))
            {
                iterator.remove();
            }
        }
    }

    /**
     * Method that changes the existing blacklisted contact's number to a new number
     *
     * @param blContact contact whos number is to be changed
     * @param newNumber the new number of the blacklisted contact
     */
    public void changeBlacklistedContact(Contact blContact, long newNumber)
    {
        for(Iterator<Contact> iterator = m_blacklistedContacts.listIterator(); iterator.hasNext(); )
        {
            Contact tempContact = iterator.next();
            if(tempContact.equals(blContact))
            {
                tempContact.setPhoneNumber(newNumber);
            }
        }
    }

    /**
     * Method that will return the current number of elements in the list.
     */
    public int size()
    {
        return m_blacklistedContacts.size();
    }

    /**
     *
     */
    public void loadBlacklist()
    {
        // Not really sure what you want this method to do.
    }

    /**
     * Method will return the contact, from the blacklist, at the specific index provided.
     *
     * @param index position of the contact to be retrieved
     */
    public Contact getBlacklistedContact(int index)
    {
        return m_blacklistedContacts.get(index);
    }
}
