package com.example.anthony.tutoandroidemse;

import android.support.annotation.NonNull;

public class BuildingContextState
{
    private int id;
    private String name;

    BuildingContextState(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId() { return this.id; }

    public String getName() { return this.name; }

    @NonNull
    public String toString() { return this.id + " - " + this.name; }
}
