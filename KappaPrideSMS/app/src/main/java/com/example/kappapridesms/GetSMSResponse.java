package com.example.kappapridesms;

public class GetSMSResponse
{
    long inbox_id;
    int num_messages;
    long min_time;
    long max_time;
    String sort_order;
    String sort_field;
    int start;
    int limit;
    Message[] messages;
    String status;

    class Message
    {
        long number;
        String message;
        String date;
        boolean isNew;
        String status;
    }
}
