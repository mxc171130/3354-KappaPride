package com.example.kappapridesms;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>
{
    String[] exSender = {"Mom", "Dad", "Me", "You", "Not Your PE Teacher", "Your PE Teacher"};
    String[] myExBinds = {"Hello", "Goodbye", "To you", "myFriend", "Sample text",
                            "The FitnessGram™ Pacer Test is a multistage aerobic capacity test that progressively gets more difficult as it continues. The 20 meter pacer test will begin in 30 seconds. Line up at the start. The running speed starts slowly, but gets faster each minute after you hear this signal. [beep] A single lap should be completed each time you hear this sound. [ding] Remember to run in a straight line, and run as long as possible. The second time you fail to complete a lap before the sound, your test is over. The test will begin on the word start. On your mark, get ready, start."
                            };
    String[] myDates = {"Oct 31, 2019", "Nov 1, 2019", "Nov 2, 2019", "Nov 3, 2019", "Nov 4, 2019", "Idk, some time ago"};

    String[] colors = {"#EE1C25", "#F78620", "#F3EB26", "#118A43", "#4D52A4", "#7F2886"};

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout messageBubble;

        public MyViewHolder(LinearLayout messageBubble)
        {
            super(messageBubble);
            this.messageBubble = messageBubble;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LinearLayout messageBubble = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.message_bubble, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(messageBubble);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int pos)
    {
        (viewHolder.messageBubble.getChildAt(1)).setBackgroundColor(Color.parseColor(colors[pos % 6]));

        ((TextView) viewHolder.messageBubble.getChildAt(0)).setText(exSender[pos]);
        ((TextView) viewHolder.messageBubble.getChildAt(1)).setText(myExBinds[pos]);
        ((TextView) viewHolder.messageBubble.getChildAt(2)).setText(myDates[pos]);
    }

    @Override
    public int getItemCount()
    {
        return myExBinds.length;
    }
}
