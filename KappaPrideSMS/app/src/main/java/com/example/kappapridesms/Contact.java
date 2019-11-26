package com.example.kappapridesms;

/**
 * Stores contact information about a user.
 *<p>
 *    Has two private attributes:
 *    <p>
 *        - m_phoneNumber
 *    </p>
 *    <p>
 *        - m_name
 *    </p>
 *
 *    Has an overloaded constructor:
 *    <p>
 *        - Contact(String name, long phoneNumber)
 *    </p>
 *
 *    Has four public methods:
 *    <p>
 *        - getPhoneNumber()
 *    </p>
 *    <p>
 *        - setPhoneNumber(long newPhone)
 *    </p>
 *    <p>
 *        - getName()
 *    </p>
 *    <p>
 *        - setName(String newName)
 *    </p>
 *
 *</p>
 * @author Mohammad Shalabi
 *
 */
public class Contact
{
    private long m_phoneNumber;
    private String m_name;

    /**
     * Overloaded constructor that constructs an
     * instance of a Contact with a name and
     * phone number.
     *
     * @param name name of the Contact
     * @param phoneNumber phone number of the Contact
     */
    public Contact (String name, long phoneNumber)
    {
        m_name = name;
        m_phoneNumber = phoneNumber;
    }

    /**
     * Returns the phone number of a Contact.
     *
     * @return m_phoneNumber phone number of contact
     */
    public long getPhoneNumber()
    {
        return m_phoneNumber;
    }

    /**
     * Returns the name of a Contact.
     *
     * @return m_name name of a contact
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * Sets a phone number for a Contact
     *
     * @param newPhone the new phone number of a Contact
     */
    public void setPhoneNumber (long newPhone)
    {
        m_phoneNumber = newPhone;
    }

    /**
     * Sets a new name for a Contact
     *
     * @param newName the new name for a Contact
     */
    public void setName(String newName)
    {
        m_name = newName;
    }

}