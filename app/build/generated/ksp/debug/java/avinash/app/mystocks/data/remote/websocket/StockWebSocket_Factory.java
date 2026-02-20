package avinash.app.mystocks.data.remote.websocket;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.serialization.json.Json;

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
public final class StockWebSocket_Factory implements Factory<StockWebSocket> {
  private final Provider<Json> jsonProvider;

  public StockWebSocket_Factory(Provider<Json> jsonProvider) {
    this.jsonProvider = jsonProvider;
  }

  @Override
  public StockWebSocket get() {
    return newInstance(jsonProvider.get());
  }

  public static StockWebSocket_Factory create(Provider<Json> jsonProvider) {
    return new StockWebSocket_Factory(jsonProvider);
  }

  public static StockWebSocket newInstance(Json json) {
    return new StockWebSocket(json);
  }
}
