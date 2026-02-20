package avinash.app.mystocks.data.remote.api;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.ktor.client.HttpClient;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class StockApi_Factory implements Factory<StockApi> {
  private final Provider<HttpClient> clientProvider;

  public StockApi_Factory(Provider<HttpClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public StockApi get() {
    return newInstance(clientProvider.get());
  }

  public static StockApi_Factory create(Provider<HttpClient> clientProvider) {
    return new StockApi_Factory(clientProvider);
  }

  public static StockApi newInstance(HttpClient client) {
    return new StockApi(client);
  }
}
