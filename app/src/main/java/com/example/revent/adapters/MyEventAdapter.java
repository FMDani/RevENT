package com.example.revent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.revent.R;
import com.example.revent.models.MyEvent;

import java.util.List;

public class MyEventAdapter extends ArrayAdapter<MyEvent>
{

    public MyEventAdapter(Context context, int resource, List<MyEvent> shapeList)
    {
        super(context,resource,shapeList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MyEvent event = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);
        }
        TextView eventName = (TextView) convertView.findViewById(R.id.eventName_cell);
        TextView location = (TextView) convertView.findViewById(R.id.placeName_cell);


        eventName.setText(event.getTitle());
        location.setText(event.getPlace());


        return convertView;
    }
}