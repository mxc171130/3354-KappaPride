package com.example.kappapridesms;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Manages the contacts of the user. It allows for the creation and deletion of contacts, as well as, retrieving and changing a contact.
 * <p>
 *     Contains two private attributes:
 *     <p>
 *         - m_contacts
 *     </p>
 *     <p>
 *         - m_size
 *     </p>
 * </p>
 * <p>
 *     Contains five public methods:
 *     <p>
 *         - addContact()
 *     </p>
 *     <p>
 *         - deleteContact()
 *     </p>
 *     <p>
 *         - getSize()
 *     </p>
 *     <p>
 *         - getContact()
 *     </p>
 *     <p>
 *         - changeContact()
 *     </p>
 * </p>
 * @author Michael Chang
 *
 */

public class ContactManager
{
    /**
     * This is the contact list, which is an arraylist of class Contact.
     */
    private ArrayList<Contact> m_contacts = new ArrayList<Contact>();

    /**
     * This is just keeps track of the size of the contacts list, with an initial size of zero.
     */
    private int m_size = 0;

    /**
     * Method that uses the two parameters it receives to add a contact to the list.
     *
     * @param phoneNumber phone number of the contact
     * @param name name of the contact
     */
    public void addContact(long phoneNumber, String name)
    {
        m_contacts.add(new Contact(name, phoneNumber));
    }

    /**
     * Method iterates through the list to see if there is a contact with matching information and deletes it; returns true if found and deleted, otherwise, false.
     *
     * @param phoneNumber phone number of the contact
     * @param name name of the contact
     * @return It will return true is the contact was removed, otherwise, it will return false.
     */
    public boolean deleteContact(long phoneNumber, String name)
    {
        for(Iterator<Contact> iterator = m_contacts.listIterator(); iterator.hasNext(); )
        {
            Contact contact = iterator.next();
            if(contact.getPhoneNumber() == phoneNumber)
            {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Methods that returns the number of elements within the list.
     *
     * @return This will return the size of the list.
     */
    public int getSize()
    {
        return m_contacts.size();
    }

    /**
     * Method iterates through the list to see if there is a contact with a matching phone number, and if so, returns the contact information.
     *
     * @param phoneNumber phone number of the contact
     * @return This will return the contact whose number matches the number passed to the method
     */
    public Contact getContact(long phoneNumber)
    {
        // Not sure if I the return is correct when dealing with a contact that doesn't exist.
        Contact failedContact = new Contact("DNE", 0000000000);
        for(Iterator<Contact> iterator = m_contacts.listIterator(); iterator.hasNext(); )
        {
            Contact contact = iterator.next();
            if(contact.getPhoneNumber() == phoneNumber)
            {
                return contact;
            }
        }
        return failedContact;
    }

    /**
     * Method that iterates through the list to see if there is a contact with matching information. If there is, it will change the information and return true; otherwise, false.
     *
     * @param contact contact whose information is to be changed
     * @param phoneNumber new phone number of contact
     * @param name new name of contact
     * @return This method will return true if the contact's info has been changed; otherwise, false.
     */
    public boolean changeContact(Contact contact, long phoneNumber, String name)
    {
        for(Iterator<Contact> iterator = m_contacts.listIterator(); iterator.hasNext(); )
        {
            Contact tempContact = iterator.next();
            if(tempContact.equals(contact))
            {
                tempContact.setPhoneNumber(phoneNumber);
                tempContact.setName(name);
                return true;
            }
        }
        return false;
    }


    public long getNumberFromName(String name)
    {
        for(Iterator<Contact> iterator = m_contacts.listIterator(); iterator.hasNext(); )
        {
            Contact tempContact = iterator.next();
            if(tempContact.getName().equals(name))
            {
                return tempContact.getPhoneNumber();
            }
        }
        return 0L;
    }


    public void saveContacts()
    {
        FileSystem.getInstance().saveContacts(m_contacts);
    }
}
