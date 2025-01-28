package com.github.group06.chelas_reversi

import com.github.group06.chelas_reversi.domain.Nick
import org.junit.Test

class NickTest {

    @Test(expected = IllegalArgumentException::class)
    fun `nick with insufficient length should throws`() {
        Nick(value = "a")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `nick with larger size should throws`() {
        Nick(value = "asdasdasdasdasdasdasdasdasd")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `blank nick should throws`() {
        Nick(value = "       ")
    }

    @Test
    fun `creating a nickname with all the requirements succeeds`() {
        Nick(value = "João")
    }

}