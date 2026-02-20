package avinash.app.mystocks.ui.screens.detail;

import androidx.lifecycle.SavedStateHandle;
import avinash.app.mystocks.data.remote.websocket.SubscriptionManager;
import avinash.app.mystocks.data.repository.PortfolioRepository;
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
public final class DetailViewModel_Factory implements Factory<DetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<StockRepository> stockRepositoryProvider;

  private final Provider<PortfolioRepository> portfolioRepositoryProvider;

  private final Provider<SubscriptionManager> subscriptionManagerProvider;

  public DetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<StockRepository> stockRepositoryProvider,
      Provider<PortfolioRepository> portfolioRepositoryProvider,
      Provider<SubscriptionManager> subscriptionManagerProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.stockRepositoryProvider = stockRepositoryProvider;
    this.portfolioRepositoryProvider = portfolioRepositoryProvider;
    this.subscriptionManagerProvider = subscriptionManagerProvider;
  }

  @Override
  public DetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), stockRepositoryProvider.get(), portfolioRepositoryProvider.get(), subscriptionManagerProvider.get());
  }

  public static DetailViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<StockRepository> stockRepositoryProvider,
      Provider<PortfolioRepository> portfolioRepositoryProvider,
      Provider<SubscriptionManager> subscriptionManagerProvider) {
    return new DetailViewModel_Factory(savedStateHandleProvider, stockRepositoryProvider, portfolioRepositoryProvider, subscriptionManagerProvider);
  }

  public static DetailViewModel newInstance(SavedStateHandle savedStateHandle,
      StockRepository stockRepository, PortfolioRepository portfolioRepository,
      SubscriptionManager subscriptionManager) {
    return new DetailViewModel(savedStateHandle, stockRepository, portfolioRepository, subscriptionManager);
  }
}
