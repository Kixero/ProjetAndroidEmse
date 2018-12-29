 package com.example.anthony.tutoandroidemse;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

 public class ContextManagementActivity extends Activity
 {
     private String room;
     private RoomContextHttpManager httpManager = new RoomContextHttpManager();
     private RelativeLayout contextView;
     private RoomContextState state;
     private ImageView image;

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_context_management);

         contextView = findViewById(R.id.contextLayout);
         image = findViewById(R.id.imageView1);

         findViewById(R.id.buttonCheck).setOnClickListener(new View.OnClickListener()
         {
             public void onClick(View v)
             {
                 room = ((EditText) findViewById(R.id.editText1)).getText().toString();
                 retrieveRoomContextState(room);
             }
         });

         findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (room != null) {
                     switchLight();
                     retrieveRoomContextState(room);
                 }
                 else
                     Toast.makeText(ContextManagementActivity.this, "No room selected !", Toast.LENGTH_SHORT).show();
             }
         });
     }


     @Override
     public boolean onCreateOptionsMenu(Menu menu)
     {
         getMenuInflater().inflate(R.menu.main, menu);
         return true;
     }

     public void switchLight()
     {
         httpManager.switchLight(room);
     }

     protected void retrieveRoomContextState(String room)
     {
        httpManager.retrieveRoomContextState(room);
     }

     private void updateContextView() {
         if (this.state != null) {
             contextView.setVisibility(View.VISIBLE);
             ((TextView) findViewById(R.id.textViewLightValue)).setText(Integer.toString(state.getLight()));
             if (state.getStatus().equals("ON"))
                 image.setImageResource(R.drawable.ic_bulb_on);
             else
                 image.setImageResource(R.drawable.ic_bulb_off);
         }
     }

     class RoomContextHttpManager
     {
         private final String CONTEXT_SERVER_URL = "http://faircorp-anthony-meranger.cleverapps.io/api/rooms";

         void switchLight(String room)
         {
             String url = CONTEXT_SERVER_URL + "/" + room + "/switchLight";

             RequestQueue queue = Volley.newRequestQueue(ContextManagementActivity.this);

             JsonObjectRequest contextRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>()
             {
                 @Override
                 public void onResponse(JSONObject response)
                 {
                     try
                     {
                         String id = response.getString("id");
                         int lightLevel = Integer.parseInt(response.getJSONObject("light").get("level").toString());
                         String lightStatus = response.getJSONObject("light").get("status").toString();

                         RoomContextState context = new RoomContextState(id, lightStatus, lightLevel);
                         onUpdate(context);
                     }
                     catch (JSONException e)
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

         void retrieveRoomContextState(String room)
         {
             String url = CONTEXT_SERVER_URL + "/" + room + "/";

             RequestQueue queue = Volley.newRequestQueue(ContextManagementActivity.this);

             JsonObjectRequest contextRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
             {
                 @Override
                 public void onResponse(JSONObject response)
                 {
                     try
                     {
                         String id = response.getString("id");
                         int lightLevel = Integer.parseInt(response.getJSONObject("light").get("level").toString());
                         String lightStatus = response.getJSONObject("light").get("status").toString();

                         RoomContextState context = new RoomContextState(id, lightStatus, lightLevel);
                         onUpdate(context);
                     }
                     catch (JSONException e)
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

         void onUpdate(RoomContextState context)
         {
             state = context;
             ContextManagementActivity.this.updateContextView();
         }
     }
}
