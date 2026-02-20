package avinash.app.mystocks;

import avinash.app.mystocks.lifecycle.AppLifecycleObserver;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MyStocksApp_MembersInjector implements MembersInjector<MyStocksApp> {
  private final Provider<AppLifecycleObserver> appLifecycleObserverProvider;

  public MyStocksApp_MembersInjector(Provider<AppLifecycleObserver> appLifecycleObserverProvider) {
    this.appLifecycleObserverProvider = appLifecycleObserverProvider;
  }

  public static MembersInjector<MyStocksApp> create(
      Provider<AppLifecycleObserver> appLifecycleObserverProvider) {
    return new MyStocksApp_MembersInjector(appLifecycleObserverProvider);
  }

  @Override
  public void injectMembers(MyStocksApp instance) {
    injectAppLifecycleObserver(instance, appLifecycleObserverProvider.get());
  }

  @InjectedFieldSignature("avinash.app.mystocks.MyStocksApp.appLifecycleObserver")
  public static void injectAppLifecycleObserver(MyStocksApp instance,
      AppLifecycleObserver appLifecycleObserver) {
    instance.appLifecycleObserver = appLifecycleObserver;
  }
}
