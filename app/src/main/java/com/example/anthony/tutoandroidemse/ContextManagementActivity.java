package com.example.anthony.tutoandroidemse;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContextManagementActivity extends FragmentActivity
{
    private ContextHttpManager httpManager;

    LinearLayout buildingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_management);

        httpManager = new ContextHttpManager(this);

        buildingsList = findViewById(R.id.parentLayout);

        httpManager.getBuildings();
    }

    void updateBuildings(ArrayList<BuildingContextState> buildings)
    {
        LayoutInflater inflater = getLayoutInflater();
        for (BuildingContextState building : buildings)
        {
            View view = inflater.inflate(R.layout.list_buildings, null);
            ((TextView) view.findViewById(R.id.text)).setText(building.getName());
            buildingsList.addView(view);

            httpManager.getRooms(view, building.getId());
        }
    }

    void updateRooms(View buildingView, ArrayList<RoomContextState> rooms, HashMap<String, List<LightContextState>> lights)
    {
        ArrayList<String> roomsName = new ArrayList<>();
        for (RoomContextState room : rooms)
        {
            roomsName.add(room.getName());
        }
        ExpandableListView expandableView = (ExpandableListView)getLayoutInflater().inflate(R.layout.rooms_expandable, null);
        expandableView.setAdapter(new RoomsExpandableListAdapter<>(this, roomsName, lights));
        ((LinearLayout)buildingView).addView(expandableView);
    }

    void switchLight(LightContextState light)
    {
        httpManager.switchLight(light);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.about :
            {
                Toast.makeText(this, "Made by Anthony MERANGER (Kixero)", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.refresh :
            {
                //refresh();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
