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
import avinash.app.mystocks.data.local.entity.PendingOrderEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class PendingOrderDao_Impl implements PendingOrderDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PendingOrderEntity> __insertionAdapterOfPendingOrderEntity;

  private final EntityDeletionOrUpdateAdapter<PendingOrderEntity> __updateAdapterOfPendingOrderEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateOrderStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOrder;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCompletedOrders;

  private final SharedSQLiteStatement __preparedStmtOfClearAllOrders;

  private final SharedSQLiteStatement __preparedStmtOfDeleteExpiredOrders;

  public PendingOrderDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPendingOrderEntity = new EntityInsertionAdapter<PendingOrderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `pending_orders` (`orderId`,`symbol`,`action`,`quantity`,`priceAtOrder`,`status`,`createdAt`,`message`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PendingOrderEntity entity) {
        statement.bindString(1, entity.getOrderId());
        statement.bindString(2, entity.getSymbol());
        statement.bindString(3, entity.getAction());
        statement.bindLong(4, entity.getQuantity());
        statement.bindDouble(5, entity.getPriceAtOrder());
        statement.bindString(6, entity.getStatus());
        statement.bindLong(7, entity.getCreatedAt());
        if (entity.getMessage() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getMessage());
        }
      }
    };
    this.__updateAdapterOfPendingOrderEntity = new EntityDeletionOrUpdateAdapter<PendingOrderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `pending_orders` SET `orderId` = ?,`symbol` = ?,`action` = ?,`quantity` = ?,`priceAtOrder` = ?,`status` = ?,`createdAt` = ?,`message` = ? WHERE `orderId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PendingOrderEntity entity) {
        statement.bindString(1, entity.getOrderId());
        statement.bindString(2, entity.getSymbol());
        statement.bindString(3, entity.getAction());
        statement.bindLong(4, entity.getQuantity());
        statement.bindDouble(5, entity.getPriceAtOrder());
        statement.bindString(6, entity.getStatus());
        statement.bindLong(7, entity.getCreatedAt());
        if (entity.getMessage() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getMessage());
        }
        statement.bindString(9, entity.getOrderId());
      }
    };
    this.__preparedStmtOfUpdateOrderStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE pending_orders SET status = ?, message = ? WHERE orderId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOrder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM pending_orders WHERE orderId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCompletedOrders = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM pending_orders WHERE status != 'PENDING'";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllOrders = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM pending_orders";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteExpiredOrders = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM pending_orders WHERE status IN ('FAILED', 'CANCELED') AND createdAt < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertOrder(final PendingOrderEntity order,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPendingOrderEntity.insert(order);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOrder(final PendingOrderEntity order,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPendingOrderEntity.handle(order);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOrderStatus(final String orderId, final String status, final String message,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateOrderStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
        _argIndex = 2;
        if (message == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, message);
        }
        _argIndex = 3;
        _stmt.bindString(_argIndex, orderId);
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
          __preparedStmtOfUpdateOrderStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOrder(final String orderId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOrder.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, orderId);
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
          __preparedStmtOfDeleteOrder.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCompletedOrders(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCompletedOrders.acquire();
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
          __preparedStmtOfDeleteCompletedOrders.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllOrders(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllOrders.acquire();
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
          __preparedStmtOfClearAllOrders.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteExpiredOrders(final long cutoffTime,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteExpiredOrders.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cutoffTime);
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
          __preparedStmtOfDeleteExpiredOrders.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PendingOrderEntity>> getPendingOrders() {
    final String _sql = "SELECT * FROM pending_orders WHERE status = 'PENDING' ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pending_orders"}, new Callable<List<PendingOrderEntity>>() {
      @Override
      @NonNull
      public List<PendingOrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "orderId");
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfPriceAtOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "priceAtOrder");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final List<PendingOrderEntity> _result = new ArrayList<PendingOrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PendingOrderEntity _item;
            final String _tmpOrderId;
            _tmpOrderId = _cursor.getString(_cursorIndexOfOrderId);
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final String _tmpAction;
            _tmpAction = _cursor.getString(_cursorIndexOfAction);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final double _tmpPriceAtOrder;
            _tmpPriceAtOrder = _cursor.getDouble(_cursorIndexOfPriceAtOrder);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpMessage;
            if (_cursor.isNull(_cursorIndexOfMessage)) {
              _tmpMessage = null;
            } else {
              _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            }
            _item = new PendingOrderEntity(_tmpOrderId,_tmpSymbol,_tmpAction,_tmpQuantity,_tmpPriceAtOrder,_tmpStatus,_tmpCreatedAt,_tmpMessage);
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
  public Flow<List<PendingOrderEntity>> getAllOrders() {
    final String _sql = "SELECT * FROM pending_orders ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pending_orders"}, new Callable<List<PendingOrderEntity>>() {
      @Override
      @NonNull
      public List<PendingOrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "orderId");
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfPriceAtOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "priceAtOrder");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final List<PendingOrderEntity> _result = new ArrayList<PendingOrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PendingOrderEntity _item;
            final String _tmpOrderId;
            _tmpOrderId = _cursor.getString(_cursorIndexOfOrderId);
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final String _tmpAction;
            _tmpAction = _cursor.getString(_cursorIndexOfAction);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final double _tmpPriceAtOrder;
            _tmpPriceAtOrder = _cursor.getDouble(_cursorIndexOfPriceAtOrder);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpMessage;
            if (_cursor.isNull(_cursorIndexOfMessage)) {
              _tmpMessage = null;
            } else {
              _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            }
            _item = new PendingOrderEntity(_tmpOrderId,_tmpSymbol,_tmpAction,_tmpQuantity,_tmpPriceAtOrder,_tmpStatus,_tmpCreatedAt,_tmpMessage);
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
  public Flow<List<PendingOrderEntity>> getNonSuccessOrders() {
    final String _sql = "SELECT * FROM pending_orders WHERE status != 'SUCCESS' ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pending_orders"}, new Callable<List<PendingOrderEntity>>() {
      @Override
      @NonNull
      public List<PendingOrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "orderId");
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfPriceAtOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "priceAtOrder");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final List<PendingOrderEntity> _result = new ArrayList<PendingOrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PendingOrderEntity _item;
            final String _tmpOrderId;
            _tmpOrderId = _cursor.getString(_cursorIndexOfOrderId);
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final String _tmpAction;
            _tmpAction = _cursor.getString(_cursorIndexOfAction);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final double _tmpPriceAtOrder;
            _tmpPriceAtOrder = _cursor.getDouble(_cursorIndexOfPriceAtOrder);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpMessage;
            if (_cursor.isNull(_cursorIndexOfMessage)) {
              _tmpMessage = null;
            } else {
              _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            }
            _item = new PendingOrderEntity(_tmpOrderId,_tmpSymbol,_tmpAction,_tmpQuantity,_tmpPriceAtOrder,_tmpStatus,_tmpCreatedAt,_tmpMessage);
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
  public Object getOrder(final String orderId,
      final Continuation<? super PendingOrderEntity> $completion) {
    final String _sql = "SELECT * FROM pending_orders WHERE orderId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, orderId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PendingOrderEntity>() {
      @Override
      @Nullable
      public PendingOrderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "orderId");
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfPriceAtOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "priceAtOrder");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final PendingOrderEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpOrderId;
            _tmpOrderId = _cursor.getString(_cursorIndexOfOrderId);
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final String _tmpAction;
            _tmpAction = _cursor.getString(_cursorIndexOfAction);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final double _tmpPriceAtOrder;
            _tmpPriceAtOrder = _cursor.getDouble(_cursorIndexOfPriceAtOrder);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpMessage;
            if (_cursor.isNull(_cursorIndexOfMessage)) {
              _tmpMessage = null;
            } else {
              _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            }
            _result = new PendingOrderEntity(_tmpOrderId,_tmpSymbol,_tmpAction,_tmpQuantity,_tmpPriceAtOrder,_tmpStatus,_tmpCreatedAt,_tmpMessage);
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
  public Flow<PendingOrderEntity> getOrderFlow(final String orderId) {
    final String _sql = "SELECT * FROM pending_orders WHERE orderId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, orderId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pending_orders"}, new Callable<PendingOrderEntity>() {
      @Override
      @Nullable
      public PendingOrderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "orderId");
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfPriceAtOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "priceAtOrder");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final PendingOrderEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpOrderId;
            _tmpOrderId = _cursor.getString(_cursorIndexOfOrderId);
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final String _tmpAction;
            _tmpAction = _cursor.getString(_cursorIndexOfAction);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final double _tmpPriceAtOrder;
            _tmpPriceAtOrder = _cursor.getDouble(_cursorIndexOfPriceAtOrder);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpMessage;
            if (_cursor.isNull(_cursorIndexOfMessage)) {
              _tmpMessage = null;
            } else {
              _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            }
            _result = new PendingOrderEntity(_tmpOrderId,_tmpSymbol,_tmpAction,_tmpQuantity,_tmpPriceAtOrder,_tmpStatus,_tmpCreatedAt,_tmpMessage);
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
  public Flow<Integer> getPendingOrderCount() {
    final String _sql = "SELECT COUNT(*) FROM pending_orders WHERE status = 'PENDING'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pending_orders"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
