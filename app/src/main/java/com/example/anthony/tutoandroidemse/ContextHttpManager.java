package com.example.anthony.tutoandroidemse;

import android.util.SparseArray;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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

    void switchLight(final LightContextState state)
    {
        String url = CONTEXT_SERVER_URL + "lights/" + state.getId() + "/switch";

        JsonObjectRequest contextRequest = new JsonObjectRequest(Request.Method.PUT, url, null, response ->
        {
            try
            {
                state.setStatus(response.getString("status"));

                //activity.updateLightState(state);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }, Throwable::printStackTrace);
        queue.add(contextRequest);
    }

    /*
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

    void getBuildings()
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

                    buildings.add(new BuildingContextState(id, name));
                }
                activity.updateBuildings(buildings);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        queue.add(contextRequest);
    }

    void getRooms(final View buildingView, final int buildingId)
    {
        String url = CONTEXT_SERVER_URL + "buildings/" + buildingId + "/rooms";

        JsonArrayRequest contextRequest = new JsonArrayRequest(Request.Method.GET, url, null, response ->
        {
            try
            {
                ArrayList<RoomContextState> rooms = new ArrayList<>();

                for (int i = 0; i < response.length(); i++)
                {
                    JSONObject json = response.getJSONObject(i);

                    int id = json.getInt("id");
                    String name = json.getString("name");
                    int level = json.getInt("level");

                    RoomContextState room = new RoomContextState(id, level, name, buildingId);
                    rooms.add(room);
                }
                getLights(buildingView, rooms);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        queue.add(contextRequest);
    }

    void getLights(final View buildingView, final ArrayList<RoomContextState> rooms)
    {
        String url = CONTEXT_SERVER_URL + "lights";

        JsonArrayRequest contextRequest = new JsonArrayRequest(Request.Method.GET, url, null, response ->
        {
            try
            {
                SparseArray<String> roomsTable = new SparseArray<>();
                HashMap<String, List<LightContextState>> roomsLights = new HashMap<>();

                for (RoomContextState room : rooms)
                {
                    roomsTable.append(room.getId(), room.getName());
                    roomsLights.put(room.getName(), new ArrayList<>());
                }

                for (int i = 0; i < response.length(); i++)
                {
                    JSONObject json = response.getJSONObject(i);

                    int roomId = json.getInt("roomId");
                    int id = json.getInt("id");
                    String status = json.getString("status");
                    int level = json.getInt("level");

                    List<LightContextState> roomsLightsList = roomsLights.get(roomsTable.get(roomId));

                    if (roomsLightsList != null)
                    {
                        roomsLightsList.add(new LightContextState(id, level, roomId, status));
                    }
                }
                activity.updateRooms(buildingView, rooms, roomsLights);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        queue.add(contextRequest);
    }
}