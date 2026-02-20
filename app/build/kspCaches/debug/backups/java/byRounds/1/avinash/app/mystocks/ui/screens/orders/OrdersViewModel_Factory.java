package avinash.app.mystocks.ui.screens.orders;

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
public final class OrdersViewModel_Factory implements Factory<OrdersViewModel> {
  private final Provider<PortfolioRepository> portfolioRepositoryProvider;

  public OrdersViewModel_Factory(Provider<PortfolioRepository> portfolioRepositoryProvider) {
    this.portfolioRepositoryProvider = portfolioRepositoryProvider;
  }

  @Override
  public OrdersViewModel get() {
    return newInstance(portfolioRepositoryProvider.get());
  }

  public static OrdersViewModel_Factory create(
      Provider<PortfolioRepository> portfolioRepositoryProvider) {
    return new OrdersViewModel_Factory(portfolioRepositoryProvider);
  }

  public static OrdersViewModel newInstance(PortfolioRepository portfolioRepository) {
    return new OrdersViewModel(portfolioRepository);
  }
}
