package com.example.kappapridesms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ContactsFragment extends Fragment
{
    private ListView listView;
    private ArrayList<String> contactList;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        listView = (ListView) getView().findViewById(R.id.list_item);
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Add code that will bring up an activity that allows the user to edit in contact information.
            }
        });
        setData();
        adapter = new ContactViewAdapter(this, R.layout.contact_listview, contactList);
        super.onActivityCreated(savedInstanceState);
    }

    // Hardcoded "contacts" to test it.
    private void setData()
    {
        contactList = new ArrayList<>();
        contactList.add("Testing 1");
        contactList.add("Testing 2");
        contactList.add("Testing 3");
        contactList.add("Testing 4");
        contactList.add("Testing 5");
    }
}
