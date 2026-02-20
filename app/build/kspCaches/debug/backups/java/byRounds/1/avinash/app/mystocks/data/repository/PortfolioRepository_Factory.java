package avinash.app.mystocks.data.repository;

import avinash.app.mystocks.data.local.dao.PendingOrderDao;
import avinash.app.mystocks.data.local.dao.PortfolioDao;
import avinash.app.mystocks.data.local.dao.StockDao;
import avinash.app.mystocks.data.remote.api.StockApi;
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
public final class PortfolioRepository_Factory implements Factory<PortfolioRepository> {
  private final Provider<StockApi> stockApiProvider;

  private final Provider<PortfolioDao> portfolioDaoProvider;

  private final Provider<PendingOrderDao> pendingOrderDaoProvider;

  private final Provider<StockDao> stockDaoProvider;

  public PortfolioRepository_Factory(Provider<StockApi> stockApiProvider,
      Provider<PortfolioDao> portfolioDaoProvider,
      Provider<PendingOrderDao> pendingOrderDaoProvider, Provider<StockDao> stockDaoProvider) {
    this.stockApiProvider = stockApiProvider;
    this.portfolioDaoProvider = portfolioDaoProvider;
    this.pendingOrderDaoProvider = pendingOrderDaoProvider;
    this.stockDaoProvider = stockDaoProvider;
  }

  @Override
  public PortfolioRepository get() {
    return newInstance(stockApiProvider.get(), portfolioDaoProvider.get(), pendingOrderDaoProvider.get(), stockDaoProvider.get());
  }

  public static PortfolioRepository_Factory create(Provider<StockApi> stockApiProvider,
      Provider<PortfolioDao> portfolioDaoProvider,
      Provider<PendingOrderDao> pendingOrderDaoProvider, Provider<StockDao> stockDaoProvider) {
    return new PortfolioRepository_Factory(stockApiProvider, portfolioDaoProvider, pendingOrderDaoProvider, stockDaoProvider);
  }

  public static PortfolioRepository newInstance(StockApi stockApi, PortfolioDao portfolioDao,
      PendingOrderDao pendingOrderDao, StockDao stockDao) {
    return new PortfolioRepository(stockApi, portfolioDao, pendingOrderDao, stockDao);
  }
}
