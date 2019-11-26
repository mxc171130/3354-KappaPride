package com.example.kappapridesms;
import java.util.ArrayList;

/**
 * Stores information about conversations.
 *<p>
 *    Has two private attributes:
 *    <p>
 *        - m_messageList
 *    </p>
 *    <p>
 *        - m_recipientPhone
 *    </p>
 *
 *    Has overload Constructor
 *    <p>
 *        - Conversation(long recipientPhone)
 *    </p>
 *
 *    Has five public methods:
 *    <p>
 *        - size()
 *    </p>
 *    <p>
 *        - addMessage(Message newMessage)
 *    </p>
 *    <p>
 *        - deleteMessage(Message deleteMessage)
 *    </p>
 *    <p>
 *        - getMessage(int index)
 *    </p>
 *    <p>
 *        - getRecipientPhone()
 *    </p>
 *
 *</p>
 *
 * edited by Mohammad Shalabi
 *
 */
public class Conversation
{
    private ArrayList<Message> m_messageList;
    private long m_recipientPhone;

    /**
     * Overloaded constructor that constructs an
     * instance of a Conversation with a recipient phone number.
     *
     * @param recipientPhone recepient phone number
     */
    public Conversation(long recipientPhone)
    {
        m_messageList = new ArrayList<Message>();
        m_recipientPhone = recipientPhone;
    }

    /**
     * Returns size of message list
     *
     * @return size size of message list
     */
    public int size()
    {
        int size = m_messageList.size();
        return size;
    }

    /**
     * Adds a new message to a message list.
     *
     * @param newMessage new message to add
     */
    public void addMessage(Message newMessage)
    {
        m_messageList.add(newMessage);
    }

    /**
     * Deletes the message that is passed to the method.
     *
     * @param deleteMessage message to delete
     */
    public void deleteMessage(Message deleteMessage)
    {
        m_messageList.remove(deleteMessage);
    }

    /**
     * Returns a message at the specified index.
     *
     * @param index index where to get the message
     * @return msg message at the specified index.
     */
    public Message getMessage(int index)
    {
        Message msg = m_messageList.get(index);
        return msg;
    }

    /**
     * Returns the authors phone number.
     *
     * @return m_recipientPhone author's phone number
     */
    public long getRecipientPhone()
    {
        return m_recipientPhone;
    }

}
