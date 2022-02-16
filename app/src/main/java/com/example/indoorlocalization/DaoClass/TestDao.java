package com.example.indoorlocalization.DaoClass;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.indoorlocalization.EntityClass.TestEntity;
import static androidx.room.OnConflictStrategy.REPLACE;

import java.util.List;

@Dao
public interface TestDao {

    @Insert(onConflict = REPLACE)
    public void test_insert(TestEntity testEntity);

    @Query("SELECT * FROM Test")
    List<TestEntity> get_testValues();
}
