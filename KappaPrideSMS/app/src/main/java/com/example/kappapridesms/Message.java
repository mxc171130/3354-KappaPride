package com.example.kappapridesms;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class Message {

    // member variables
    private long m_timestamp;
    private String m_content;

    // returns timestamp for when each message was sent
    public long getTimestamp() { return m_timestamp; }


    // returns content of a message
    public String getContent() { return m_content; }

    // returns the current date
    public String getDate()
    {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return date;
    }

    // returns the current time
    public Date getTime()
    {
        Date c = Calendar.getInstance().getTime();
        return c;
    }

    public Message(long _timestamp, String _content)
    {
        m_timestamp = _timestamp;
        m_content = _content;
    }
}
