package com.example.indoorlocalization;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.example.indoorlocalization.DaoClass.TestDao;
import com.example.indoorlocalization.DaoClass.TestDao_Impl;
import com.example.indoorlocalization.DaoClass.TrainingDao;
import com.example.indoorlocalization.DaoClass.TrainingDao_Impl;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public final class RoomDatabaseActivity_Impl extends RoomDatabaseActivity {
  private volatile TrainingDao _trainingDao;

  private volatile TestDao _testDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Training` (`ID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `Location` TEXT, `Orientation` TEXT, `RSSI_1` INTEGER NOT NULL, `RSSI_2` INTEGER NOT NULL, `RSSI_3` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Test` (`ID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `Location` TEXT, `Orientation` TEXT, `RSSI_1` INTEGER NOT NULL, `RSSI_2` INTEGER NOT NULL, `RSSI_3` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"156931bb5b5fd1b653556c1224ecae40\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Training`");
        _db.execSQL("DROP TABLE IF EXISTS `Test`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTraining = new HashMap<String, TableInfo.Column>(6);
        _columnsTraining.put("ID", new TableInfo.Column("ID", "INTEGER", true, 1));
        _columnsTraining.put("Location", new TableInfo.Column("Location", "TEXT", false, 0));
        _columnsTraining.put("Orientation", new TableInfo.Column("Orientation", "TEXT", false, 0));
        _columnsTraining.put("RSSI_1", new TableInfo.Column("RSSI_1", "INTEGER", true, 0));
        _columnsTraining.put("RSSI_2", new TableInfo.Column("RSSI_2", "INTEGER", true, 0));
        _columnsTraining.put("RSSI_3", new TableInfo.Column("RSSI_3", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTraining = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTraining = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTraining = new TableInfo("Training", _columnsTraining, _foreignKeysTraining, _indicesTraining);
        final TableInfo _existingTraining = TableInfo.read(_db, "Training");
        if (! _infoTraining.equals(_existingTraining)) {
          throw new IllegalStateException("Migration didn't properly handle Training(com.example.indoorlocalization.EntityClass.TrainingEntity).\n"
                  + " Expected:\n" + _infoTraining + "\n"
                  + " Found:\n" + _existingTraining);
        }
        final HashMap<String, TableInfo.Column> _columnsTest = new HashMap<String, TableInfo.Column>(6);
        _columnsTest.put("ID", new TableInfo.Column("ID", "INTEGER", true, 1));
        _columnsTest.put("Location", new TableInfo.Column("Location", "TEXT", false, 0));
        _columnsTest.put("Orientation", new TableInfo.Column("Orientation", "TEXT", false, 0));
        _columnsTest.put("RSSI_1", new TableInfo.Column("RSSI_1", "INTEGER", true, 0));
        _columnsTest.put("RSSI_2", new TableInfo.Column("RSSI_2", "INTEGER", true, 0));
        _columnsTest.put("RSSI_3", new TableInfo.Column("RSSI_3", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTest = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTest = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTest = new TableInfo("Test", _columnsTest, _foreignKeysTest, _indicesTest);
        final TableInfo _existingTest = TableInfo.read(_db, "Test");
        if (! _infoTest.equals(_existingTest)) {
          throw new IllegalStateException("Migration didn't properly handle Test(com.example.indoorlocalization.EntityClass.TestEntity).\n"
                  + " Expected:\n" + _infoTest + "\n"
                  + " Found:\n" + _existingTest);
        }
      }
    }, "156931bb5b5fd1b653556c1224ecae40", "c49c970e105dcb362d4eefbdcb903d31");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Training","Test");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Training`");
      _db.execSQL("DELETE FROM `Test`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public TrainingDao trainingDao() {
    if (_trainingDao != null) {
      return _trainingDao;
    } else {
      synchronized(this) {
        if(_trainingDao == null) {
          _trainingDao = new TrainingDao_Impl(this);
        }
        return _trainingDao;
      }
    }
  }

  @Override
  public TestDao testDao() {
    if (_testDao != null) {
      return _testDao;
    } else {
      synchronized(this) {
        if(_testDao == null) {
          _testDao = new TestDao_Impl(this);
        }
        return _testDao;
      }
    }
  }
}
