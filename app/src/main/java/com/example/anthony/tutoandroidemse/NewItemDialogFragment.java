package com.example.anthony.tutoandroidemse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

enum ItemType {
    BUILDING("Building"),
    ROOM("Room"),
    LIGHT("Light");

    String type;

    ItemType(String type)
    {
        this.type = type;
    }

    @NonNull
    public String toString()
    {
        return type;
    }
}

public class NewItemDialogFragment extends DialogFragment
{
    LayoutInflater inflater;
    ViewGroup viewGroup;
    ContextManagementActivity activity;
    AlertDialog.Builder builder;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        activity = (ContextManagementActivity)(getActivity());
        builder = new AlertDialog.Builder(activity);

        inflater = activity.getLayoutInflater();
        viewGroup = (ViewGroup)inflater.inflate(R.layout.dialog_add, null);

        Spinner spinner = viewGroup.findViewById(R.id.item_choice);
        ItemType[] list = {ItemType.BUILDING,
                           ItemType.ROOM,
                           ItemType.LIGHT};
        ArrayAdapter<ItemType> dataAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch ((ItemType)parent.getSelectedItem())
                {
                    case BUILDING:
                    {
                        break;
                    }
                    case ROOM:
                    {
                        TextView text = viewGroup.findViewById(R.id.text_id);
                        text.setText(R.string.room);
                        text.setVisibility(View.VISIBLE);

                        Spinner spinner = viewGroup.findViewById(R.id.spinner);
                        //activity.retrieveAllBuildings(spinner);
                        spinner.setVisibility(View.VISIBLE);

                        viewGroup.findViewById(R.id.text_id).setVisibility(View.VISIBLE);
                        viewGroup.findViewById(R.id.edit_name).setVisibility(View.VISIBLE);
                        viewGroup.findViewById(R.id.edit_level).setVisibility(View.VISIBLE);

                        builder.setPositiveButton(R.string.new_object, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        getDialog().dismiss();
                                    }
                                });
                        break;
                    }
                    case LIGHT:
                    {
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        builder.setView(viewGroup);

        return builder.create();
    }
}
