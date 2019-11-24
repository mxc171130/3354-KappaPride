package com.example.kappapridesms;
import java.util.ArrayList;

public class Conversation
{
    private ArrayList<Message> m_messageList;
    private long m_authorPhone;
    private long m_receiverPhone;

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
    public long getAuthorPhone()
    {
        return m_authorPhone;
    }

    // returns the receivers phone number
    public long getReceiverPhone()
    {
        return m_receiverPhone;
    }


    public Conversation(long authorPhone, long receiverPhone)
    {
        m_messageList = new ArrayList<Message>();
        m_authorPhone = authorPhone;
        m_receiverPhone = receiverPhone;
    }
}
