package com.example.kappapridesms;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileSystem
{
    private static FileSystem instance = new FileSystem();

    public static FileSystem getInstance()
    {
        return instance;
    }

    public ArrayList<Long> loadBlacklistNumbers()
    {
        try
        {
            Context applicationContext = KappaApplication.getAppContext();
            StringBuilder pathBuilder = new StringBuilder();

            pathBuilder.append(applicationContext.getFilesDir().getCanonicalPath());
            pathBuilder.append("/contacts/blacklist");

            File blacklistFile = new File(pathBuilder.toString());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(blacklistFile)));
            String line;

            ArrayList<Long> addedNumbers = new ArrayList<Long>();

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

    public void loadContacts(ContactManager contactManager)
    {
        try
        {
            Context applicationContext = KappaApplication.getAppContext();
            StringBuilder pathBuilder = new StringBuilder();

            pathBuilder.append(applicationContext.getFilesDir().getCanonicalPath());
            pathBuilder.append("/contacts/contacts");

            File contactsFile = new File(pathBuilder.toString());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(contactsFile)));
            String line;

            while((line = bufferedReader.readLine()) != null)
            {
                long number = Long.parseLong(line);
                line = bufferedReader.readLine();

                contactManager.addContact(number, line);
            }

            bufferedReader.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void loadConversations(ArrayList<Conversation> conversations)
    {
        try
        {
            Context applicationContext = KappaApplication.getAppContext();
            StringBuilder pathBuilder = new StringBuilder();

            pathBuilder.append(applicationContext.getFilesDir().getCanonicalPath());
            pathBuilder.append("/conversations/");

            File conversationPath = new File(pathBuilder.toString());
            File[] allConversations = conversationPath.listFiles();

            for(int i = 0; i < allConversations.length; i++)
            {
                String recipientPhone = allConversations[i].getName();
                Conversation addConversation = new Conversation(Long.parseLong(recipientPhone));

                File[] allMessages = allConversations[i].listFiles();

                for(int j = 0; j < allMessages.length; j++)
                {
                    // Obtain a BufferedReader to read each message
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(allMessages[j])));

                    boolean sentFromThisPhone = bufferedReader.readLine().equals("true");

                    StringBuilder contentBuilder = new StringBuilder();
                    String line;

                    while((line = bufferedReader.readLine()) != null)
                    {
                        contentBuilder.append(line);
                    }

                    bufferedReader.close();

                    long timestamp = Long.parseLong(allMessages[j].getName());

                    // Recreate the message object and add it to the conversation
                    Message addMessage = new Message(timestamp, sentFromThisPhone, contentBuilder.toString());
                    addConversation.addMessage(addMessage);
                }

                conversations.add(addConversation);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void saveConversations(ArrayList<Conversation> conversations)
    {
        try
        {
            Context applicationContext = KappaApplication.getAppContext();

            for(int i = 0; i < conversations.size(); i++)
            {
                Conversation targetConversation = conversations.get(i);

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

                File[] cleanArrayFiles = directoryTestFile.listFiles();

                for(File deleteFile : cleanArrayFiles)
                {
                    deleteFile.delete();
                }

                saveConversation(directoryBuilder.toString(), targetConversation);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void saveConversation(String directory, Conversation conversation)
    {
        try
        {
            for (int j = 0; j < conversation.size(); j++)
            {
                Message saveMessage = conversation.getMessage(j);

                StringBuilder fileBuilder = new StringBuilder();
                fileBuilder.append(directory);
                fileBuilder.append(saveMessage.getTimestamp());

                PrintWriter printWriter = new PrintWriter(new File(fileBuilder.toString()));

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

    public void saveContacts(ArrayList<Contact> contacts)
    {
        try
        {
            Context applicationContext = KappaApplication.getAppContext();
            StringBuilder pathBuilder = new StringBuilder();

            pathBuilder.append(applicationContext.getFilesDir().getCanonicalPath());
            pathBuilder.append("/contacts/contacts");

            File contactsFile = new File(pathBuilder.toString());

            PrintWriter printWriter = new PrintWriter(contactsFile);

            for(int i = 0; i < contacts.size(); i++)
            {
                Contact saveContact = contacts.get(i);

                printWriter.println(saveContact.getPhoneNumber());
                printWriter.println(saveContact.getName());
            }

            printWriter.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void saveBlacklistNumbers(Blacklist blacklist)
    {
        try
        {
            Context applicationContext = KappaApplication.getAppContext();
            StringBuilder pathBuilder = new StringBuilder();

            pathBuilder.append(applicationContext.getFilesDir().getCanonicalPath());
            pathBuilder.append("/contacts/blacklist");

            File blacklistFile = new File(pathBuilder.toString());

            PrintWriter printWriter = new PrintWriter(blacklistFile);

            for(int i = 0; i < blacklist.size(); i++)
            {
                Contact saveNumberFromContact = blacklist.getBlacklistedContact(i);

                printWriter.println(saveNumberFromContact.getPhoneNumber());
            }

            printWriter.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private FileSystem()
    {
        try
        {
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
