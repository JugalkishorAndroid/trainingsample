@file:Suppress("unused")
package com.jugal.trainingsample

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.jugal.trainingsample.di.DB_MODULE
import com.jugal.trainingsample.di.NETWORKING_MODULE
import com.jugal.trainingsample.di.REPOSITORY_MODULE
import com.jugal.trainingsample.di.VIEW_MODEL_MODULE
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyTrainingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyTrainingApplication)
            loadKoinModules(REQUIRED_MODULE)
        }

    }

    companion object {
        val REQUIRED_MODULE = listOf(
            NETWORKING_MODULE,
            REPOSITORY_MODULE,
            VIEW_MODEL_MODULE,
            DB_MODULE
        )

        private var app: MyTrainingApplication? = null
        fun app(): MyTrainingApplication = app!!

        fun isNetworkConnected(): Boolean {
            val connectivityManager = app!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo != null
        }
    }
}