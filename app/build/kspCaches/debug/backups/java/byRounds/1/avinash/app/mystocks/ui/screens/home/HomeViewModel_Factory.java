package avinash.app.mystocks.ui.screens.home;

import avinash.app.mystocks.data.remote.websocket.SubscriptionManager;
import avinash.app.mystocks.data.repository.HomeRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<HomeRepository> homeRepositoryProvider;

  private final Provider<StockRepository> stockRepositoryProvider;

  private final Provider<SubscriptionManager> subscriptionManagerProvider;

  public HomeViewModel_Factory(Provider<HomeRepository> homeRepositoryProvider,
      Provider<StockRepository> stockRepositoryProvider,
      Provider<SubscriptionManager> subscriptionManagerProvider) {
    this.homeRepositoryProvider = homeRepositoryProvider;
    this.stockRepositoryProvider = stockRepositoryProvider;
    this.subscriptionManagerProvider = subscriptionManagerProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(homeRepositoryProvider.get(), stockRepositoryProvider.get(), subscriptionManagerProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<HomeRepository> homeRepositoryProvider,
      Provider<StockRepository> stockRepositoryProvider,
      Provider<SubscriptionManager> subscriptionManagerProvider) {
    return new HomeViewModel_Factory(homeRepositoryProvider, stockRepositoryProvider, subscriptionManagerProvider);
  }

  public static HomeViewModel newInstance(HomeRepository homeRepository,
      StockRepository stockRepository, SubscriptionManager subscriptionManager) {
    return new HomeViewModel(homeRepository, stockRepository, subscriptionManager);
  }
}
