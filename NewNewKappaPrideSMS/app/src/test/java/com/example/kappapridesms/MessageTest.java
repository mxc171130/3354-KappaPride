package com.example.kappapridesms;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for the Message class. This tests the timestamp, content, sentFromPhone, and the year.
 *
 * @author Michael Chang
 */
public class MessageTest {
    /**
     * This initializes the initial object of class Message.
     */
    private Message message;

    /**
     * This method initializes the values before the actual testing happens.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {
        message = new Message(11111111, true, "This is the test message.");
    }

    /**
     * This method will test if the timestamps are correct.
     */
    @Test
    public void timestampTest()
    {
        assertEquals(11111111, message.getTimestamp());
    }

    /**
     * This method will test if the content is correct.
     */
    @Test
    public void contentTest()
    {
        assertEquals("This is the first test message.", message.getContent());
    }

    /**
     * This method will test if the boolean value is the same.
     */
    @Test
    public void sentFromPhoneTest()
    {
        assertEquals(true, message.isSentFromThisPhone());
    }

    /**
     * This method will test if the year returned is the correct one.
     */
    @Test
    public void yearTest()
    {
        //String initialYear = message.getDate();
        assertEquals("12-31-1969", message.getDate());
    }
}