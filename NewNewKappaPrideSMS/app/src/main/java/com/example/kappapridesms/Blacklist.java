package com.example.kappapridesms;
import java.util.ArrayList;
import java.util.Iterator;

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
    private ArrayList<Long> m_blacklistedNumbers = new ArrayList<Long>();

    /**
     * Method that adds a preexisting contact to the blacklist
     *
     * @param blContact the contact to be blacklisted
     */
    public void addBlacklistedNumber(Long blContact)
    {
        m_blacklistedNumbers.add(blContact);
    }

    /**
     * Method that removes a contact from the blacklist
     *
     * @param blContact the contact that is to be removed from the blacklist
     */
    public void removeBlacklistedNumber(Long blContact)
    {
        for(Iterator<Long> iterator = m_blacklistedNumbers.listIterator(); iterator.hasNext(); )
        {
            Long contact = iterator.next();
            if(contact.equals(blContact))
            {
                iterator.remove();
            }
        }
    }

    /**
     * Method that will return the current number of elements in the list.
     *
     * @return size of the m_blacklistedNumbers ArrayList
     */
    public int size()
    {
        return m_blacklistedNumbers.size();
    }

    /**
     * Method will return the contact, from the blacklist, at the specific index provided.
     *
     * @param index position of the contact to be retrieved
     */
    public Long getBlacklistedContact(int index)
    {
        return m_blacklistedNumbers.get(index);
    }
}
