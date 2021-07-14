package com.jugal.trainingsample.data.networking.api

import android.content.Context
import androidx.annotation.RawRes
import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.InputStreamReader

abstract class ApiTest<T> where T : Any {

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var mockWebServer: MockWebServer
    protected val url: String
        get() = mockWebServer.url("/").toString()
    abstract val api: T

    @Before
    open fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    open fun tearDown() {
        mockWebServer.shutdown()
    }

    protected fun mockHttpResponse(@RawRes res: Int, responseCode: Int) {
        val json = context.readRawResource(res)
        return mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(responseCode)
                .setBody(json)
        )
    }

    protected fun mockHttpResponse(body: String?, responseCode: Int) {
        return mockWebServer.enqueue(
            MockResponse().setResponseCode(responseCode)
                .apply {
                    if (body != null) {
                        this.setBody(body)
                    }
                }
        )
    }

    private fun Context.readRawResource(res: Int): String {
        val builder = StringBuilder()
        InputStreamReader(resources.openRawResource(res)).use { r ->
            r.forEachLine {
                builder.append(it)
            }
        }
        return builder.toString()
    }

    inline fun <reified T> createApi(url: String): T {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }).build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }


}