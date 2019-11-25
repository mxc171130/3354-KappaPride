package com.example.kappapridesms;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class Message
{
    // member variables
    private long m_timestamp;
    private boolean m_sentFromThisPhone;
    private String m_content;

    // returns timestamp for when each message was sent
    public long getTimestamp() { return m_timestamp; }


    // returns content of a message
    public String getContent() { return m_content; }

    // returns the current date
    public String getDate()
    {
        String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date(m_timestamp));
        return date;
    }

    public boolean isSentFromThisPhone()
    {
        return m_sentFromThisPhone;
    }

    // returns the current time
    public String getTime()
    {
        return new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date(m_timestamp));
    }

    public Message(long _timestamp, boolean _sentFromThisPhone, String _content)
    {
        m_timestamp = _timestamp;
        m_sentFromThisPhone = _sentFromThisPhone;
        m_content = _content;
    }
}
