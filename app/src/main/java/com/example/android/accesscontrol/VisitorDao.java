package com.example.android.accesscontrol;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VisitorDao {

    @Insert
    void insert(Visitor visitor);

    @Update
    void update(Visitor visitor);

    @Delete
    void delete(Visitor visitor);

    @Query("DELETE FROM visitor_table")
    void deleteAllVisitors();

    @Query("SELECT * FROM visitor_table ORDER BY timeGenerated DESC")
    LiveData<List<Visitor>> getAllVisitors();

}
