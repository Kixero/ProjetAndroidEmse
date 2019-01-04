package com.example.anthony.tutoandroidemse;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContextManagementActivity extends FragmentActivity
{
    private ContextHttpManager httpManager;

    ExpandableListView buildingsList;
    List<String> buildingsListTitle;
    HashMap<String, List<RoomContextState>> buildingsListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_management);

        httpManager = new ContextHttpManager(this);

        buildingsList = findViewById(R.id.buildingsList);

        buildingsList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()
        {
            @Override
            public void onGroupExpand(int groupPosition)
            {
                Toast.makeText(ContextManagementActivity.this, buildingsListTitle.get(groupPosition), Toast.LENGTH_SHORT).show();
            }
        });

        buildingsList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener()
        {
            @Override
            public void onGroupCollapse(int groupPosition)
            {
                Toast.makeText(ContextManagementActivity.this, buildingsListTitle.get(groupPosition), Toast.LENGTH_SHORT).show();
            }
        });

        buildingsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                Toast.makeText(
                        getApplicationContext(),
                        buildingsListTitle.get(groupPosition)
                                + " -> "
                                + buildingsListDetail.get(
                                buildingsListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });

        httpManager.getBuildingsAndRooms();


        /*
        buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                BuildingContextState state = (BuildingContextState) parent.getSelectedItem();
                findViewById(R.id.roomText).setVisibility(View.VISIBLE);

                httpManager.retrieveBuildingsRooms(state.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                RoomContextState state = (RoomContextState) parent.getSelectedItem();
                findViewById(R.id.lightText).setVisibility(View.VISIBLE);

                httpManager.retrieveRoomsLights(state.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        lightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                lightState = (LightContextState) parent.getSelectedItem();

                toggle.setEnabled(true);

                updateLightState(lightState);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        toggle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switchLight();
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                level.setTextColor(getResources().getColor(R.color.change));
                level.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                setLightLevel(seekbar.getProgress());
            }
        });

        refresh();
        */
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
            case R.id.add :
            {
                DialogFragment dialog = new NewItemDialogFragment();
                dialog.show(getSupportFragmentManager(), "New Item Dialog");
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    void updateBuildings(HashMap<String, List<RoomContextState>> buildingsListDetail)
    {
        this.buildingsListDetail = buildingsListDetail;
        buildingsListTitle = new ArrayList<>(buildingsListDetail.keySet());
        CustomExpandableListAdapter<RoomContextState> expandableListAdapter = new CustomExpandableListAdapter<>(this, buildingsListTitle, buildingsListDetail);

        buildingsList.setAdapter(expandableListAdapter);
    }

    /*
    public void refresh()
    {
        roomSpinner.setVisibility(View.INVISIBLE);
        lightSpinner.setVisibility(View.INVISIBLE);
        httpManager.retrieveAllBuildingsContextState(buildingSpinner);
    }
    */

    public void retrieveAllBuildings()
    {
        //httpManager.retrieveAllBuildingsContextState();
    }

    public void switchLight()
    {
        //httpManager.switchLight();
    }

    //public void setLightLevel(int level) { httpManager.setLightLevel(lightState, level);}

    public void createItem(){}
}
