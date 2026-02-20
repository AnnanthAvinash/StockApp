package avinash.app.mystocks.data.local.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import avinash.app.mystocks.data.local.dao.PendingOrderDao;
import avinash.app.mystocks.data.local.dao.PendingOrderDao_Impl;
import avinash.app.mystocks.data.local.dao.PortfolioDao;
import avinash.app.mystocks.data.local.dao.PortfolioDao_Impl;
import avinash.app.mystocks.data.local.dao.RecentViewedDao;
import avinash.app.mystocks.data.local.dao.RecentViewedDao_Impl;
import avinash.app.mystocks.data.local.dao.StockDao;
import avinash.app.mystocks.data.local.dao.StockDao_Impl;
import avinash.app.mystocks.data.local.dao.WishlistDao;
import avinash.app.mystocks.data.local.dao.WishlistDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StockDatabase_Impl extends StockDatabase {
  private volatile StockDao _stockDao;

  private volatile RecentViewedDao _recentViewedDao;

  private volatile WishlistDao _wishlistDao;

  private volatile PortfolioDao _portfolioDao;

  private volatile PendingOrderDao _pendingOrderDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `stocks` (`symbol` TEXT NOT NULL, `name` TEXT NOT NULL, `logoUrl` TEXT NOT NULL, `currentPrice` REAL NOT NULL, `previousPrice` REAL NOT NULL, `dayHigh` REAL NOT NULL, `dayLow` REAL NOT NULL, `volume` INTEGER NOT NULL, `openPrice` REAL NOT NULL, `lastUpdated` INTEGER NOT NULL, PRIMARY KEY(`symbol`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `recent_viewed` (`symbol` TEXT NOT NULL, `name` TEXT NOT NULL, `logoUrl` TEXT NOT NULL, `viewedAt` INTEGER NOT NULL, PRIMARY KEY(`symbol`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `wishlist` (`symbol` TEXT NOT NULL, `addedAt` INTEGER NOT NULL, PRIMARY KEY(`symbol`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `portfolio` (`symbol` TEXT NOT NULL, `quantity` INTEGER NOT NULL, `averagePrice` REAL NOT NULL, `totalInvested` REAL NOT NULL, `lastUpdated` INTEGER NOT NULL, PRIMARY KEY(`symbol`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pending_orders` (`orderId` TEXT NOT NULL, `symbol` TEXT NOT NULL, `action` TEXT NOT NULL, `quantity` INTEGER NOT NULL, `priceAtOrder` REAL NOT NULL, `status` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `message` TEXT, PRIMARY KEY(`orderId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2d2eb22093a60b628a1fbe6fa8317a02')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `stocks`");
        db.execSQL("DROP TABLE IF EXISTS `recent_viewed`");
        db.execSQL("DROP TABLE IF EXISTS `wishlist`");
        db.execSQL("DROP TABLE IF EXISTS `portfolio`");
        db.execSQL("DROP TABLE IF EXISTS `pending_orders`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsStocks = new HashMap<String, TableInfo.Column>(10);
        _columnsStocks.put("symbol", new TableInfo.Column("symbol", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStocks.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStocks.put("logoUrl", new TableInfo.Column("logoUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStocks.put("currentPrice", new TableInfo.Column("currentPrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStocks.put("previousPrice", new TableInfo.Column("previousPrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStocks.put("dayHigh", new TableInfo.Column("dayHigh", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStocks.put("dayLow", new TableInfo.Column("dayLow", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStocks.put("volume", new TableInfo.Column("volume", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStocks.put("openPrice", new TableInfo.Column("openPrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStocks.put("lastUpdated", new TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStocks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStocks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStocks = new TableInfo("stocks", _columnsStocks, _foreignKeysStocks, _indicesStocks);
        final TableInfo _existingStocks = TableInfo.read(db, "stocks");
        if (!_infoStocks.equals(_existingStocks)) {
          return new RoomOpenHelper.ValidationResult(false, "stocks(avinash.app.mystocks.data.local.entity.StockEntity).\n"
                  + " Expected:\n" + _infoStocks + "\n"
                  + " Found:\n" + _existingStocks);
        }
        final HashMap<String, TableInfo.Column> _columnsRecentViewed = new HashMap<String, TableInfo.Column>(4);
        _columnsRecentViewed.put("symbol", new TableInfo.Column("symbol", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecentViewed.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecentViewed.put("logoUrl", new TableInfo.Column("logoUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecentViewed.put("viewedAt", new TableInfo.Column("viewedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecentViewed = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRecentViewed = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRecentViewed = new TableInfo("recent_viewed", _columnsRecentViewed, _foreignKeysRecentViewed, _indicesRecentViewed);
        final TableInfo _existingRecentViewed = TableInfo.read(db, "recent_viewed");
        if (!_infoRecentViewed.equals(_existingRecentViewed)) {
          return new RoomOpenHelper.ValidationResult(false, "recent_viewed(avinash.app.mystocks.data.local.entity.RecentViewedEntity).\n"
                  + " Expected:\n" + _infoRecentViewed + "\n"
                  + " Found:\n" + _existingRecentViewed);
        }
        final HashMap<String, TableInfo.Column> _columnsWishlist = new HashMap<String, TableInfo.Column>(2);
        _columnsWishlist.put("symbol", new TableInfo.Column("symbol", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWishlist.put("addedAt", new TableInfo.Column("addedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWishlist = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWishlist = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWishlist = new TableInfo("wishlist", _columnsWishlist, _foreignKeysWishlist, _indicesWishlist);
        final TableInfo _existingWishlist = TableInfo.read(db, "wishlist");
        if (!_infoWishlist.equals(_existingWishlist)) {
          return new RoomOpenHelper.ValidationResult(false, "wishlist(avinash.app.mystocks.data.local.entity.WishlistEntity).\n"
                  + " Expected:\n" + _infoWishlist + "\n"
                  + " Found:\n" + _existingWishlist);
        }
        final HashMap<String, TableInfo.Column> _columnsPortfolio = new HashMap<String, TableInfo.Column>(5);
        _columnsPortfolio.put("symbol", new TableInfo.Column("symbol", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPortfolio.put("quantity", new TableInfo.Column("quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPortfolio.put("averagePrice", new TableInfo.Column("averagePrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPortfolio.put("totalInvested", new TableInfo.Column("totalInvested", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPortfolio.put("lastUpdated", new TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPortfolio = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPortfolio = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPortfolio = new TableInfo("portfolio", _columnsPortfolio, _foreignKeysPortfolio, _indicesPortfolio);
        final TableInfo _existingPortfolio = TableInfo.read(db, "portfolio");
        if (!_infoPortfolio.equals(_existingPortfolio)) {
          return new RoomOpenHelper.ValidationResult(false, "portfolio(avinash.app.mystocks.data.local.entity.PortfolioEntity).\n"
                  + " Expected:\n" + _infoPortfolio + "\n"
                  + " Found:\n" + _existingPortfolio);
        }
        final HashMap<String, TableInfo.Column> _columnsPendingOrders = new HashMap<String, TableInfo.Column>(8);
        _columnsPendingOrders.put("orderId", new TableInfo.Column("orderId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPendingOrders.put("symbol", new TableInfo.Column("symbol", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPendingOrders.put("action", new TableInfo.Column("action", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPendingOrders.put("quantity", new TableInfo.Column("quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPendingOrders.put("priceAtOrder", new TableInfo.Column("priceAtOrder", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPendingOrders.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPendingOrders.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPendingOrders.put("message", new TableInfo.Column("message", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPendingOrders = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPendingOrders = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPendingOrders = new TableInfo("pending_orders", _columnsPendingOrders, _foreignKeysPendingOrders, _indicesPendingOrders);
        final TableInfo _existingPendingOrders = TableInfo.read(db, "pending_orders");
        if (!_infoPendingOrders.equals(_existingPendingOrders)) {
          return new RoomOpenHelper.ValidationResult(false, "pending_orders(avinash.app.mystocks.data.local.entity.PendingOrderEntity).\n"
                  + " Expected:\n" + _infoPendingOrders + "\n"
                  + " Found:\n" + _existingPendingOrders);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "2d2eb22093a60b628a1fbe6fa8317a02", "2d78bcbe9714c24ce1a855c7163aa99f");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "stocks","recent_viewed","wishlist","portfolio","pending_orders");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `stocks`");
      _db.execSQL("DELETE FROM `recent_viewed`");
      _db.execSQL("DELETE FROM `wishlist`");
      _db.execSQL("DELETE FROM `portfolio`");
      _db.execSQL("DELETE FROM `pending_orders`");
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
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(StockDao.class, StockDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RecentViewedDao.class, RecentViewedDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WishlistDao.class, WishlistDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PortfolioDao.class, PortfolioDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PendingOrderDao.class, PendingOrderDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public StockDao stockDao() {
    if (_stockDao != null) {
      return _stockDao;
    } else {
      synchronized(this) {
        if(_stockDao == null) {
          _stockDao = new StockDao_Impl(this);
        }
        return _stockDao;
      }
    }
  }

  @Override
  public RecentViewedDao recentViewedDao() {
    if (_recentViewedDao != null) {
      return _recentViewedDao;
    } else {
      synchronized(this) {
        if(_recentViewedDao == null) {
          _recentViewedDao = new RecentViewedDao_Impl(this);
        }
        return _recentViewedDao;
      }
    }
  }

  @Override
  public WishlistDao wishlistDao() {
    if (_wishlistDao != null) {
      return _wishlistDao;
    } else {
      synchronized(this) {
        if(_wishlistDao == null) {
          _wishlistDao = new WishlistDao_Impl(this);
        }
        return _wishlistDao;
      }
    }
  }

  @Override
  public PortfolioDao portfolioDao() {
    if (_portfolioDao != null) {
      return _portfolioDao;
    } else {
      synchronized(this) {
        if(_portfolioDao == null) {
          _portfolioDao = new PortfolioDao_Impl(this);
        }
        return _portfolioDao;
      }
    }
  }

  @Override
  public PendingOrderDao pendingOrderDao() {
    if (_pendingOrderDao != null) {
      return _pendingOrderDao;
    } else {
      synchronized(this) {
        if(_pendingOrderDao == null) {
          _pendingOrderDao = new PendingOrderDao_Impl(this);
        }
        return _pendingOrderDao;
      }
    }
  }
}
