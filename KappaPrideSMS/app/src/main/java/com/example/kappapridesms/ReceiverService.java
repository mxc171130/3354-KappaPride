package com.example.kappapridesms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReceiverService extends Service
{
    public static final String CHANNEL_ID = "9594924902390";
    public static final int NOTIFICATION_ID = 238544524;

    class ReceiverThread implements Runnable
    {
        @Override
        public void run()
        {
            // Check periodically for messages
            while(true)
            {
                try
                {
                    // Attempt to find messages in 10 second intervals
                    Thread.sleep(10000);

                    // Get message inboxes from SMS server
                    InboxSMSResponse inboxes = getInboxes();

                    // Check each inbox for new messages
                    for(InboxSMSResponse.Inbox inbox : inboxes.inboxes)
                    {
                        if(inbox.newMsgs > 0)
                        {
                            // This inbox has new messages, so get them
                            GetSMSResponse messages = getMessages(inbox.id);

                            for(GetSMSResponse.Message message : messages.messages)
                            {
                                if(message.isNew)
                                {
                                    // Notify the user that a new message was sent
                                    // Save the message using fs
                                }
                            }
                        }
                    }
                }
                catch(InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
        }

        private InboxSMSResponse getInboxes()
        {
            try
            {
                // Generate request URL
                StringBuilder urlGenerator = new StringBuilder();
                urlGenerator.append(getString(R.string.api_endpoint));
                urlGenerator.append("get_inboxes/?apikey=");
                urlGenerator.append(getString(R.string.api_key));

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

        private GetSMSResponse getMessages(String inboxId)
        {
            try
            {
                // Generate request URL
                StringBuilder urlGenerator = new StringBuilder();
                urlGenerator.append(getString(R.string.api_endpoint));
                urlGenerator.append("get_messages/?apikey=");
                urlGenerator.append(getString(R.string.api_key));
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

        private String doRESTCall(String url)
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

        private void notifyMessageReceived()
        {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("Message Received")
                    .setContentText("You have received a message!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    public ReceiverService()
    {
    }

    @Override
    public void onCreate()
    {
        createNotificationChannel();

        Thread serviceThread = new Thread(new ReceiverThread());
        serviceThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // Tell Android OS that the service should be restarted after it's been killed
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onDestroy()
    {
    }

    private void createNotificationChannel()
    {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
