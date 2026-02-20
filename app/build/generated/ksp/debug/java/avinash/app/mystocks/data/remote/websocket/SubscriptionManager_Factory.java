package avinash.app.mystocks.data.remote.websocket;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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
public final class SubscriptionManager_Factory implements Factory<SubscriptionManager> {
  private final Provider<WebSocketConnectionManager> wsConnectionManagerProvider;

  public SubscriptionManager_Factory(
      Provider<WebSocketConnectionManager> wsConnectionManagerProvider) {
    this.wsConnectionManagerProvider = wsConnectionManagerProvider;
  }

  @Override
  public SubscriptionManager get() {
    return newInstance(wsConnectionManagerProvider.get());
  }

  public static SubscriptionManager_Factory create(
      Provider<WebSocketConnectionManager> wsConnectionManagerProvider) {
    return new SubscriptionManager_Factory(wsConnectionManagerProvider);
  }

  public static SubscriptionManager newInstance(WebSocketConnectionManager wsConnectionManager) {
    return new SubscriptionManager(wsConnectionManager);
  }
}
