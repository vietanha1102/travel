package com.example.democoordinator.roomdatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.democoordinator.model.Place;

import java.util.List;

@Dao
public interface PlaceDAO {
    @Insert
    void insertPlace(Place place);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertReturnId(Place place);

    @Query("Select * from Place")
    List<Place> getListPlace();

    @Query("Select * from Place where id=:id")
    Place getPlaceById(int id);

    @Query("Update Place set favourite=:favourite where id=:id")
    void updateSttFavorite(boolean favourite, int id);
}
