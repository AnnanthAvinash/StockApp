package avinash.app.mystocks.data.remote.websocket;

import avinash.app.mystocks.data.cache.StockCacheDataSource;
import avinash.app.mystocks.data.local.dao.PendingOrderDao;
import avinash.app.mystocks.data.local.dao.PortfolioDao;
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
public final class WebSocketConnectionManager_Factory implements Factory<WebSocketConnectionManager> {
  private final Provider<StockSocketClient> socketClientProvider;

  private final Provider<StockCacheDataSource> cacheProvider;

  private final Provider<PendingOrderDao> pendingOrderDaoProvider;

  private final Provider<PortfolioDao> portfolioDaoProvider;

  public WebSocketConnectionManager_Factory(Provider<StockSocketClient> socketClientProvider,
      Provider<StockCacheDataSource> cacheProvider,
      Provider<PendingOrderDao> pendingOrderDaoProvider,
      Provider<PortfolioDao> portfolioDaoProvider) {
    this.socketClientProvider = socketClientProvider;
    this.cacheProvider = cacheProvider;
    this.pendingOrderDaoProvider = pendingOrderDaoProvider;
    this.portfolioDaoProvider = portfolioDaoProvider;
  }

  @Override
  public WebSocketConnectionManager get() {
    return newInstance(socketClientProvider.get(), cacheProvider.get(), pendingOrderDaoProvider.get(), portfolioDaoProvider.get());
  }

  public static WebSocketConnectionManager_Factory create(
      Provider<StockSocketClient> socketClientProvider,
      Provider<StockCacheDataSource> cacheProvider,
      Provider<PendingOrderDao> pendingOrderDaoProvider,
      Provider<PortfolioDao> portfolioDaoProvider) {
    return new WebSocketConnectionManager_Factory(socketClientProvider, cacheProvider, pendingOrderDaoProvider, portfolioDaoProvider);
  }

  public static WebSocketConnectionManager newInstance(StockSocketClient socketClient,
      StockCacheDataSource cache, PendingOrderDao pendingOrderDao, PortfolioDao portfolioDao) {
    return new WebSocketConnectionManager(socketClient, cache, pendingOrderDao, portfolioDao);
  }
}
