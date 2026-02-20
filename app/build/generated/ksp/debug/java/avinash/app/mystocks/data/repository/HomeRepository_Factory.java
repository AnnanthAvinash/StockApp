package avinash.app.mystocks.data.repository;

import avinash.app.mystocks.data.cache.StockCacheDataSource;
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
public final class HomeRepository_Factory implements Factory<HomeRepository> {
  private final Provider<StockApi> stockApiProvider;

  private final Provider<StockCacheDataSource> cacheProvider;

  public HomeRepository_Factory(Provider<StockApi> stockApiProvider,
      Provider<StockCacheDataSource> cacheProvider) {
    this.stockApiProvider = stockApiProvider;
    this.cacheProvider = cacheProvider;
  }

  @Override
  public HomeRepository get() {
    return newInstance(stockApiProvider.get(), cacheProvider.get());
  }

  public static HomeRepository_Factory create(Provider<StockApi> stockApiProvider,
      Provider<StockCacheDataSource> cacheProvider) {
    return new HomeRepository_Factory(stockApiProvider, cacheProvider);
  }

  public static HomeRepository newInstance(StockApi stockApi, StockCacheDataSource cache) {
    return new HomeRepository(stockApi, cache);
  }
}
