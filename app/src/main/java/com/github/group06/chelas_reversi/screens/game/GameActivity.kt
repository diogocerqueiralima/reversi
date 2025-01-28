package com.github.group06.chelas_reversi.screens.game

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.github.group06.chelas_reversi.DependenciesContainer
import com.github.group06.chelas_reversi.infrastructure.NickRepositoryImpl
import com.github.group06.chelas_reversi.screens.pairing.PairingActivity
import com.github.group06.chelas_reversi.services.RealGameService
import com.github.group06.chelas_reversi.ui.theme.ChelasreversiTheme

class GameActivity : ComponentActivity() {

    private val viewModel by viewModels<GameViewModel>(
        factoryProducer = {
            val dependenciesContainer = application as DependenciesContainer
            GameViewModelFactory(
                service = RealGameService(dependenciesContainer.db, dependenciesContainer.gameDb.gameDao()),
                repository = NickRepositoryImpl(dependenciesContainer.preferencesDataStore)
            )
        }
    )

    private val navigateToPairing: Intent by lazy {
        val intent = Intent(this, PairingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val gameId = intent.getStringExtra("game_id")!!

        viewModel.loadGame(gameId) { startActivity(navigateToPairing) }

        setContent {
            ChelasreversiTheme {
                GameScreen(viewModel) { startActivity(navigateToPairing) }
            }
        }
    }

}