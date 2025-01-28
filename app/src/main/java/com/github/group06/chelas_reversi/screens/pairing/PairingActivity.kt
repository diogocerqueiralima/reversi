package com.github.group06.chelas_reversi.screens.pairing

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.github.group06.chelas_reversi.DependenciesContainer
import com.github.group06.chelas_reversi.infrastructure.NickRepositoryImpl
import com.github.group06.chelas_reversi.screens.author.AuthorActivity
import com.github.group06.chelas_reversi.screens.favourites.FavoritesActivity
import com.github.group06.chelas_reversi.screens.game.GameActivity
import com.github.group06.chelas_reversi.screens.nick.NickActivity
import com.github.group06.chelas_reversi.services.GameService
import com.github.group06.chelas_reversi.services.RealGameService
import com.github.group06.chelas_reversi.services.RealPairingService
import com.github.group06.chelas_reversi.ui.theme.ChelasreversiTheme

class PairingActivity : ComponentActivity() {

    private val viewModel by viewModels<PairingViewModel>(
        factoryProducer = {
            val dependenciesContainer = application as DependenciesContainer
            PairingViewModelFactory(
                service = RealPairingService(dependenciesContainer.db),
                repository = NickRepositoryImpl(dependenciesContainer.preferencesDataStore)
            )
        }
    )

    private val navigateToAuthorIntent: Intent by lazy {
        Intent(this, AuthorActivity::class.java)
    }

    private val navigateToNickIntent: Intent by lazy {
        Intent(this, NickActivity::class.java)
    }

    private val navigateToFavoritesIntent: Intent by lazy {
        Intent(this, FavoritesActivity::class.java)
    }

    private fun navigateToGameIntent(gameId: String): Intent {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("game_id", gameId)
        return intent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChelasreversiTheme {
                PairingScreen(
                    viewModel = viewModel,
                    authorScreenIntent = { startActivity(navigateToAuthorIntent) },
                    nickScreenIntent = { startActivity(navigateToNickIntent) },
                    quitScreenIntent = { finish() },
                    favoritesScreenIntent = { startActivity(navigateToFavoritesIntent) },
                    gameIntent = { startActivity(navigateToGameIntent(it)) }
                )
            }
        }
    }

}