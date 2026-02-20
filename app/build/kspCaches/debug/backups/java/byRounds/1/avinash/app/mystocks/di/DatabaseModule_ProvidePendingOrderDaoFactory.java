package avinash.app.mystocks.di;

import avinash.app.mystocks.data.local.dao.PendingOrderDao;
import avinash.app.mystocks.data.local.db.StockDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DatabaseModule_ProvidePendingOrderDaoFactory implements Factory<PendingOrderDao> {
  private final Provider<StockDatabase> databaseProvider;

  public DatabaseModule_ProvidePendingOrderDaoFactory(Provider<StockDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PendingOrderDao get() {
    return providePendingOrderDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvidePendingOrderDaoFactory create(
      Provider<StockDatabase> databaseProvider) {
    return new DatabaseModule_ProvidePendingOrderDaoFactory(databaseProvider);
  }

  public static PendingOrderDao providePendingOrderDao(StockDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePendingOrderDao(database));
  }
}
