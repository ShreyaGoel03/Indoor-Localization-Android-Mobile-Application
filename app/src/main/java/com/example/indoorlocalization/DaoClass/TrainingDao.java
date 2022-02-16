package com.example.indoorlocalization.DaoClass;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.indoorlocalization.EntityClass.TrainingEntity;
import static androidx.room.OnConflictStrategy.REPLACE;

import java.util.List;

@Dao
public interface TrainingDao {

    @Insert(onConflict = REPLACE)
    public void train_insert(TrainingEntity trainingEntity);

    @Query("SELECT * FROM Training")
    List<TrainingEntity> getValues();
}
