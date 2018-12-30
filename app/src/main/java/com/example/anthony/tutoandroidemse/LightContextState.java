package com.example.anthony.tutoandroidemse;

import android.support.annotation.NonNull;

public class LightContextState
{
    private int id;
    private int level;
    private int roomId;
    private String status;

    public LightContextState(int id, int level, int roomId, String status)
    {
        this.id = id;
        this.level = level;
        this.roomId = roomId;
        this.status = status;
    }

    public int getId() { return this.id; }

    public int getLevel() { return this.level; }

    public int getRoomId() { return this.roomId; }

    public String getStatus() { return this.status; }

    public void setStatus(String status) { this.status = status; }

    public void setLevel(int level) { this.level = level; }

    @NonNull
    public String toString() { return "Light #" + this.id; }
}


