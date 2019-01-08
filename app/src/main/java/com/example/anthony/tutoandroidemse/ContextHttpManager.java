package com.example.anthony.tutoandroidemse;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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

    void switchLight(final ImageButton lightView, final LightContextState state)
    {
        String url = CONTEXT_SERVER_URL + "lights/" + state.getId() + "/switch";

        JsonObjectRequest contextRequest = new JsonObjectRequest(Request.Method.PUT, url, null, response ->
        {
            try
            {
                state.setStatus(response.getString("status"));

                activity.updateLightState(lightView, state);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }, Throwable::printStackTrace);
        queue.add(contextRequest);
    }

    void setLightLevel(final SeekBar view, final LightContextState state)
    {
        String url = CONTEXT_SERVER_URL + "lights/" + state.getId() + "/level/" + state.getLevel();

        JsonObjectRequest contextRequest = new JsonObjectRequest(Request.Method.PUT, url, null, response ->
        {
            try
            {
                state.setLevel(response.getInt("level"));

                activity.updateLightLevel(view, state);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }, Throwable::printStackTrace);
        queue.add(contextRequest);
    }

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

    void createLight(LightContextState light)
    {
        String url = CONTEXT_SERVER_URL + "lights";

        JSONObject jsonRequest = new JSONObject();
        try
        {
            jsonRequest.put("id", light.getId());
            jsonRequest.put("level", light.getLevel());
            jsonRequest.put("roomId", light.getRoomId());
            jsonRequest.put("status", light.getStatus());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        final String requestString = jsonRequest.toString();

        StringRequest contextRequest = new StringRequest(Request.Method.POST, url, response -> activity.refresh(), Throwable::printStackTrace)
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError
            {
                try {
                    return requestString == null ? null : requestString.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestString, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        queue.add(contextRequest);
    }

    void createRoom(RoomContextState room)
    {
        String url = CONTEXT_SERVER_URL + "rooms";

        JSONObject jsonRequest = new JSONObject();
        try
        {
            jsonRequest.put("id", room.getId());
            jsonRequest.put("name", room.getName());
            jsonRequest.put("level", room.getLevel());
            jsonRequest.put("buildingId", room.getBuildingId());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        final String requestString = jsonRequest.toString();

        StringRequest contextRequest = new StringRequest(Request.Method.POST, url, response -> activity.refresh(), Throwable::printStackTrace)
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError
            {
                try {
                    return requestString == null ? null : requestString.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestString, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        queue.add(contextRequest);
    }
}