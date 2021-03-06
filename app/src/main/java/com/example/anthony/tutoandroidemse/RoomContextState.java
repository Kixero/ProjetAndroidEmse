package com.example.anthony.tutoandroidemse;


import android.support.annotation.NonNull;

public class RoomContextState
{
    private int id;
    private int level;
    private String name;
    private int buildingId;

    RoomContextState(int id, int level, String name, int buildingId)
    {
        this.id = id;
        this.level = level;
        this.name = name;
        this.buildingId = buildingId;
    }

    public int getId() { return this.id; }

    public String getName() { return this.name; }

    public int getBuildingId() { return this.buildingId; }

    public int getLevel() { return this.level; }

    @NonNull
    public String toString() { return this.id + " - " + this.name; }
}