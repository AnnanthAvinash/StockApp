package avinash.app.mystocks.di;

import avinash.app.mystocks.data.local.dao.StockDao;
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
public final class DatabaseModule_ProvideStockDaoFactory implements Factory<StockDao> {
  private final Provider<StockDatabase> databaseProvider;

  public DatabaseModule_ProvideStockDaoFactory(Provider<StockDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public StockDao get() {
    return provideStockDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideStockDaoFactory create(
      Provider<StockDatabase> databaseProvider) {
    return new DatabaseModule_ProvideStockDaoFactory(databaseProvider);
  }

  public static StockDao provideStockDao(StockDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideStockDao(database));
  }
}
