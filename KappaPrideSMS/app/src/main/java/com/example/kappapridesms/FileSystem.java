package com.example.kappapridesms;

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
            File blacklistFile = new File("contacts/blacklist.txt");

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
            File contactsFile = new File("contacts/contacts.txt");

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
            File conversationPath = new File("conversations");
            File[] allConversations = conversationPath.listFiles();

            for(int i = 0; i < allConversations.length; i++)
            {
                Conversation addConversation = new Conversation();

                File[] allMessages = allConversations[i].listFiles();

                for(int j = 0; j < allMessages.length; j++)
                {
                    // Obtain a BufferedReader to read each message
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(allMessages[j])));

                    long authorNumber = Long.parseLong(bufferedReader.readLine());
                    long receiverNumber = Long.parseLong(bufferedReader.readLine());
                    long timestamp = Long.parseLong(bufferedReader.readLine());

                    StringBuilder contentBuilder = new StringBuilder();
                    String line;

                    while((line = bufferedReader.readLine()) != null)
                    {
                        contentBuilder.append(line);
                    }

                    bufferedReader.close();

                    // Recreate the message object and add it to the conversation
                    Message addMessage = new Message(timestamp, authorNumber, receiverNumber, contentBuilder.toString());
                    addConversation.addMessage(addMessage);
                }

                conversations.add(conversation);
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
            for(int i = 0; i < conversations.size(); i++)
            {
                Conversation targetConversation = conversations.get(i);
                Message directoryGeneratorMessage = conversations.get(i);

                StringBuilder directoryBuilder = new StringBuilder();
                directoryBuilder.append("conversations/");
                directoryBuilder.append(directoryGeneratorMessage.getAuthorPhone());
                directoryBuilder.append("_");
                directoryBuilder.append(directoryGeneratorMessage.getReceiverPhone());

                File directoryTestFile = new File(directoryBuilder.toString());

                if(directoryTestFile.exists())
                {
                    saveConversation(directoryBuilder.toString(), targetConversation);
                }

                directoryBuilder = new StringBuilder();

                directoryBuilder.append("conversations/");
                directoryBuilder.append(directoryGeneratorMessage.getReceiverPhone());
                directoryBuilder.append("_");
                directoryBuilder.append(directoryGeneratorMessage.getAuthorPhone());

                directoryTestFile = new File(directoryBuilder.toString());

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
        for(int j = 0; j < conversation.size(); j++)
        {
            Message saveMessage = targetConversation.getMessage(j);

            StringBuilder fileBuilder = new StringBuilder();
            fileBuilder.append(directory);
            fileBuilder.append("/");
            fileBuilder.append(saveMessage.getTimestamp());

            PrintWriter printWriter = new PrintWriter(new File(fileBuilder.toString()));

            printWriter.println(saveMessage.getAuthorNumber());
            printWriter.println(saveMessage.getReceiverNumber());
            printWriter.println(saveMessage.getTimestamp());
            printWriter.println(saveMessage.getContent());

            printWriter.close();
        }
    }

    public void saveContacts(ArrayList<Contact> contacts)
    {
        try
        {
            File contactsFile = new File("contacts/contacts.txt");

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

        }
    }

    public void saveBlacklistNumbers(Blacklist blacklist)
    {
        try
        {
            File blacklistFile = new File("contacts/blacklist.txt");

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

        }
    }

    private FileSystem()
    {
    }
}
