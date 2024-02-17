package com.mielechm.zftechnicaltask.di

import com.mielechm.zftechnicaltask.data.remote.VehicleApi
import com.mielechm.zftechnicaltask.repositories.DefaultVehiclesRepository
import com.mielechm.zftechnicaltask.repositories.VehiclesRepository
import com.mielechm.zftechnicaltask.util.API_KEY
import com.mielechm.zftechnicaltask.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesVehicleApi(): VehicleApi {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request().newBuilder().header("x-api-key", API_KEY).build()
            chain.proceed(request)
        }.addInterceptor(loggingInterceptor).build()

        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL).client(okHttpClient).build().create()
    }

    @Singleton
    @Provides
    fun provideVehicleRepository(api: VehicleApi) =
        DefaultVehiclesRepository(api) as VehiclesRepository

}