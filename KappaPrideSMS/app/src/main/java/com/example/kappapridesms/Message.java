package com.example.kappapridesms;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class Message {

    // member variables
    private long _timestamp;
    private long _authorPhone;
    private long _receiverPhone;
    private String _content;

    // returns timestamp for when each message was sent
    public long getTimestamp() { return _timestamp; }

    // returns the authors phone number
    public long getAuthorPhone()
    {
        return _authorPhone;
    }

    // returns the receivers phone number
    public long get_receiverPhone()
    {
        return _receiverPhone;
    }

    // returns content of a message
    public String getContent() { return _content; }

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

}
