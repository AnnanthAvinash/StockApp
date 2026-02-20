package avinash.app.mystocks.di;

import avinash.app.mystocks.data.local.dao.WishlistDao;
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
public final class DatabaseModule_ProvideWishlistDaoFactory implements Factory<WishlistDao> {
  private final Provider<StockDatabase> databaseProvider;

  public DatabaseModule_ProvideWishlistDaoFactory(Provider<StockDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public WishlistDao get() {
    return provideWishlistDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideWishlistDaoFactory create(
      Provider<StockDatabase> databaseProvider) {
    return new DatabaseModule_ProvideWishlistDaoFactory(databaseProvider);
  }

  public static WishlistDao provideWishlistDao(StockDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideWishlistDao(database));
  }
}
