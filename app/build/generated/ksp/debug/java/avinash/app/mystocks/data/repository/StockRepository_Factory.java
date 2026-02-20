package avinash.app.mystocks.data.repository;

import avinash.app.mystocks.data.local.dao.RecentViewedDao;
import avinash.app.mystocks.data.local.dao.StockDao;
import avinash.app.mystocks.data.local.dao.WishlistDao;
import avinash.app.mystocks.data.remote.api.StockApi;
import avinash.app.mystocks.data.remote.websocket.WebSocketConnectionManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class StockRepository_Factory implements Factory<StockRepository> {
  private final Provider<StockApi> stockApiProvider;

  private final Provider<StockDao> stockDaoProvider;

  private final Provider<RecentViewedDao> recentViewedDaoProvider;

  private final Provider<WishlistDao> wishlistDaoProvider;

  private final Provider<WebSocketConnectionManager> wsConnectionManagerProvider;

  public StockRepository_Factory(Provider<StockApi> stockApiProvider,
      Provider<StockDao> stockDaoProvider, Provider<RecentViewedDao> recentViewedDaoProvider,
      Provider<WishlistDao> wishlistDaoProvider,
      Provider<WebSocketConnectionManager> wsConnectionManagerProvider) {
    this.stockApiProvider = stockApiProvider;
    this.stockDaoProvider = stockDaoProvider;
    this.recentViewedDaoProvider = recentViewedDaoProvider;
    this.wishlistDaoProvider = wishlistDaoProvider;
    this.wsConnectionManagerProvider = wsConnectionManagerProvider;
  }

  @Override
  public StockRepository get() {
    return newInstance(stockApiProvider.get(), stockDaoProvider.get(), recentViewedDaoProvider.get(), wishlistDaoProvider.get(), wsConnectionManagerProvider.get());
  }

  public static StockRepository_Factory create(Provider<StockApi> stockApiProvider,
      Provider<StockDao> stockDaoProvider, Provider<RecentViewedDao> recentViewedDaoProvider,
      Provider<WishlistDao> wishlistDaoProvider,
      Provider<WebSocketConnectionManager> wsConnectionManagerProvider) {
    return new StockRepository_Factory(stockApiProvider, stockDaoProvider, recentViewedDaoProvider, wishlistDaoProvider, wsConnectionManagerProvider);
  }

  public static StockRepository newInstance(StockApi stockApi, StockDao stockDao,
      RecentViewedDao recentViewedDao, WishlistDao wishlistDao,
      WebSocketConnectionManager wsConnectionManager) {
    return new StockRepository(stockApi, stockDao, recentViewedDao, wishlistDao, wsConnectionManager);
  }
}
