package avinash.app.mystocks.ui.screens.portfolio;

import avinash.app.mystocks.data.remote.websocket.SubscriptionManager;
import avinash.app.mystocks.data.repository.PortfolioRepository;
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
public final class PortfolioViewModel_Factory implements Factory<PortfolioViewModel> {
  private final Provider<PortfolioRepository> portfolioRepositoryProvider;

  private final Provider<SubscriptionManager> subscriptionManagerProvider;

  public PortfolioViewModel_Factory(Provider<PortfolioRepository> portfolioRepositoryProvider,
      Provider<SubscriptionManager> subscriptionManagerProvider) {
    this.portfolioRepositoryProvider = portfolioRepositoryProvider;
    this.subscriptionManagerProvider = subscriptionManagerProvider;
  }

  @Override
  public PortfolioViewModel get() {
    return newInstance(portfolioRepositoryProvider.get(), subscriptionManagerProvider.get());
  }

  public static PortfolioViewModel_Factory create(
      Provider<PortfolioRepository> portfolioRepositoryProvider,
      Provider<SubscriptionManager> subscriptionManagerProvider) {
    return new PortfolioViewModel_Factory(portfolioRepositoryProvider, subscriptionManagerProvider);
  }

  public static PortfolioViewModel newInstance(PortfolioRepository portfolioRepository,
      SubscriptionManager subscriptionManager) {
    return new PortfolioViewModel(portfolioRepository, subscriptionManager);
  }
}
