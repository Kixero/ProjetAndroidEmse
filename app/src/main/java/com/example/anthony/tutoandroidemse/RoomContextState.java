package com.example.anthony.tutoandroidemse;

public class RoomContextState
{
    private String room;
    private String status;
    private int light;

    public RoomContextState(String room, String status, int light)
    {
        super();
        this.room = room;
        this.status = status;
        this.light = light;
    }

    public String getRoom()
    {
        return this.room;
    }

    public String getStatus()
    {
        return this.status;
    }

    public int getLight()
    {
        return this.light;
    }
}