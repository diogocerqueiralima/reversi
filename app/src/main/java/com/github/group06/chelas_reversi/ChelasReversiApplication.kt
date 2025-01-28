package com.github.group06.chelas_reversi

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.github.group06.chelas_reversi.storage.GameDB
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings
import com.google.firebase.firestore.firestore

interface DependenciesContainer {

    val db: FirebaseFirestore
    val preferencesDataStore: DataStore<Preferences>
    val gameDb: GameDB

}

class ChelasReversiApplication : Application(), DependenciesContainer {

    override val db: FirebaseFirestore by lazy {
        Firebase.firestore.also {
            it.useEmulator("10.0.2.2", 8080)
            it.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
                .build()
        }
    }

    override val preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

    override val gameDb: GameDB by lazy {
        Room.databaseBuilder(
            context = this,
            klass = GameDB::class.java,
            name = "game-db"
        ).build()
    }

}