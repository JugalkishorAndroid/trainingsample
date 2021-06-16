package com.jugal.trainingsample.di

import com.jugal.trainingsample.app.viewmodel.PeopleViewModel
import com.jugal.trainingsample.data.db.GeIdeaDatabase
import com.jugal.trainingsample.data.networking.api.PeopleApi
import com.jugal.trainingsample.data.repository.PeopleRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

val REPOSITORY_MODULE = module {
    single { PeopleRepository.create(get(), get()) }
}

val VIEW_MODEL_MODULE = module {
    viewModel { PeopleViewModel(get()) }
}

val DB_MODULE = module {
    single { GeIdeaDatabase.create(androidContext()) }
    single { get<GeIdeaDatabase>().peoplesDao() }
}

val NETWORKING_MODULE = module {

    single {
        OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }
        ).callTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<PeopleApi> {
        get<Retrofit>().create()
    }

}

private const val BASE_URL = "https://reqres.in/api/"
private const val TIME_OUT = 120L

