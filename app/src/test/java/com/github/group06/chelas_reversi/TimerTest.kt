package com.github.group06.chelas_reversi

import com.github.group06.chelas_reversi.domain.Timer
import org.junit.Assert
import org.junit.Test

class TimerTest {

    @Test
    fun `create a timer should succeeds`() {
        Timer(value = 10)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create a timer with value lower or equal to zero should fails`() {
        Timer(value = 0)
    }

    @Test
    fun `decrement a timer should returns a new timer with a new value`() {

        val expected = Timer(9)
        val actual = Timer(10).dec()

        Assert.assertEquals(expected, actual)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `decrement a timer when value it's one should fails`() {
        Timer(1).dec()
    }

}