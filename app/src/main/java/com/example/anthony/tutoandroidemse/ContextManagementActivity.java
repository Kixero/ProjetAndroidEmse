package com.example.anthony.tutoandroidemse;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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

        buildingsList = findViewById(R.id.linearLayout);

        refresh();
    }

    void updateBuildings(ArrayList<BuildingContextState> buildings)
    {
        LayoutInflater inflater = getLayoutInflater();
        for (BuildingContextState building : buildings)
        {
            View view = inflater.inflate(R.layout.list_buildings, null);
            ((TextView) view.findViewById(R.id.text)).setText(building.getName());
            buildingsList.addView(view);

            httpManager.getRooms(view.findViewById(R.id.list), building.getId());
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
        expandableView.setAdapter(new RoomsExpandableListAdapter(this, roomsName, lights));
        ((LinearLayout)buildingView).addView(expandableView);
    }

    void switchLight(ImageButton lightView, LightContextState light)
    {
        httpManager.switchLight(lightView, light);
    }

    void setLightLevel(SeekBar view, LightContextState light)
    {
        httpManager.setLightLevel(view, light);
    }

    void updateLight(View view, LightContextState light)
    {
        updateLightState(view.findViewById(R.id.toggle), light);
        updateLightLevel(view.findViewById(R.id.seekBar), light);
    }

    void updateLightState(ImageButton view, LightContextState light)
    {
        view.setBackgroundResource(light.getStatus().equals("ON") ? R.drawable.ic_bulb_on : R.drawable.ic_bulb_off);
    }

    void updateLightLevel(SeekBar view, LightContextState light)
    {
        view.setProgress(light.getLevel());
    }

    void createLight(LightContextState light)
    {
        httpManager.createLight(light);
    }

    void refresh()
    {
        buildingsList.removeAllViewsInLayout();
        httpManager.getBuildings();
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
                refresh();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
