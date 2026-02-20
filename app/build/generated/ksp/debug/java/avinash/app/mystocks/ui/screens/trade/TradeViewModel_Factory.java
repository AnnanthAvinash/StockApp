package avinash.app.mystocks.ui.screens.trade;

import androidx.lifecycle.SavedStateHandle;
import avinash.app.mystocks.data.remote.api.StockApi;
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
public final class TradeViewModel_Factory implements Factory<TradeViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<StockRepository> stockRepositoryProvider;

  private final Provider<PortfolioRepository> portfolioRepositoryProvider;

  private final Provider<StockApi> stockApiProvider;

  public TradeViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<StockRepository> stockRepositoryProvider,
      Provider<PortfolioRepository> portfolioRepositoryProvider,
      Provider<StockApi> stockApiProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.stockRepositoryProvider = stockRepositoryProvider;
    this.portfolioRepositoryProvider = portfolioRepositoryProvider;
    this.stockApiProvider = stockApiProvider;
  }

  @Override
  public TradeViewModel get() {
    return newInstance(savedStateHandleProvider.get(), stockRepositoryProvider.get(), portfolioRepositoryProvider.get(), stockApiProvider.get());
  }

  public static TradeViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<StockRepository> stockRepositoryProvider,
      Provider<PortfolioRepository> portfolioRepositoryProvider,
      Provider<StockApi> stockApiProvider) {
    return new TradeViewModel_Factory(savedStateHandleProvider, stockRepositoryProvider, portfolioRepositoryProvider, stockApiProvider);
  }

  public static TradeViewModel newInstance(SavedStateHandle savedStateHandle,
      StockRepository stockRepository, PortfolioRepository portfolioRepository, StockApi stockApi) {
    return new TradeViewModel(savedStateHandle, stockRepository, portfolioRepository, stockApi);
  }
}
