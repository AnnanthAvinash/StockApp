package avinash.app.mystocks.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.AmbiguousColumnResolver;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import avinash.app.mystocks.data.local.entity.PortfolioEntity;
import avinash.app.mystocks.data.local.entity.StockEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PortfolioDao_Impl implements PortfolioDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PortfolioEntity> __insertionAdapterOfPortfolioEntity;

  private final EntityDeletionOrUpdateAdapter<PortfolioEntity> __updateAdapterOfPortfolioEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteHolding;

  private final SharedSQLiteStatement __preparedStmtOfClearPortfolio;

  public PortfolioDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPortfolioEntity = new EntityInsertionAdapter<PortfolioEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `portfolio` (`symbol`,`quantity`,`averagePrice`,`totalInvested`,`lastUpdated`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PortfolioEntity entity) {
        statement.bindString(1, entity.getSymbol());
        statement.bindLong(2, entity.getQuantity());
        statement.bindDouble(3, entity.getAveragePrice());
        statement.bindDouble(4, entity.getTotalInvested());
        statement.bindLong(5, entity.getLastUpdated());
      }
    };
    this.__updateAdapterOfPortfolioEntity = new EntityDeletionOrUpdateAdapter<PortfolioEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `portfolio` SET `symbol` = ?,`quantity` = ?,`averagePrice` = ?,`totalInvested` = ?,`lastUpdated` = ? WHERE `symbol` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PortfolioEntity entity) {
        statement.bindString(1, entity.getSymbol());
        statement.bindLong(2, entity.getQuantity());
        statement.bindDouble(3, entity.getAveragePrice());
        statement.bindDouble(4, entity.getTotalInvested());
        statement.bindLong(5, entity.getLastUpdated());
        statement.bindString(6, entity.getSymbol());
      }
    };
    this.__preparedStmtOfDeleteHolding = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM portfolio WHERE symbol = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearPortfolio = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM portfolio";
        return _query;
      }
    };
  }

  @Override
  public Object insertHolding(final PortfolioEntity holding,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPortfolioEntity.insert(holding);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertHoldings(final List<PortfolioEntity> holdings,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPortfolioEntity.insert(holdings);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateHolding(final PortfolioEntity holding,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPortfolioEntity.handle(holding);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteHolding(final String symbol, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteHolding.acquire();
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
          __preparedStmtOfDeleteHolding.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearPortfolio(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearPortfolio.acquire();
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
          __preparedStmtOfClearPortfolio.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PortfolioEntity>> getAllHoldings() {
    final String _sql = "SELECT * FROM portfolio ORDER BY symbol ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"portfolio"}, new Callable<List<PortfolioEntity>>() {
      @Override
      @NonNull
      public List<PortfolioEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfAveragePrice = CursorUtil.getColumnIndexOrThrow(_cursor, "averagePrice");
          final int _cursorIndexOfTotalInvested = CursorUtil.getColumnIndexOrThrow(_cursor, "totalInvested");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final List<PortfolioEntity> _result = new ArrayList<PortfolioEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PortfolioEntity _item;
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final double _tmpAveragePrice;
            _tmpAveragePrice = _cursor.getDouble(_cursorIndexOfAveragePrice);
            final double _tmpTotalInvested;
            _tmpTotalInvested = _cursor.getDouble(_cursorIndexOfTotalInvested);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _item = new PortfolioEntity(_tmpSymbol,_tmpQuantity,_tmpAveragePrice,_tmpTotalInvested,_tmpLastUpdated);
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
  public Object getHolding(final String symbol,
      final Continuation<? super PortfolioEntity> $completion) {
    final String _sql = "SELECT * FROM portfolio WHERE symbol = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, symbol);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PortfolioEntity>() {
      @Override
      @Nullable
      public PortfolioEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfAveragePrice = CursorUtil.getColumnIndexOrThrow(_cursor, "averagePrice");
          final int _cursorIndexOfTotalInvested = CursorUtil.getColumnIndexOrThrow(_cursor, "totalInvested");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final PortfolioEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final double _tmpAveragePrice;
            _tmpAveragePrice = _cursor.getDouble(_cursorIndexOfAveragePrice);
            final double _tmpTotalInvested;
            _tmpTotalInvested = _cursor.getDouble(_cursorIndexOfTotalInvested);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _result = new PortfolioEntity(_tmpSymbol,_tmpQuantity,_tmpAveragePrice,_tmpTotalInvested,_tmpLastUpdated);
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
  public Flow<PortfolioEntity> getHoldingFlow(final String symbol) {
    final String _sql = "SELECT * FROM portfolio WHERE symbol = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, symbol);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"portfolio"}, new Callable<PortfolioEntity>() {
      @Override
      @Nullable
      public PortfolioEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfAveragePrice = CursorUtil.getColumnIndexOrThrow(_cursor, "averagePrice");
          final int _cursorIndexOfTotalInvested = CursorUtil.getColumnIndexOrThrow(_cursor, "totalInvested");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final PortfolioEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final double _tmpAveragePrice;
            _tmpAveragePrice = _cursor.getDouble(_cursorIndexOfAveragePrice);
            final double _tmpTotalInvested;
            _tmpTotalInvested = _cursor.getDouble(_cursorIndexOfTotalInvested);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _result = new PortfolioEntity(_tmpSymbol,_tmpQuantity,_tmpAveragePrice,_tmpTotalInvested,_tmpLastUpdated);
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
  public Flow<Map<PortfolioEntity, StockEntity>> getHoldingsWithStocks() {
    final String _sql = "\n"
            + "        SELECT p.*, s.* FROM portfolio p\n"
            + "        LEFT JOIN stocks s ON p.symbol = s.symbol\n"
            + "        ORDER BY p.symbol ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"portfolio",
        "stocks"}, new Callable<Map<PortfolioEntity, StockEntity>>() {
      @Override
      @NonNull
      public Map<PortfolioEntity, StockEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int[][] _cursorIndices = AmbiguousColumnResolver.resolve(_cursor.getColumnNames(), new String[][] {{"symbol",
                "quantity", "averagePrice", "totalInvested", "lastUpdated"}, {"symbol",
                "lastUpdated", "name", "logoUrl", "currentPrice", "previousPrice", "dayHigh",
                "dayLow", "volume", "openPrice"}});
            final Map<PortfolioEntity, StockEntity> _result = new LinkedHashMap<PortfolioEntity, StockEntity>();
            while (_cursor.moveToNext()) {
              final PortfolioEntity _key;
              final String _tmpSymbol;
              _tmpSymbol = _cursor.getString(_cursorIndices[0][0]);
              final int _tmpQuantity;
              _tmpQuantity = _cursor.getInt(_cursorIndices[0][1]);
              final double _tmpAveragePrice;
              _tmpAveragePrice = _cursor.getDouble(_cursorIndices[0][2]);
              final double _tmpTotalInvested;
              _tmpTotalInvested = _cursor.getDouble(_cursorIndices[0][3]);
              final long _tmpLastUpdated;
              _tmpLastUpdated = _cursor.getLong(_cursorIndices[0][4]);
              _key = new PortfolioEntity(_tmpSymbol,_tmpQuantity,_tmpAveragePrice,_tmpTotalInvested,_tmpLastUpdated);
              if (_cursor.isNull(_cursorIndices[1][0]) && _cursor.isNull(_cursorIndices[1][1]) &&
                  _cursor.isNull(_cursorIndices[1][2]) && _cursor.isNull(_cursorIndices[1][3]) &&
                  _cursor.isNull(_cursorIndices[1][4]) && _cursor.isNull(_cursorIndices[1][5]) &&
                  _cursor.isNull(_cursorIndices[1][6]) && _cursor.isNull(_cursorIndices[1][7]) &&
                  _cursor.isNull(_cursorIndices[1][8]) && _cursor.isNull(_cursorIndices[1][9])) {
                _result.put(_key, null);
                continue;
              }
              final StockEntity _value;
              final String _tmpSymbol_1;
              _tmpSymbol_1 = _cursor.getString(_cursorIndices[1][0]);
              final long _tmpLastUpdated_1;
              _tmpLastUpdated_1 = _cursor.getLong(_cursorIndices[1][1]);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndices[1][2]);
              final String _tmpLogoUrl;
              _tmpLogoUrl = _cursor.getString(_cursorIndices[1][3]);
              final double _tmpCurrentPrice;
              _tmpCurrentPrice = _cursor.getDouble(_cursorIndices[1][4]);
              final double _tmpPreviousPrice;
              _tmpPreviousPrice = _cursor.getDouble(_cursorIndices[1][5]);
              final double _tmpDayHigh;
              _tmpDayHigh = _cursor.getDouble(_cursorIndices[1][6]);
              final double _tmpDayLow;
              _tmpDayLow = _cursor.getDouble(_cursorIndices[1][7]);
              final long _tmpVolume;
              _tmpVolume = _cursor.getLong(_cursorIndices[1][8]);
              final double _tmpOpenPrice;
              _tmpOpenPrice = _cursor.getDouble(_cursorIndices[1][9]);
              _value = new StockEntity(_tmpSymbol_1,_tmpName,_tmpLogoUrl,_tmpCurrentPrice,_tmpPreviousPrice,_tmpDayHigh,_tmpDayLow,_tmpVolume,_tmpOpenPrice,_tmpLastUpdated_1);
              if (!_result.containsKey(_key)) {
                _result.put(_key, _value);
              }
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Double> getTotalInvested() {
    final String _sql = "SELECT SUM(totalInvested) FROM portfolio";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"portfolio"}, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
