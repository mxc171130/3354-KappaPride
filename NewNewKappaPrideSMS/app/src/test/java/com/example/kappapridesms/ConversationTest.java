package com.example.kappapridesms;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * This tests the size(), addMessage(), deleteMessage, and getMssage()
 * , and getRecipientPhone() methods of the Conversation class.
 *
 * @author Mohammad Shalabi
 */
public class ConversationTest
{

    Conversation tester;

    /**
     * Sets up the tester by instanciating a Conversation
     */
    @Before
    public void setUp()
    {
        tester = new Conversation(999999999);

    }

    /**
     * Tests the size() Conversation method
     */
    @Test
    public void size()
    {
        Message msg = new Message(9999999, true, "Hello!");
        tester.addMessage(msg);

        assertEquals("This should return true", 1, tester.size());

    }

    /**
     * Tests the addMessage() Conversation method
     */
    @Test
    public void addMessage()
    {

        // adding the message
        Message msg = new Message(11111111, true, "Hello!");
        tester.addMessage(msg);

        assertEquals("This should return true", 1, tester.size());

    }

    /**
     * Tests the deleteMessage() Conversation method
     */
    @Test
    public void deleteMessage()
    {
        Message msg1 = new Message(11111111, false, "Hello!");
        Message msg2 = new Message(22222222, false, "Hello");
        Message msg3 = new Message(33333333, false, "hello");
        tester.addMessage(msg1);
        tester.addMessage(msg2);
        tester.addMessage(msg3);
        tester.deleteMessage(msg1);
        assertEquals("This is return true", 2, tester.size());

    }

    /**
     * Tests the getMessage() Conversation method
     */
    @Test
    public void getMessage()
    {
        Message msg = new Message(1010101011, false, "Hello!");
        tester.addMessage(msg);

        Message msgCopy = msg;

        assertEquals("This should return true", msgCopy, tester.getMessage(0));

    }

    /**
     * Tests the getRecipientPhone() Conversation method
     */
    @Test
    public void getRecipientPhone()
    {
        long num = 999999999;
        assertEquals("This should return true", num, tester.getRecipientPhone());
    }


}