package com.jugal.trainingsample.data.repository

import android.content.Context
import androidx.annotation.RawRes
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.jugal.trainingsample.data.db.TrainingDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.InputStreamReader

@ExperimentalCoroutinesApi
abstract class RepositoryTest<T> where T : Any {

    @get:Rule
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)
    protected val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var mockWebServer: MockWebServer
    protected val url: String
        get() = mockWebServer.url("/").toString()

    abstract val repository: T

    @Before
    open fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockWebServer = MockWebServer()
        mockWebServer.start()
        database.clearAllTables()
    }

    @After
    open fun tearDown() {
        mockWebServer.shutdown()
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
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

    companion object {

        lateinit var database: TrainingDatabase

        @JvmStatic
        @BeforeClass
        fun setUpDatabase() {
            database = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().targetContext,
                TrainingDatabase::class.java
            ).allowMainThreadQueries().build().also {
                it.clearAllTables()
            }
        }

        @JvmStatic
        @AfterClass
        fun tearDownDatabase() {
            database.clearAllTables()
        }

    }
}