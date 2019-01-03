package com.example.anthony.tutoandroidemse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContextManagementActivity extends FragmentActivity
{
    private Spinner buildingSpinner, roomSpinner, lightSpinner;
    private TextView status, level;
    private ImageView bulb;
    private Switch toggle;
    private SeekBar seekbar;

    private LightContextState lightState;

    private ContextHttpManager httpManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_management);

        httpManager = new ContextHttpManager(this);

        buildingSpinner = findViewById(R.id.item_choice);
        roomSpinner = findViewById(R.id.roomSpinner);
        lightSpinner = findViewById(R.id.lightSpinner);

        level = findViewById(R.id.levelValue);
        status = findViewById(R.id.statusValue);

        bulb = findViewById(R.id.bulb);
        toggle = findViewById(R.id.toggle);
        seekbar = findViewById(R.id.seekBar);

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
            case R.id.add :
            {
                DialogFragment dialog = new NewItemDialogFragment();
                dialog.show(getSupportFragmentManager(), "New Item Dialog");
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh()
    {
        roomSpinner.setVisibility(View.INVISIBLE);
        lightSpinner.setVisibility(View.INVISIBLE);
        httpManager.retrieveAllBuildingsContextState(buildingSpinner);
    }

    public void retrieveAllBuildings(Spinner spinner)
    {
        httpManager.retrieveAllBuildingsContextState(spinner);
    }

    public void switchLight()
    {
        httpManager.switchLight(lightState);
    }

    public void setLightLevel(int level) { httpManager.setLightLevel(lightState, level);}

    void updateSpinner(ArrayList<?> states, @NonNull Spinner spinner)
    {
        ArrayAdapter<?> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, states);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setVisibility(View.VISIBLE);
        arrayAdapter.notifyDataSetChanged();
    }

    public void updateBuildingsSpinner(ArrayList<BuildingContextState> states)
    {
        updateSpinner(states, buildingSpinner);
    }

    public void updateRoomsSpinner(ArrayList<RoomContextState> states)
    {
        updateSpinner(states, roomSpinner);
    }

    public void updateLightsSpinner(ArrayList<LightContextState> states)
    {
        updateSpinner(states, lightSpinner);
    }

    public void updateLightState(LightContextState state)
    {
        if (state.getStatus().equals("ON"))
        {
            bulb.setImageResource(R.drawable.ic_bulb_on);
            toggle.setChecked(true);
        }
        else
        {
            bulb.setImageResource(R.drawable.ic_bulb_off);
            toggle.setChecked(false);
        }
        status.setText(state.getStatus());
        seekbar.setProgress(state.getLevel());
        level.setText(Integer.toString(state.getLevel()));
        level.setTextColor(getResources().getColor(R.color.valid));
    }

    public void createItem(){}
}
