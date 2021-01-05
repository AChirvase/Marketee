package com.rinftech.marketee.framework

import com.rinftech.marketee.data.Repository
import com.rinftech.marketee.framework.local_datasource.LocalDataSource
import com.rinftech.marketee.framework.local_datasource.LocalDataSourceImpl
import com.rinftech.marketee.framework.local_datasource.MarketeeRoomDataBase
import com.rinftech.marketee.framework.remote_datasource.MarketeeService
import com.rinftech.marketee.framework.remote_datasource.NetworkResponseHandler
import com.rinftech.marketee.framework.remote_datasource.RemoteDataSource
import com.rinftech.marketee.framework.remote_datasource.RemoteDataSourceImpl
import com.rinftech.marketee.presentation.MainActivityViewModel
import com.rinftech.marketee.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
    Dependency Injection Module
 */
val marketeeModule = module {

    //creates singleton of the database
    single { MarketeeRoomDataBase.buildDatabase(get()) }
    //creates singleton of the DataAccessObject (DAO)
    single { (get() as MarketeeRoomDataBase).marketeeDao() }

    //setup and dependencies for the MarketeeService
    single { NetworkResponseHandler() }
    single { provideLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { provideMarketeeService(get()) }

    //DataSources
    single<LocalDataSource> { LocalDataSourceImpl(get()) }
    single<RemoteDataSource> { RemoteDataSourceImpl(get()) }

    //Marketee App Repository
    single { Repository(get(), get(), get()) }

    //ViewModels
    viewModel { MainActivityViewModel(get(), get()) }

}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BODY
    return logger
}

private fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
    OkHttpClient()
        .newBuilder()
            //logging http calls and errors
        .addInterceptor(loggingInterceptor)
        .build()

private fun provideMarketeeService(retrofit: Retrofit): MarketeeService =
    retrofit.create(MarketeeService::class.java)
