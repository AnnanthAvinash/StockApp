package avinash.app.mystocks.lifecycle;

import avinash.app.mystocks.data.remote.websocket.SubscriptionManager;
import avinash.app.mystocks.data.remote.websocket.WebSocketConnectionManager;
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
public final class AppLifecycleObserver_Factory implements Factory<AppLifecycleObserver> {
  private final Provider<WebSocketConnectionManager> wsConnectionManagerProvider;

  private final Provider<SubscriptionManager> subscriptionManagerProvider;

  public AppLifecycleObserver_Factory(
      Provider<WebSocketConnectionManager> wsConnectionManagerProvider,
      Provider<SubscriptionManager> subscriptionManagerProvider) {
    this.wsConnectionManagerProvider = wsConnectionManagerProvider;
    this.subscriptionManagerProvider = subscriptionManagerProvider;
  }

  @Override
  public AppLifecycleObserver get() {
    return newInstance(wsConnectionManagerProvider.get(), subscriptionManagerProvider.get());
  }

  public static AppLifecycleObserver_Factory create(
      Provider<WebSocketConnectionManager> wsConnectionManagerProvider,
      Provider<SubscriptionManager> subscriptionManagerProvider) {
    return new AppLifecycleObserver_Factory(wsConnectionManagerProvider, subscriptionManagerProvider);
  }

  public static AppLifecycleObserver newInstance(WebSocketConnectionManager wsConnectionManager,
      SubscriptionManager subscriptionManager) {
    return new AppLifecycleObserver(wsConnectionManager, subscriptionManager);
  }
}
