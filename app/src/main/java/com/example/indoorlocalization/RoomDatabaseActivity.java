package com.example.indoorlocalization;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.indoorlocalization.DaoClass.TestDao;
import com.example.indoorlocalization.DaoClass.TrainingDao;
import com.example.indoorlocalization.EntityClass.TestEntity;
import com.example.indoorlocalization.EntityClass.TrainingEntity;

@Database(entities = {TrainingEntity.class, TestEntity.class}, version = 2, exportSchema = false)
public abstract class RoomDatabaseActivity extends RoomDatabase {

    //Create instance of Database
    private static RoomDatabaseActivity database;

    //Define the name of Database
    private static String DATABASE_NAME = "RSSI Data";

    public synchronized static RoomDatabaseActivity getInstance(Context context){

        //Check condition if Database created or not
        if (database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDatabaseActivity.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return database;
    }

    //Create DAO
    public abstract TrainingDao trainingDao();

    public abstract TestDao testDao();


}
