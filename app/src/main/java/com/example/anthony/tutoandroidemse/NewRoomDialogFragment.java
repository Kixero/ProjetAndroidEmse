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

public class NewRoomDialogFragment extends DialogFragment
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
        viewGroup = (ViewGroup) inflater.inflate(R.layout.dialog_new_room, null);

        builder.setView(viewGroup);
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dismiss());
        builder.setPositiveButton(R.string.new_room, (dialog, which) ->
        {
            String textBuildingId = ((EditText) viewGroup.findViewById(R.id.edit_building_id)).getText().toString();
            String textName = ((EditText) viewGroup.findViewById(R.id.edit_room_name)).getText().toString();
            String textLevel = ((EditText) viewGroup.findViewById(R.id.edit_room_level)).getText().toString();

            if (textBuildingId.equals(""))
            {
                Toast.makeText(activity, "Building ID is empty", Toast.LENGTH_SHORT).show();
            }
            else if (textName.equals(""))
            {
                Toast.makeText(activity, "Name is empty", Toast.LENGTH_SHORT).show();
            }
            else if (textLevel.equals(""))
            {
                Toast.makeText(activity, "Level is empty", Toast.LENGTH_SHORT).show();
            }
            else
            {
                int buildingId = Integer.parseInt(textBuildingId);
                int level = Integer.parseInt(textLevel);
                ((ContextManagementActivity) getActivity()).createRoom(new RoomContextState(-1, level, textName, buildingId));
            }
        });

        return builder.create();
    }
}
