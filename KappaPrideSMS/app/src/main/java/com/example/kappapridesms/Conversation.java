package com.example.kappapridesms;
import java.util.ArrayList;

public class Conversation
{
    private ArrayList<Message> m_messageList;
    private long m_recipientPhone;

    public int size()
    {
        return m_messageList.size();
    }


    public void addMessage(Message newMessage)
    {
        m_messageList.add(newMessage);
    }


    public  void deleteMessage(Message deleteMessage)
    {
        m_messageList.remove(deleteMessage);
    }


    public Message getMessage(int index)
    {
        return m_messageList.get(index);
    }

    // returns the authors phone number
    public long getRecipientPhone()
    {
        return m_recipientPhone;
    }


    public Conversation(long recipientPhone)
    {
        m_messageList = new ArrayList<Message>();
        m_recipientPhone = recipientPhone;
    }
}
