package com.example.kappapridesms;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MessageTest {
    private Message message;
    //private Message testMessage1 = new Message(11111111, true, "This is the first test message.");
    //private Message testMessage2 = new Message(22222222, true, "This is the second test message.");

    @Before
    public void setUp() throws Exception
    {
        message = new Message(11111111, true, "This is the test message.");
    }

    @Test
    public void timestampTest()
    {
        assertEquals(11111111, message.getTimestamp());
    }

    @Test
    public void contentTest()
    {
        assertEquals("This is the first test message.", message.getContent());
    }

    @Test
    public void sentFromPhoneTest()
    {
        assertEquals(true, message.isSentFromThisPhone());
    }

    @Test
    public void yearTest()
    {
        //String initialYear = message.getDate();
        assertEquals("12-31-1969", message.getDate());
    }
}