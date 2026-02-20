package avinash.app.mystocks.di;

import avinash.app.mystocks.data.local.dao.RecentViewedDao;
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
public final class DatabaseModule_ProvideRecentViewedDaoFactory implements Factory<RecentViewedDao> {
  private final Provider<StockDatabase> databaseProvider;

  public DatabaseModule_ProvideRecentViewedDaoFactory(Provider<StockDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public RecentViewedDao get() {
    return provideRecentViewedDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideRecentViewedDaoFactory create(
      Provider<StockDatabase> databaseProvider) {
    return new DatabaseModule_ProvideRecentViewedDaoFactory(databaseProvider);
  }

  public static RecentViewedDao provideRecentViewedDao(StockDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRecentViewedDao(database));
  }
}
