package com.example.indoorlocalization.DaoClass;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.indoorlocalization.EntityClass.TestEntity;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public final class TestDao_Impl implements TestDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfTestEntity;

  public TestDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTestEntity = new EntityInsertionAdapter<TestEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Test`(`ID`,`Location`,`Orientation`,`RSSI_1`,`RSSI_2`,`RSSI_3`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TestEntity value) {
        stmt.bindLong(1, value.getID());
        if (value.getLocation() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getLocation());
        }
        if (value.getOrientation() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getOrientation());
        }
        stmt.bindLong(4, value.getRssi_1());
        stmt.bindLong(5, value.getRssi_2());
        stmt.bindLong(6, value.getRssi_3());
      }
    };
  }

  @Override
  public void test_insert(TestEntity testEntity) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfTestEntity.insert(testEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<TestEntity> get_testValues() {
    final String _sql = "SELECT * FROM Test";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfID = _cursor.getColumnIndexOrThrow("ID");
      final int _cursorIndexOfLocation = _cursor.getColumnIndexOrThrow("Location");
      final int _cursorIndexOfOrientation = _cursor.getColumnIndexOrThrow("Orientation");
      final int _cursorIndexOfRssi1 = _cursor.getColumnIndexOrThrow("RSSI_1");
      final int _cursorIndexOfRssi2 = _cursor.getColumnIndexOrThrow("RSSI_2");
      final int _cursorIndexOfRssi3 = _cursor.getColumnIndexOrThrow("RSSI_3");
      final List<TestEntity> _result = new ArrayList<TestEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TestEntity _item;
        final String _tmpLocation;
        _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        final String _tmpOrientation;
        _tmpOrientation = _cursor.getString(_cursorIndexOfOrientation);
        final int _tmpRssi_1;
        _tmpRssi_1 = _cursor.getInt(_cursorIndexOfRssi1);
        final int _tmpRssi_2;
        _tmpRssi_2 = _cursor.getInt(_cursorIndexOfRssi2);
        final int _tmpRssi_3;
        _tmpRssi_3 = _cursor.getInt(_cursorIndexOfRssi3);
        _item = new TestEntity(_tmpLocation,_tmpOrientation,_tmpRssi_1,_tmpRssi_2,_tmpRssi_3);
        final int _tmpID;
        _tmpID = _cursor.getInt(_cursorIndexOfID);
        _item.setID(_tmpID);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
