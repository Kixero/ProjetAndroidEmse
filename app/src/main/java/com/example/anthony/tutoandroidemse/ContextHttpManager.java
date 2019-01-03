package com.example.anthony.tutoandroidemse;

import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    void retrieveAllBuildingsContextState(final Spinner spinner)
    {
        String url = CONTEXT_SERVER_URL + "buildings";

        JsonArrayRequest contextRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    ArrayList<BuildingContextState> states = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject building = response.getJSONObject(i);
                        int id = building.getInt("id");
                        String name = building.getString("name");

                        BuildingContextState state = new BuildingContextState(id, name);
                        states.add(state);
                    }
                    activity.updateSpinner(states, spinner);
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

    void retrieveBuildingsRooms(final int building)
    {
        String url = CONTEXT_SERVER_URL + "rooms";

        JsonArrayRequest contextRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    ArrayList<RoomContextState> states = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject room = response.getJSONObject(i);

                        int roomBuildingId = room.getInt("buildingId");

                        if (roomBuildingId == building)
                        {
                            int id = room.getInt("id");
                            String name = room.getString("name");
                            int level = room.getInt("level");

                            RoomContextState state = new RoomContextState(id, level, name, building);
                            states.add(state);
                        }
                    }
                    activity.updateRoomsSpinner(states);
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
}