package avinash.app.mystocks.data.cache;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class StockCacheDataSource_Factory implements Factory<StockCacheDataSource> {
  @Override
  public StockCacheDataSource get() {
    return newInstance();
  }

  public static StockCacheDataSource_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static StockCacheDataSource newInstance() {
    return new StockCacheDataSource();
  }

  private static final class InstanceHolder {
    private static final StockCacheDataSource_Factory INSTANCE = new StockCacheDataSource_Factory();
  }
}
