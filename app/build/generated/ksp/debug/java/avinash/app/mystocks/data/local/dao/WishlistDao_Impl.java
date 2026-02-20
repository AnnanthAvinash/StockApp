package avinash.app.mystocks.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import avinash.app.mystocks.data.local.entity.StockEntity;
import avinash.app.mystocks.data.local.entity.WishlistEntity;
import java.lang.Boolean;
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
public final class WishlistDao_Impl implements WishlistDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WishlistEntity> __insertionAdapterOfWishlistEntity;

  private final SharedSQLiteStatement __preparedStmtOfRemoveFromWishlist;

  private final SharedSQLiteStatement __preparedStmtOfClearWishlist;

  public WishlistDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWishlistEntity = new EntityInsertionAdapter<WishlistEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `wishlist` (`symbol`,`addedAt`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WishlistEntity entity) {
        statement.bindString(1, entity.getSymbol());
        statement.bindLong(2, entity.getAddedAt());
      }
    };
    this.__preparedStmtOfRemoveFromWishlist = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM wishlist WHERE symbol = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearWishlist = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM wishlist";
        return _query;
      }
    };
  }

  @Override
  public Object addToWishlist(final WishlistEntity wishlistEntity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfWishlistEntity.insert(wishlistEntity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object removeFromWishlist(final String symbol,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveFromWishlist.acquire();
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
          __preparedStmtOfRemoveFromWishlist.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearWishlist(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearWishlist.acquire();
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
          __preparedStmtOfClearWishlist.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<StockEntity>> getWishlistStocks() {
    final String _sql = "\n"
            + "        SELECT s.* FROM stocks s \n"
            + "        INNER JOIN wishlist w ON s.symbol = w.symbol \n"
            + "        ORDER BY w.addedAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stocks",
        "wishlist"}, new Callable<List<StockEntity>>() {
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
  public Flow<List<WishlistEntity>> getAllWishlistItems() {
    final String _sql = "SELECT * FROM wishlist ORDER BY addedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"wishlist"}, new Callable<List<WishlistEntity>>() {
      @Override
      @NonNull
      public List<WishlistEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfAddedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "addedAt");
          final List<WishlistEntity> _result = new ArrayList<WishlistEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WishlistEntity _item;
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final long _tmpAddedAt;
            _tmpAddedAt = _cursor.getLong(_cursorIndexOfAddedAt);
            _item = new WishlistEntity(_tmpSymbol,_tmpAddedAt);
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
  public Flow<Boolean> isInWishlist(final String symbol) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM wishlist WHERE symbol = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, symbol);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"wishlist"}, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp != 0;
          } else {
            _result = false;
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
  public Object isInWishlistSync(final String symbol,
      final Continuation<? super Boolean> $completion) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM wishlist WHERE symbol = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, symbol);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp != 0;
          } else {
            _result = false;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
