package com.jugal.trainingsample.utils

import androidx.annotation.RawRes
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

inline fun <reified T> Gson.fromRes(@RawRes res: Int): T {
    val type = (object : TypeToken<T>() {}).type
    val inputStreamReader = InputStreamReader(
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .resources
            .openRawResource(res)
    )
    return this.fromJson(inputStreamReader, type)
}