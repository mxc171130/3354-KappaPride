package com.example.kappapridesms;


import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Iterator;
import android.os.Bundle;

public class ConversationActivity extends AppCompatActivity implements View.OnTouchListener, SearchView.OnCloseListener
{
    // TODO must add this activity to the manifest, will add it on a later commit

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_activity);
    }

    // TODO implement functionality
    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    // TODO implement functionality
    /**
     * The user is attempting to close the SearchView.
     *
     * @return true if the listener wants to override the default behavior of clearing the
     * text field and dismissing it, false otherwise.
     */
    @Override
    public boolean onClose() {
        return false;
    }
}