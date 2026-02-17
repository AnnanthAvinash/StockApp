package avinash.app.mystocks

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import avinash.app.mystocks.lifecycle.AppLifecycleObserver
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyStocksApp : Application() {

    @Inject lateinit var appLifecycleObserver: AppLifecycleObserver

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
    }
}
