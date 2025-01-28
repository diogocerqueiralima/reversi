package com.github.group06.chelas_reversi.screens.favourites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.github.group06.chelas_reversi.DependenciesContainer
import com.github.group06.chelas_reversi.services.RealFavoritesService
import com.github.group06.chelas_reversi.ui.theme.ChelasreversiTheme

class FavoritesActivity : ComponentActivity() {

    private val viewModel by viewModels<FavoritesViewModel>(
        factoryProducer = {
            val dependenciesContainer = application as DependenciesContainer
            FavoritesViewModelFactory(
                service = RealFavoritesService(dependenciesContainer.gameDb.gameDao())
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel.loadGames()

        setContent {
            ChelasreversiTheme {
                FavoritesScreen(viewModel = viewModel, navigateToPairing = { finish() })
            }
        }

    }

}