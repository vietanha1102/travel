package com.example.democoordinator.roomdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.democoordinator.model.Place;

@Database(entities = {Place.class}, version = 1)
public abstract class PlaceDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "place.db";

    private static PlaceDatabase instance;

    public static synchronized PlaceDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), PlaceDatabase.class, DATABASE_NAME).build();
        }
        return instance;
    }

    public abstract PlaceDAO placeDAO();

}
