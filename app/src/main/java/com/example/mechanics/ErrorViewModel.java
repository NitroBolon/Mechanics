package com.example.mechanics;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ErrorViewModel extends AndroidViewModel
{
    private ErrorRepository errorRepository;
    private LiveData<List<Error>> errors;

    public ErrorViewModel(@NonNull Application app)
    {
        super(app);
        errorRepository = new ErrorRepository(app);
        errors = errorRepository.findAllErrors();
    }

    public LiveData<List<Error>> findAll()
    {
        return errors;
    }

    public void insert(Error error)
    {
        errorRepository.insert(error);
    }
    public void update(Error error)
    {
        errorRepository.update(error);
    }
    public void delete(Error error)
    {
        errorRepository.delete(error);
    }
}
