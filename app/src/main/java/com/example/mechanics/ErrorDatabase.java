package com.example.mechanics;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Error.class}, version = 1, exportSchema = false)
public abstract class ErrorDatabase extends RoomDatabase
{
    public abstract ErrorDao errorDao();

    private static volatile ErrorDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ErrorDatabase getDatabase(final Context context)
    {
        if(INSTANCE == null)
        {
            synchronized (ErrorDatabase.class)
            {
                if(INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ErrorDatabase.class, "error_db")
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                ErrorDao dao = INSTANCE.errorDao();
                dao.deleteAll();
                Context context = GlobalApplication.getAppContext();

                /*Error error = new Error();
                error.setCode("c1");
                error.setDescription("des1");

                Error error2 = new Error();
                error2.setCode("c2");
                error2.setDescription("des2");

                Error error3 = new Error();
                error3.setCode("c3");
                error3.setDescription("des3");

                dao.insert(error);
                dao.insert(error2);
                dao.insert(error3);*/

                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(
                            new InputStreamReader(context.getAssets().open("obd.txt")));

                    String mLine;
                    Error error2 = new Error();
                    while ((mLine = reader.readLine()) != null) {
                        error2.setCode(mLine.substring(0,5));
                        error2.setDescription(mLine.substring(6));
                        dao.insert(error2);
                    }
                } catch (IOException e) {
                    Toast.makeText(context, "Cuś ni tak", Toast.LENGTH_SHORT).show();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            Toast.makeText(context, "Cuś ni tak", Toast.LENGTH_SHORT).show();
                        }
                    }
                }



            });
        }
    };
}
