package com.github.group06.chelas_reversi.favourites

import com.github.group06.chelas_reversi.domain.Game
import com.github.group06.chelas_reversi.domain.Play
import com.github.group06.chelas_reversi.domain.Player
import com.github.group06.chelas_reversi.screens.favourites.FavoriteState
import com.github.group06.chelas_reversi.screens.favourites.FavoritesViewModel
import com.github.group06.chelas_reversi.services.FakeFavoritesService
import com.github.group06.chelas_reversi.utils.ReplaceMainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class FavouritesViewModelTest {

    @get:Rule
    val dispatcherRule = ReplaceMainDispatcherRule()

    @Test
    fun initial_state_should_be_loading() {

        val viewModel = FavoritesViewModel(
            service = FakeFavoritesService()
        )

        assert(viewModel.state.value is FavoriteState.Loading){
            "Expected state is loading but was ${viewModel.state.value}"
        }

    }

    @Test
    fun call_load_games_when_state_is_not_loading() {

        val viewModel = FavoritesViewModel(
            service = FakeFavoritesService(),
            initialState = FavoriteState.Deleting
        )

        assert(viewModel.state.value is FavoriteState.Deleting){
            "Expected state is Deleting but was ${viewModel.state.value}"
        }

    }

    @Test
    fun after_call_load_games_should_transit_to_listing_state() = runTest{

        val viewModel = FavoritesViewModel(
            service = FakeFavoritesService()
        )

        viewModel.loadGames()?.join()

        assert(viewModel.state.value is FavoriteState.Listing){
            "Expected state is Listing but was ${viewModel.state.value}"
        }
    }

    @Test
    fun on_remove_game_from_favourites_when_state_is_not_listing() = runTest{

        val viewModel = FavoritesViewModel(
            service = FakeFavoritesService()
        )

        viewModel.removeFromFavorites(Game(me = Player("Mihail"), opponent = Player("Diogo")))?.join()

        assert(viewModel.state.value is FavoriteState.Loading){
            "Expected state is Loading but was ${viewModel.state.value}"
        }

    }

    @Test
    fun on_remove_game_from_favourites_should_transit_to_deleting_state() = runTest{

        val viewModel = FavoritesViewModel(
            service = FakeFavoritesService(),
            initialState = FavoriteState.Listing(emptyList())
        )

        viewModel.removeFromFavorites(Game(me = Player("Mihail"), opponent = Player("Diogo")))

        assert(viewModel.state.value is FavoriteState.Deleting){
            "Expected state is Deleting but was ${viewModel.state.value}"
        }

    }

    @Test
    fun on_remove_game_from_favourites_should_transit_to_listing_state_after_remove() = runTest{

        val viewModel = FavoritesViewModel(
            service = FakeFavoritesService(),
            initialState = FavoriteState.Listing(emptyList())
        )

        viewModel.removeFromFavorites(Game(me = Player("Mihail"), opponent = Player("Diogo")))?.join()

        assert(viewModel.state.value is FavoriteState.Listing){
            "Expected state is Listing but was ${viewModel.state.value}"
        }

    }

    @Test
    fun call_start_replay_when_state_is_not_listing() {

        val viewModel = FavoritesViewModel(
            service = FakeFavoritesService()
        )

        viewModel.startReplay(Game(me = Player("Diogo"), opponent = Player("Mihail")))

        assert(viewModel.state.value is FavoriteState.Loading){
            "Expected state is Loading but was ${viewModel.state.value}"
        }

    }

    @Test
    fun start_replay_should_transit_to_replay_state() {

        val viewModel = FavoritesViewModel(
            service = FakeFavoritesService(),
            initialState = FavoriteState.Listing(emptyList())
        )

        viewModel.startReplay(Game(me = Player("Diogo"), opponent = Player("Mihail")))

        assert(viewModel.state.value is FavoriteState.Replay){
            "Expected state is Replay but was ${viewModel.state.value}"
        }

    }

    @Test
    fun call_back_when_state_is_not_replay() {

        val viewModel = FavoritesViewModel(
            service = FakeFavoritesService()
        )

        viewModel.back()

        assert(viewModel.state.value is FavoriteState.Loading){
            "Expected state is Loading but was ${viewModel.state.value}"
        }

    }

    @Test
    fun call_back_should_transit_to_loading_state() {

        val viewModel = FavoritesViewModel(
            service = FakeFavoritesService(),
            initialState = FavoriteState.Replay(Game(me = Player("Mihail"), opponent = Player("Diogo")), play = Play(null, emptyList()), 0)
        )

        viewModel.back()

        assert(viewModel.state.value is FavoriteState.Loading){
            "Expected state is Loading but was ${viewModel.state.value}"
        }

    }

    @Test
    fun call_back_should_transit_to_listing_state_when_load_games() = runTest {

        val viewModel = FavoritesViewModel(
            service = FakeFavoritesService(),
            initialState = FavoriteState.Replay(Game(me = Player("Mihail"), opponent = Player("Diogo")), play = Play(null, emptyList()), 0)
        )

        viewModel.back()?.join()

        assert(viewModel.state.value is FavoriteState.Listing){
            "Expected state is Listing but was ${viewModel.state.value}"
        }

    }

}