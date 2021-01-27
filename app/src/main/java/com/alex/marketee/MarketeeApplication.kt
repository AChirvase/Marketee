package com.alex.marketee

import android.app.Application
import com.alex.marketee.framework.marketeeModule
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