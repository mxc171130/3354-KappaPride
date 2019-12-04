package com.example.kappapridesms;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class ContactViewAdapter extends ArrayAdapter<String>
{
    private ContactsFragment fragment;
    private List<String> contactsList;

    public ContactViewAdapter(ContactsFragment context, int resource, List<String> objects)
    {
        super(context.getActivity(), resource, objects);

    }

    @Override
    public int getCount()
    {
        return contactsList.size();
    }

    @Override
    public String getItem(int position)
    {
        return contactsList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) fragment.getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.conversation_view, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            } else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.contactName.setText(getItem(position));

        String firstLetter = String.valueOf(getItem(position).charAt(0));

        /*
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(getItem(position));

        TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);
         */

        return convertView;
    }

    private class ViewHolder
    {
        private ImageView imageView;
        private TextView contactName;

        public ViewHolder(View v)
        {
            imageView = (ImageView) v.findViewById(R.id.image_view);
            contactName = (TextView) v.findViewById(R.id.contact_name);
        }
    }
}
