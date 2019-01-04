package com.example.anthony.tutoandroidemse;

import android.util.SparseArray;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ContextHttpManager
{
    private final String CONTEXT_SERVER_URL = "http://faircorp-anthony-meranger.cleverapps.io/api/";

    private ContextManagementActivity activity;
    private RequestQueue queue;

    ContextHttpManager(ContextManagementActivity activity)
    {
        this.activity = activity;
        queue = Volley.newRequestQueue(activity);
    }

    /*
    void switchLight(final LightContextState state)
    {
        String url = CONTEXT_SERVER_URL + "lights/" + state.getId() + "/switch";

        JsonObjectRequest contextRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    state.setStatus(response.getString("status"));

                    activity.updateLightState(state);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                    }
                });
        queue.add(contextRequest);
    }

    void setLightLevel(final LightContextState state, int level)
    {
        String url = CONTEXT_SERVER_URL + "lights/" + state.getId() + "/level/" + level;

        JsonObjectRequest contextRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    state.setLevel(response.getInt("level"));

                    activity.updateLightState(state);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                    }
                });
        queue.add(contextRequest);
    }
    */

    void getBuildingsAndRooms()
    {
        String url = CONTEXT_SERVER_URL + "buildings";

        JsonArrayRequest contextRequest = new JsonArrayRequest(Request.Method.GET, url, null, response ->
        {
            try
            {
                ArrayList<BuildingContextState> buildings = new ArrayList<>();

                for (int i = 0; i < response.length(); i++)
                {
                    JSONObject json = response.getJSONObject(i);
                    int id = json.getInt("id");
                    String name = json.getString("name");

                    BuildingContextState building = new BuildingContextState(id, name);
                    buildings.add(building);
                }
                retrieveBuildingsRooms(buildings);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        queue.add(contextRequest);
    }

    void retrieveBuildingsRooms(final ArrayList<BuildingContextState> buildings)
    {
        String url = CONTEXT_SERVER_URL + "rooms";

        JsonArrayRequest contextRequest = new JsonArrayRequest(Request.Method.GET, url, null, response ->
        {
            try
            {
                SparseArray<String> buildingsTable = new SparseArray<>();
                HashMap<String, List<RoomContextState>> buildingsRooms = new HashMap<>();

                for (BuildingContextState building : buildings)
                {
                    buildingsRooms.put(building.getName(), new ArrayList<>());
                    buildingsTable.append(building.getId(), building.getName());
                }
                for (int i = 0; i < response.length(); i++)
                {
                    JSONObject json = response.getJSONObject(i);

                    int roomBuildingId = json.getInt("buildingId");

                    if (buildingsTable.get(roomBuildingId) != null)
                    {
                        int id = json.getInt("id");
                        String name = json.getString("name");
                        int level = json.getInt("level");

                        RoomContextState room = new RoomContextState(id, level, name, roomBuildingId);
                        buildingsRooms.get(buildingsTable.valueAt(roomBuildingId)).add(room);
                    }
                }
                activity.updateBuildings(buildingsRooms);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        queue.add(contextRequest);
    }
    /*

    void retrieveRoomsLights(int room)
    {
        String url = CONTEXT_SERVER_URL + "rooms/" + room + "/lights";

        JsonArrayRequest contextRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    ArrayList<LightContextState> states = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject light = response.getJSONObject(i);

                        int roomId = light.getInt("roomId");
                        int id = light.getInt("id");
                        String status = light.getString("status");
                        int level = light.getInt("level");

                        LightContextState state = new LightContextState(id, level, roomId, status);
                        states.add(state);
                    }
                    activity.updateLightsSpinner(states);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                    }
                });
        queue.add(contextRequest);
    }
    */
}