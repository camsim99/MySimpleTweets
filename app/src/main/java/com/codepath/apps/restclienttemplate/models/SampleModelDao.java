package com.codepath.apps.restclienttemplate.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SampleModelDao {

    // @Query annotation requires knowing SQL syntax
    // See http://www.sqltutorial.org/

    //query an object byt the unique identifier... :id needs to match id declaration on next line
    @Query("SELECT * FROM SampleModel WHERE id = :id")
    SampleModel byId(long id);

    //pull last 300 entries that were created for this table
    @Query("SELECT * FROM SampleModel ORDER BY ID DESC LIMIT 300")
    List<SampleModel> recentItems();

    //to insert entries into the table
    //onConflict...if i give object that had identier already, update to the one thatI passed in
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(SampleModel... sampleModels);
}
