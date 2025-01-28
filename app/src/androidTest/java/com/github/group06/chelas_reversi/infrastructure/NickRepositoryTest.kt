package com.github.group06.chelas_reversi.infrastructure

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.group06.chelas_reversi.domain.Nick
import com.github.group06.chelas_reversi.utils.CleanDataStoreRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NickRepositoryTest {

    @get:Rule
    val cleanDataStoreRule = CleanDataStoreRule()

    @Test
    fun get_nick_returns_null_when_there_is_no_nick_stored() = runTest {

        val rep = NickRepositoryImpl(cleanDataStoreRule.dataStore)
        val nick = rep.getNick()
        assert(nick == null)

    }

    @Test
    fun update_nick_stores_in_datastore() = runTest {

        val rep = NickRepositoryImpl(cleanDataStoreRule.dataStore)
        val expected = Nick(id = "the id", value = "Pedro")
        rep.updateNick(nick = expected)
        val stored = rep.getNick()
        assert(stored == expected)

    }

}