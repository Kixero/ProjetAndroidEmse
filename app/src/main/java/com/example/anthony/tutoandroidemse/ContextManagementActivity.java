package com.example.anthony.tutoandroidemse;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ActionProvider;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContextManagementActivity extends Activity
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

        buildingSpinner = findViewById(R.id.buildingSpinner);
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

        httpManager.retrieveAllBuildingsContextState();
    }

    public void switchLight()
    {
        httpManager.switchLight(lightState);
    }

    public void setLightLevel(int level) { httpManager.setLightLevel(lightState, level);}

    private void updateSpinner(ArrayList<?> states, @NonNull Spinner spinner)
    {
        ArrayAdapter<?> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, states);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    public void updateBuildingSpinner(ArrayList<BuildingContextState> states)
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
}
