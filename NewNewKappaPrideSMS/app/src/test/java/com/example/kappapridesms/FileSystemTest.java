package com.example.kappapridesms;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class contains 3 unit tests for the persistence system of the app.
 * Saving and loading conversations, messages within conversations, and the
 * numbers in the blacklist is tested.
 *
 * @author Nathan Beck
 */
@RunWith(AndroidJUnit4.class)
public class FileSystemTest
{
    /**
     * The ConversationRepository instance to save information from
     */
    ConversationRepository m_instance;

    /**
     * The FileSystem instance to test
     */
    FileSystem m_fileSystem;

    /**
     * The test fixture for these unit tests
     */
    @Before
    public void setUp()
    {
        m_instance = ConversationRepository.getInstance();
        m_fileSystem = FileSystem.getInstance();
    }

    /**
     * The first file system test. Here, a new conversation is added and saved. We
     * test we can load it back using the file system.
     */
    @Test
    public void persistenceTest()
    {
        // Add the new conversation and save it
        m_instance.addConversation(new Conversation(5435435432L));
        m_fileSystem.saveConversations(m_instance.getConversations());

        // Load the conversations back from the file system.
        ArrayList<Conversation> loadedConversations = new ArrayList<Conversation>();
        m_fileSystem.loadConversations(loadedConversations);

        // Set a conversation existence flag
        boolean isConversationPresent = false;

        // Set the flag true if we find a conversation with the same attached number
        for(Conversation conversation : loadedConversations)
        {
            if(conversation.getRecipientPhone() == 5435435432L)
            {
                isConversationPresent = true;
            }
        }

        // Pass this test if the flag is set
        assertTrue(isConversationPresent);
    }

    /**
     * The second persistence test. Tests if it saves messages correctly within a conversation.
     */
    @Test
    public void persistenceTest2()
    {
        // Specify a new conversation and message to add
        Conversation addConversation = new Conversation(5435435431L);
        Message addingMessage = new Message(new Date().getTime(), true, "Test");
        addConversation.addMessage(addingMessage);

        // Save the new message and conversation
        m_instance.addConversation(addConversation);
        m_fileSystem.saveConversations(m_instance.getConversations());

        // Load the conversation back
        ArrayList<Conversation> loadedConversations = new ArrayList<Conversation>();
        m_fileSystem.loadConversations(loadedConversations);

        // Find the conversation
        for(Conversation conversation : loadedConversations)
        {
            if(conversation.getRecipientPhone() == 5435435431L)
            {
                // After finding the vonersation, find the message
                for(int i = 0; i < conversation.size(); i++)
                {
                    Message testMessage = conversation.getMessage(i);

                    if(testMessage.getTimestamp() == addingMessage.getTimestamp())
                    {
                        assertEquals(testMessage.getContent(), addingMessage.getContent());
                        assertEquals(testMessage.isSentFromThisPhone(), addingMessage.isSentFromThisPhone());
                        return;
                    }
                }
            }
        }

        // The conversation or message could not be found. Fail this test.
        fail();
    }

    /**
     * Persistence test for the blacklist. Tests 3 numbers and checks if they are saved and loaded
     * correctly.
     */
    @Test
    public void persistenceTest3()
    {
        // Initiate a blacklist and add some numbers to it
        Blacklist testBlacklist = new Blacklist();
        testBlacklist.addBlacklistedNumber(5555550123L);
        testBlacklist.addBlacklistedNumber(1234567890L);
        testBlacklist.addBlacklistedNumber(9876543210L);

        // Save and load the blacklist numbers
        m_fileSystem.saveBlacklistNumbers(testBlacklist);
        ArrayList<Long> loadedNumbers = m_fileSystem.loadBlacklistNumbers();

        // Set seen flags
        int firstMatch = 0;
        int secondMatch = 0;
        int thirdMatch = 0;

        // Go over all numbers, marking each number seen
        for(Long number : loadedNumbers)
        {
            if(number == 5555550123L)
            {
                firstMatch++;
            }
            else if(number == 1234567890L)
            {
                secondMatch++;
            }
            else if(number == 9876543210L)
            {
                thirdMatch++;
            }
        }

        // Make sure every number was seen exactly once
        assertEquals(firstMatch, 1);
        assertEquals(secondMatch, 1);
        assertEquals(thirdMatch, 1);
    }
}
