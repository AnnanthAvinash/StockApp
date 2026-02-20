package avinash.app.mystocks.di;

import avinash.app.mystocks.data.local.dao.PortfolioDao;
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
public final class DatabaseModule_ProvidePortfolioDaoFactory implements Factory<PortfolioDao> {
  private final Provider<StockDatabase> databaseProvider;

  public DatabaseModule_ProvidePortfolioDaoFactory(Provider<StockDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PortfolioDao get() {
    return providePortfolioDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvidePortfolioDaoFactory create(
      Provider<StockDatabase> databaseProvider) {
    return new DatabaseModule_ProvidePortfolioDaoFactory(databaseProvider);
  }

  public static PortfolioDao providePortfolioDao(StockDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePortfolioDao(database));
  }
}
