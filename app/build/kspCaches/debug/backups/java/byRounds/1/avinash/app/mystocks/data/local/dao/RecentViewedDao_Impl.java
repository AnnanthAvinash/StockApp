package avinash.app.mystocks.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import avinash.app.mystocks.data.local.entity.RecentViewedEntity;
import avinash.app.mystocks.data.local.entity.StockEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RecentViewedDao_Impl implements RecentViewedDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RecentViewedEntity> __insertionAdapterOfRecentViewedEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteRecentViewed;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllRecentViewed;

  private final SharedSQLiteStatement __preparedStmtOfKeepOnlyRecent;

  public RecentViewedDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecentViewedEntity = new EntityInsertionAdapter<RecentViewedEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `recent_viewed` (`symbol`,`name`,`logoUrl`,`viewedAt`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecentViewedEntity entity) {
        statement.bindString(1, entity.getSymbol());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getLogoUrl());
        statement.bindLong(4, entity.getViewedAt());
      }
    };
    this.__preparedStmtOfDeleteRecentViewed = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM recent_viewed WHERE symbol = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllRecentViewed = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM recent_viewed";
        return _query;
      }
    };
    this.__preparedStmtOfKeepOnlyRecent = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        DELETE FROM recent_viewed \n"
                + "        WHERE symbol NOT IN (\n"
                + "            SELECT symbol FROM recent_viewed \n"
                + "            ORDER BY viewedAt DESC \n"
                + "            LIMIT ?\n"
                + "        )\n"
                + "    ";
        return _query;
      }
    };
  }

  @Override
  public Object insertRecentViewed(final RecentViewedEntity recentViewed,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRecentViewedEntity.insert(recentViewed);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRecentViewed(final String symbol,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteRecentViewed.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, symbol);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteRecentViewed.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllRecentViewed(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllRecentViewed.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllRecentViewed.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object keepOnlyRecent(final int keepCount, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfKeepOnlyRecent.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, keepCount);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfKeepOnlyRecent.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<StockEntity>> getRecentViewedStocks(final int limit) {
    final String _sql = "\n"
            + "        SELECT s.* FROM stocks s \n"
            + "        INNER JOIN recent_viewed r ON s.symbol = r.symbol \n"
            + "        ORDER BY r.viewedAt DESC \n"
            + "        LIMIT ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stocks",
        "recent_viewed"}, new Callable<List<StockEntity>>() {
      @Override
      @NonNull
      public List<StockEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLogoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "logoUrl");
          final int _cursorIndexOfCurrentPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPrice");
          final int _cursorIndexOfPreviousPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "previousPrice");
          final int _cursorIndexOfDayHigh = CursorUtil.getColumnIndexOrThrow(_cursor, "dayHigh");
          final int _cursorIndexOfDayLow = CursorUtil.getColumnIndexOrThrow(_cursor, "dayLow");
          final int _cursorIndexOfVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "volume");
          final int _cursorIndexOfOpenPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "openPrice");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final List<StockEntity> _result = new ArrayList<StockEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StockEntity _item;
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpLogoUrl;
            _tmpLogoUrl = _cursor.getString(_cursorIndexOfLogoUrl);
            final double _tmpCurrentPrice;
            _tmpCurrentPrice = _cursor.getDouble(_cursorIndexOfCurrentPrice);
            final double _tmpPreviousPrice;
            _tmpPreviousPrice = _cursor.getDouble(_cursorIndexOfPreviousPrice);
            final double _tmpDayHigh;
            _tmpDayHigh = _cursor.getDouble(_cursorIndexOfDayHigh);
            final double _tmpDayLow;
            _tmpDayLow = _cursor.getDouble(_cursorIndexOfDayLow);
            final long _tmpVolume;
            _tmpVolume = _cursor.getLong(_cursorIndexOfVolume);
            final double _tmpOpenPrice;
            _tmpOpenPrice = _cursor.getDouble(_cursorIndexOfOpenPrice);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _item = new StockEntity(_tmpSymbol,_tmpName,_tmpLogoUrl,_tmpCurrentPrice,_tmpPreviousPrice,_tmpDayHigh,_tmpDayLow,_tmpVolume,_tmpOpenPrice,_tmpLastUpdated);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<RecentViewedEntity>> getRecentViewed(final int limit) {
    final String _sql = "SELECT * FROM recent_viewed ORDER BY viewedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"recent_viewed"}, new Callable<List<RecentViewedEntity>>() {
      @Override
      @NonNull
      public List<RecentViewedEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLogoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "logoUrl");
          final int _cursorIndexOfViewedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "viewedAt");
          final List<RecentViewedEntity> _result = new ArrayList<RecentViewedEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RecentViewedEntity _item;
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpLogoUrl;
            _tmpLogoUrl = _cursor.getString(_cursorIndexOfLogoUrl);
            final long _tmpViewedAt;
            _tmpViewedAt = _cursor.getLong(_cursorIndexOfViewedAt);
            _item = new RecentViewedEntity(_tmpSymbol,_tmpName,_tmpLogoUrl,_tmpViewedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<RecentViewedEntity>> getAllRecentViewed() {
    final String _sql = "SELECT * FROM recent_viewed ORDER BY viewedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"recent_viewed"}, new Callable<List<RecentViewedEntity>>() {
      @Override
      @NonNull
      public List<RecentViewedEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLogoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "logoUrl");
          final int _cursorIndexOfViewedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "viewedAt");
          final List<RecentViewedEntity> _result = new ArrayList<RecentViewedEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RecentViewedEntity _item;
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpLogoUrl;
            _tmpLogoUrl = _cursor.getString(_cursorIndexOfLogoUrl);
            final long _tmpViewedAt;
            _tmpViewedAt = _cursor.getLong(_cursorIndexOfViewedAt);
            _item = new RecentViewedEntity(_tmpSymbol,_tmpName,_tmpLogoUrl,_tmpViewedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
