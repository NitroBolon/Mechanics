package com.example.mechanics;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ErrorRepository
{
    private ErrorDao errorDao;
    private LiveData<List<Error>> errors;

    ErrorRepository(Application app)
    {
        ErrorDatabase db = ErrorDatabase.getDatabase(app);
        errorDao = db.errorDao();
        errors = errorDao.findAll();
    }

    LiveData<List<Error>> findAllErrors()
    {
        return errors;
    }

    void insert(Error error)
    {
        ErrorDatabase.databaseWriteExecutor.execute( () ->{
            errorDao.insert(error);
        });
    }

    void update(Error error)
    {
        ErrorDatabase.databaseWriteExecutor.execute( () ->{
            errorDao.update(error);
        });
    }

    void delete(Error error)
    {
        ErrorDatabase.databaseWriteExecutor.execute( () ->{
            errorDao.delete(error);
        });
    }
}

