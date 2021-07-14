package com.jugal.trainingsample.utils

import org.junit.Assert

infix fun Any?.shouldBe(other: Any?) = Assert.assertEquals(other, this)

infix fun Any?.shouldNotBe(other: Any?) = Assert.assertNotEquals(other, this)

fun success(msg: String? = null) = if (msg != null) Assert.assertTrue(
    msg,
    true
) else Assert.assertTrue(true)