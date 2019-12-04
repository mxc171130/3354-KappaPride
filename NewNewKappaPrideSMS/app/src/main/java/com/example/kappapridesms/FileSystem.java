package com.example.kappapridesms;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * This class implements all of the I/O operations performed by this application.
 * All modifications to ConversationRepository's fields should be handled by the
 * appropriate save feature implemented in this class. This class uses the Singleton
 * design pattern to ensure that only one FileSystem is in use.
 *
 * @author Nathan Beck
 */
public class FileSystem
{
    /**
     * The sole instance of FileSystem
     */
    private static FileSystem instance = new FileSystem();

    /**
     * Returns the sole instance of FileSystem
     *
     * @return The sole instance of FileSystem
     */
    public static FileSystem getInstance()
    {
        return instance;
    }

    /**
     * Returns an array of phone numbers that are present in the file system.
     *
     * @return An ArrayList of phone numbers present in the saved blacklist
     */
    public ArrayList<Long> loadBlacklistNumbers()
    {
        try
        {
            // Get this application's path to its data storage
            Context applicationContext = KappaApplication.getAppContext();
            StringBuilder pathBuilder = new StringBuilder();

            // Begin building the path to the blacklist
            pathBuilder.append(applicationContext.getFilesDir().getCanonicalPath());
            pathBuilder.append("/contacts/blacklist");

            // Create a file to open the blacklist
            File blacklistFile = new File(pathBuilder.toString());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(blacklistFile)));
            String line;

            ArrayList<Long> addedNumbers = new ArrayList<Long>();

            // Read each number into the ArrayList of blacklisted numbers
            while((line = bufferedReader.readLine()) != null)
            {
                addedNumbers.add(Long.parseLong(line));
            }

            bufferedReader.close();

            return addedNumbers;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }


    /**
     * Loads the conversations present in the file system into the passed ArrayList.
     *
     * @param conversations The ArrayList in which to load the conversations
     */
    public void loadConversations(ArrayList<Conversation> conversations)
    {
        try
        {
            // Get the root path associated with this application's data
            Context applicationContext = KappaApplication.getAppContext();
            StringBuilder pathBuilder = new StringBuilder();

            // Begin to construct the conversations directory
            pathBuilder.append(applicationContext.getFilesDir().getCanonicalPath());
            pathBuilder.append("/conversations/");

            // Open the directory and retrieve all files in the directory.
            // Each file in this directory is a conversation, which is a directory that
            // houses each message in the conversation. The name of the file in each
            // conversation directory is the timestamp of the message while its contents
            // are the actual message content to be displayed.
            File conversationPath = new File(pathBuilder.toString());
            File[] allConversations = conversationPath.listFiles();

            if(allConversations == null)
            {
                return;
            }

            for(int i = 0; i < allConversations.length; i++)
            {
                // Create a conversation based on the name of the file (which houses the phone number)
                String recipientPhone = allConversations[i].getName();
                Conversation addConversation = new Conversation(Long.parseLong(recipientPhone));

                // Obtain all messages in the conversation
                File[] allMessages = allConversations[i].listFiles();

                for(int j = 0; j < allMessages.length; j++)
                {
                    // Obtain a BufferedReader to read each message
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(allMessages[j])));

                    // Determine if the message was sent from this phone by reading the first line
                    boolean sentFromThisPhone = bufferedReader.readLine().equals("true");

                    StringBuilder contentBuilder = new StringBuilder();
                    String line;

                    // Obtain the message content by reading each line thereafter
                    while((line = bufferedReader.readLine()) != null)
                    {
                        contentBuilder.append(line);
                    }

                    bufferedReader.close();

                    // Obtain the timestamp by reading the name
                    long timestamp = Long.parseLong(allMessages[j].getName());

                    // Recreate the message object and add it to the conversation
                    Message addMessage = new Message(timestamp, sentFromThisPhone, contentBuilder.toString());
                    addConversation.addMessage(addMessage);
                }

                // Add the newly constructed conversation to the passed ArrayList
                conversations.add(addConversation);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Saves each conversation in the passed ArrayList to the file system.
     *
     * @param conversations The ArrayList of conversations to be saved
     */
    public void saveConversations(ArrayList<Conversation> conversations)
    {
        try
        {
            Context applicationContext = KappaApplication.getAppContext();

            for(int i = 0; i < conversations.size(); i++)
            {
                Conversation targetConversation = conversations.get(i);

                // Create the directory in which to save this conversation
                StringBuilder directoryBuilder = new StringBuilder();
                directoryBuilder.append(applicationContext.getFilesDir().getCanonicalPath());
                directoryBuilder.append("/conversations/");
                directoryBuilder.append(targetConversation.getRecipientPhone());
                directoryBuilder.append("/");

                File directoryTestFile = new File(directoryBuilder.toString());

                if(!directoryTestFile.exists())
                {
                    directoryTestFile.mkdirs();
                }

                // Remove all stale data from this directory
                File[] cleanArrayFiles = directoryTestFile.listFiles();

                for(File deleteFile : cleanArrayFiles)
                {
                    deleteFile.delete();
                }

                // Save the individual conversation
                saveConversation(directoryBuilder.toString(), targetConversation);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * Saves the passed conversation to the passed directory.
     *
     * @param directory The directory in which to save the conversation
     * @param conversation The conversation to save
     */
    private void saveConversation(String directory, Conversation conversation)
    {
        try
        {
            for (int j = 0; j < conversation.size(); j++)
            {
                // Get the message content and create the file in which to save
                Message saveMessage = conversation.getMessage(j);

                StringBuilder fileBuilder = new StringBuilder();
                fileBuilder.append(directory);
                fileBuilder.append(saveMessage.getTimestamp());

                PrintWriter printWriter = new PrintWriter(new File(fileBuilder.toString()));

                // Print message information
                if(saveMessage.isSentFromThisPhone())
                {
                    printWriter.println("true");
                }
                else
                {
                    printWriter.println("false");
                }

                printWriter.println(saveMessage.getContent());

                printWriter.close();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * Saves the blacklisted numbers to the file system.
     *
     * @param blacklist The blacklist to save
     */
    public void saveBlacklistNumbers(Blacklist blacklist)
    {
        try
        {
            // Get the root path of this application's data
            Context applicationContext = KappaApplication.getAppContext();
            StringBuilder pathBuilder = new StringBuilder();

            // Finish building the path to the blacklist
            pathBuilder.append(applicationContext.getFilesDir().getCanonicalPath());
            pathBuilder.append("/contacts/blacklist");

            File blacklistFile = new File(pathBuilder.toString());

            PrintWriter printWriter = new PrintWriter(blacklistFile);

            // Write the blacklisted numbers to the file system
            for(int i = 0; i < blacklist.size(); i++)
            {
                printWriter.println(blacklist.getBlacklistedContact(i));
            }

            printWriter.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Creates the sole instance of FileSystem
     */
    private FileSystem()
    {
        try
        {
            // Create the contacts folder if it doesn't exist upon initialization
            Context applicationContext = KappaApplication.getAppContext();
            StringBuilder pathBuilder = new StringBuilder();

            pathBuilder.append(applicationContext.getFilesDir().getCanonicalPath());
            pathBuilder.append("/contacts/");

            File contactsLoc = new File(pathBuilder.toString());

            if(!contactsLoc.exists())
            {
                contactsLoc.mkdirs();
            }
        }
        catch(Exception ex)
        {

        }
    }
}
