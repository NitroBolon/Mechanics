package com.example.mechanics;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
interface ErrorDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Error error);

    @Update
    public void update(Error error);

    @Delete
    public void delete(Error error);

    @Query("DELETE FROM error")
    public void deleteAll();

    @Query("SELECT * FROM error ORDER BY code")
    public LiveData<List<Error>> findAll();

    @Query("SELECT * FROM error WHERE code LIKE :code")
    public List<Error> findErrorWithCode(String code);
}
@Entity(tableName = "error")
public class Error {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String code;
    private String description;
    public Error() {}

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id=id;
    }

    public String getCode()
    {
        return code;
    }
    public void setCode(String code)
    {
        this.code=code;
    }

    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
}
