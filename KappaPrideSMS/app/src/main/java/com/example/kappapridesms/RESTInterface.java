package com.example.kappapridesms;

import android.content.ContextWrapper;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RESTInterface
{
    public static final String API_ENDPOINT = "https://api.txtlocal.com/";
    public static final String API_KEY = "1cBnzcrfGQw-Gzupbkf0AiOQGVZr4zoxRz38153Q7y";

    public static InboxSMSResponse getInboxes()
    {
        try
        {
            // Generate request URL
            StringBuilder urlGenerator = new StringBuilder();
            urlGenerator.append(API_ENDPOINT);
            urlGenerator.append("get_inboxes/?apikey=");
            urlGenerator.append(API_KEY);

            // Get response from REST call
            String response = doRESTCall(urlGenerator.toString());

            // Return GSON-translated response
            InboxSMSResponse inboxSMSResponse = new Gson().fromJson(response.toString(), InboxSMSResponse.class);
            return inboxSMSResponse;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        // Error somewhere in try block, return null response.
        return null;
    }

    public static GetSMSResponse getMessages(String inboxId)
    {
        try
        {
            // Generate request URL
            StringBuilder urlGenerator = new StringBuilder();
            urlGenerator.append(API_ENDPOINT);
            urlGenerator.append("get_messages/?apikey=");
            urlGenerator.append(API_KEY);
            urlGenerator.append("&inbox_id=");
            urlGenerator.append(inboxId);

            // Get response from REST call
            String response = doRESTCall(urlGenerator.toString());

            // Return GSON-translated response
            GetSMSResponse getSMSResponse = new Gson().fromJson(response, GetSMSResponse.class);
            return getSMSResponse;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }


    public static boolean sendMessage(String message, long toNumber)
    {
        try
        {
            // Generate request URL
            StringBuilder urlGenerator = new StringBuilder();
            urlGenerator.append(API_ENDPOINT);
            urlGenerator.append("send/?apikey=");
            urlGenerator.append(API_KEY);
            urlGenerator.append("&message=");
            urlGenerator.append(message);
            urlGenerator.append("&numbers=");
            urlGenerator.append(toNumber);

            // Get response from REST call
            String response = doRESTCall(urlGenerator.toString());

            SendSMSResponse sendSMSResponse = new Gson().fromJson(response, SendSMSResponse.class);
        }
        catch(Exception ex)
        {
            return false;
        }

        return true;
    }


    public static String doRESTCall(String url)
    {
        try
        {
            // Create URL
            URL resourceLocation = new URL(url.toString());

            // Establish HTTP connection and set properties
            HttpURLConnection connection = (HttpURLConnection) resourceLocation.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            // Retrieve JSON response
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();

            String line;

            while ((line = rd.readLine()) != null)
            {
                responseBuilder.append(line);
            }

            // Close streams
            rd.close();
            connection.disconnect();

            return responseBuilder.toString();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }
}
