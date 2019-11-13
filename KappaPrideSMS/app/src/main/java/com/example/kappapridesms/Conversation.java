package com.example.kappapridesms;
import java.util.ArrayList;

public class Conversation {

    //member variables

    private int size;
    ArrayList<Message> messageList = new ArrayList<Message>();

    //methods
    // returns the size of the message list
    public int size(int n)
    {
        return messageList.size();
    }



    // *****************NOT SURE WHAT TO DO HERE****
    public void addMessage(String s, int i)
    {

    }
   //***********************************************




    // removes a message from the list
   public  void deleteMessage(int i)
    {

        messageList.remove(i);

    }

    // Returns a message at the specified index
     public Message getMessage(int index)
     {
         return messageList.get(index);

     }


}
