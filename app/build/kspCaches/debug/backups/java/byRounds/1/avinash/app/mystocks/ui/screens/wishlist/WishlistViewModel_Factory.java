package avinash.app.mystocks.ui.screens.wishlist;

import avinash.app.mystocks.data.remote.websocket.SubscriptionManager;
import avinash.app.mystocks.data.repository.StockRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class WishlistViewModel_Factory implements Factory<WishlistViewModel> {
  private final Provider<StockRepository> stockRepositoryProvider;

  private final Provider<SubscriptionManager> subscriptionManagerProvider;

  public WishlistViewModel_Factory(Provider<StockRepository> stockRepositoryProvider,
      Provider<SubscriptionManager> subscriptionManagerProvider) {
    this.stockRepositoryProvider = stockRepositoryProvider;
    this.subscriptionManagerProvider = subscriptionManagerProvider;
  }

  @Override
  public WishlistViewModel get() {
    return newInstance(stockRepositoryProvider.get(), subscriptionManagerProvider.get());
  }

  public static WishlistViewModel_Factory create(Provider<StockRepository> stockRepositoryProvider,
      Provider<SubscriptionManager> subscriptionManagerProvider) {
    return new WishlistViewModel_Factory(stockRepositoryProvider, subscriptionManagerProvider);
  }

  public static WishlistViewModel newInstance(StockRepository stockRepository,
      SubscriptionManager subscriptionManager) {
    return new WishlistViewModel(stockRepository, subscriptionManager);
  }
}
