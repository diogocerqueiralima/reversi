package com.github.group06.chelas_reversi.infrastructure

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.github.group06.chelas_reversi.domain.Nick
import kotlinx.coroutines.flow.first

interface NickRepository {

    suspend fun getNick(): Nick?

    suspend fun updateNick(nick: Nick)

}

class NickRepositoryImpl(private val store: DataStore<Preferences>) : NickRepository {

    private val nickKey = stringPreferencesKey("nick")
    private val idKey = stringPreferencesKey("id")

    override suspend fun getNick(): Nick? {

        val preferences = store.data.first()

        return preferences[nickKey]?.let { name ->
            preferences[idKey]?.let { id ->
                Nick(id = id, value = name)
            }
        }

    }

    override suspend fun updateNick(nick: Nick) {

        store.edit {
            it[nickKey] = nick.value
            it[idKey] = nick.id
        }

    }

}