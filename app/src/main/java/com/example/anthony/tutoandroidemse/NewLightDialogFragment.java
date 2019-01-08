package com.example.anthony.tutoandroidemse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class NewLightDialogFragment extends DialogFragment
{
    LayoutInflater inflater;
    ViewGroup viewGroup;
    ContextManagementActivity activity;
    AlertDialog.Builder builder;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        activity = (ContextManagementActivity) (getActivity());
        builder = new AlertDialog.Builder(activity);

        inflater = activity.getLayoutInflater();
        viewGroup = (ViewGroup) inflater.inflate(R.layout.dialog_new_light, null);

        builder.setView(viewGroup);
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dismiss());
        builder.setPositiveButton(R.string.new_light, (dialog, which) ->
        {
            String textRoomId = ((EditText) viewGroup.findViewById(R.id.edit_building_id)).getText().toString();

            if (textRoomId.equals(""))
            {
                Toast.makeText(activity, "Room ID is empty", Toast.LENGTH_SHORT).show();
            }
            else
            {
                int roomId = Integer.parseInt(textRoomId);
                ((ContextManagementActivity) getActivity()).createLight(new LightContextState(-1, 50, roomId, "OFF"));
            }
        });

        return builder.create();
    }
}
