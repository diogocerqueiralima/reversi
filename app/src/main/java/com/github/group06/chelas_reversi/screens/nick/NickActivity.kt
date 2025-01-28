package com.github.group06.chelas_reversi.screens.nick

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.github.group06.chelas_reversi.DependenciesContainer
import com.github.group06.chelas_reversi.infrastructure.NickRepositoryImpl
import com.github.group06.chelas_reversi.screens.pairing.PairingActivity
import com.github.group06.chelas_reversi.ui.theme.ChelasreversiTheme

class NickActivity : ComponentActivity() {

    private val viewModel by viewModels<NickViewModel>(
        factoryProducer = {
            val dependenciesContainer = application as DependenciesContainer
            NickViewModelFactory(
                repository = NickRepositoryImpl(dependenciesContainer.preferencesDataStore)
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel.load()

        setContent {
            ChelasreversiTheme {
                NickScreen(viewModel = viewModel, navigateToPairing = { finish() })
            }
        }

    }

}