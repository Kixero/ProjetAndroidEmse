package com.example.anthony.tutoandroidemse;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomsExpandableListAdapter<T> extends BaseExpandableListAdapter
{
    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<T>> expandableListDetail;

    RoomsExpandableListAdapter(Context context, List<String> expandableListTitle, HashMap<String, List<T>> expandableListDetail)
    {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition)
    {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition)
    {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        final String expandedListText = getChild(listPosition, expandedListPosition).toString();

        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_lights, null);
        }
        TextView textView = convertView.findViewById(R.id.light_item);
        textView.setText(expandedListText);

        ToggleButton toggle = convertView.findViewById(R.id.toggle);
        toggle.setOnClickListener(v ->
        {
            //((ContextManagementActivity)context).switchLight(light);
        });

        return convertView;
    }

    public int getChildrenCount(int listPosition)
    {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition)
    {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount()
    {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition)
    {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        final String roomTitle = getGroup(listPosition).toString();

        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_rooms, null);
        }

        TextView expandedRoomListView = convertView.findViewById(R.id.room_item);
        expandedRoomListView.setText(roomTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition)
    {
        return true;
    }
}
