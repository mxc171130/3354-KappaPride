package com.example.kappapridesms;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Stores information about messages.
 * <p>
 *     Contains three private attributes:
 *     <p>
 *         - m_timestamp
 *     </p>
 *     <p>
 *         - m_sentFromThisPhone
 *     </p>
 *     <p>
 *        - m_content
 *     </p>
 *
 * </p>
 * <p>
 *     Contains five public methods:
 *     <p>
 *         - getTimestamp()
 *     </p>
 *     <p>
 *         - getContent()
 *     </p>
 *     <p>
 *         - getDate()
 *     </p>
 *     <p>
 *         - isSentFromThisPhone()
 *     </p>
 *     <p>
 *         - getTime()
 *     </p>
 * </p>
 *
 * edited by Mohammad Shalabi
 */
public class Message
{
    private long m_timestamp;
    private boolean m_sentFromThisPhone;
    private String m_content;

    /**
     * Overloaded constructor that constructs an
     * instance of a Message with a timestamp,
     * sentFromThisPhone boolean value, and content.
     *
     * @param _timestamp time stamp of the message
     * @param _sentFromThisPhone boolean value to check if message was sent from this phone
     * @param _content the contents of a message
     */
    public Message(long _timestamp, boolean _sentFromThisPhone, String _content)
    {
        m_timestamp = _timestamp;
        m_sentFromThisPhone = _sentFromThisPhone;
        m_content = _content;
    }

    /**
     * Returns timestamp for when each message was sent
     *
     * @return m_timestamp time stamp of a message
     */
    public long getTimestamp()
    {
        return m_timestamp;
    }

    /**
     * Returns content of a message
     *
     * @return m_content content of a message
     */
    public String getContent()
    {
        return m_content;
    }

    /**
     * Returns the current date
     *
     * @return date content of a message
     */
    public String getDate()
    {
        String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date(m_timestamp));
        return date;
    }

    /**
     * Checks if message was sent from this phone.
     *
     * @return m_sentFromThisPhone boolean value to see if message was sent from this phone.
     */
    public boolean isSentFromThisPhone()
    {
        return m_sentFromThisPhone;
    }

    /**
     * Returns the current time
     *
     * @return time current time
     */
    public String getTime()
    {
        String time = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date(m_timestamp));
        return time;
    }


}
