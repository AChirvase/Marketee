package com.rinftech.marketee

import android.app.Application
import com.rinftech.marketee.framework.marketeeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

open class MarketeeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MarketeeApplication)
            modules(marketeeModule)
        }
    }
}