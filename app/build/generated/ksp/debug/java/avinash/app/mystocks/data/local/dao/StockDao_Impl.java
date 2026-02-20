package avinash.app.mystocks.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
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
public final class StockDao_Impl implements StockDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StockEntity> __insertionAdapterOfStockEntity;

  private final EntityDeletionOrUpdateAdapter<StockEntity> __updateAdapterOfStockEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStockPrice;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllStocks;

  public StockDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStockEntity = new EntityInsertionAdapter<StockEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `stocks` (`symbol`,`name`,`logoUrl`,`currentPrice`,`previousPrice`,`dayHigh`,`dayLow`,`volume`,`openPrice`,`lastUpdated`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StockEntity entity) {
        statement.bindString(1, entity.getSymbol());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getLogoUrl());
        statement.bindDouble(4, entity.getCurrentPrice());
        statement.bindDouble(5, entity.getPreviousPrice());
        statement.bindDouble(6, entity.getDayHigh());
        statement.bindDouble(7, entity.getDayLow());
        statement.bindLong(8, entity.getVolume());
        statement.bindDouble(9, entity.getOpenPrice());
        statement.bindLong(10, entity.getLastUpdated());
      }
    };
    this.__updateAdapterOfStockEntity = new EntityDeletionOrUpdateAdapter<StockEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `stocks` SET `symbol` = ?,`name` = ?,`logoUrl` = ?,`currentPrice` = ?,`previousPrice` = ?,`dayHigh` = ?,`dayLow` = ?,`volume` = ?,`openPrice` = ?,`lastUpdated` = ? WHERE `symbol` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StockEntity entity) {
        statement.bindString(1, entity.getSymbol());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getLogoUrl());
        statement.bindDouble(4, entity.getCurrentPrice());
        statement.bindDouble(5, entity.getPreviousPrice());
        statement.bindDouble(6, entity.getDayHigh());
        statement.bindDouble(7, entity.getDayLow());
        statement.bindLong(8, entity.getVolume());
        statement.bindDouble(9, entity.getOpenPrice());
        statement.bindLong(10, entity.getLastUpdated());
        statement.bindString(11, entity.getSymbol());
      }
    };
    this.__preparedStmtOfUpdateStockPrice = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE stocks SET currentPrice = ?, previousPrice = currentPrice, dayHigh = CASE WHEN ? > dayHigh THEN ? ELSE dayHigh END, dayLow = CASE WHEN ? < dayLow THEN ? ELSE dayLow END, volume = volume + ?, lastUpdated = ? WHERE symbol = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllStocks = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM stocks";
        return _query;
      }
    };
  }

  @Override
  public Object insertStock(final StockEntity stock, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfStockEntity.insert(stock);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertStocks(final List<StockEntity> stocks,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfStockEntity.insert(stocks);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStock(final StockEntity stock, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfStockEntity.handle(stock);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStockPrice(final String symbol, final double price, final long volumeIncrease,
      final long timestamp, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStockPrice.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, price);
        _argIndex = 2;
        _stmt.bindDouble(_argIndex, price);
        _argIndex = 3;
        _stmt.bindDouble(_argIndex, price);
        _argIndex = 4;
        _stmt.bindDouble(_argIndex, price);
        _argIndex = 5;
        _stmt.bindDouble(_argIndex, price);
        _argIndex = 6;
        _stmt.bindLong(_argIndex, volumeIncrease);
        _argIndex = 7;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 8;
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
          __preparedStmtOfUpdateStockPrice.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllStocks(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllStocks.acquire();
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
          __preparedStmtOfDeleteAllStocks.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<StockEntity>> getAllStocks() {
    final String _sql = "SELECT * FROM stocks ORDER BY symbol ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stocks"}, new Callable<List<StockEntity>>() {
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
  public Object getStock(final String symbol, final Continuation<? super StockEntity> $completion) {
    final String _sql = "SELECT * FROM stocks WHERE symbol = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, symbol);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<StockEntity>() {
      @Override
      @Nullable
      public StockEntity call() throws Exception {
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
          final StockEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new StockEntity(_tmpSymbol,_tmpName,_tmpLogoUrl,_tmpCurrentPrice,_tmpPreviousPrice,_tmpDayHigh,_tmpDayLow,_tmpVolume,_tmpOpenPrice,_tmpLastUpdated);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<StockEntity> getStockFlow(final String symbol) {
    final String _sql = "SELECT * FROM stocks WHERE symbol = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, symbol);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stocks"}, new Callable<StockEntity>() {
      @Override
      @Nullable
      public StockEntity call() throws Exception {
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
          final StockEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new StockEntity(_tmpSymbol,_tmpName,_tmpLogoUrl,_tmpCurrentPrice,_tmpPreviousPrice,_tmpDayHigh,_tmpDayLow,_tmpVolume,_tmpOpenPrice,_tmpLastUpdated);
          } else {
            _result = null;
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
  public Flow<List<StockEntity>> getTopGainers(final int limit) {
    final String _sql = "SELECT * FROM stocks ORDER BY (currentPrice - openPrice) / openPrice * 100 DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stocks"}, new Callable<List<StockEntity>>() {
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
  public Flow<List<StockEntity>> getTopLosers(final int limit) {
    final String _sql = "SELECT * FROM stocks ORDER BY (currentPrice - openPrice) / openPrice * 100 ASC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stocks"}, new Callable<List<StockEntity>>() {
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
  public Flow<List<StockEntity>> searchStocks(final String query) {
    final String _sql = "SELECT * FROM stocks WHERE symbol LIKE '%' || ? || '%' OR name LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stocks"}, new Callable<List<StockEntity>>() {
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
