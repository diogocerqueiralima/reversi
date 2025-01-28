package com.github.group06.chelas_reversi.screens.favourites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.github.group06.chelas_reversi.R
import com.github.group06.chelas_reversi.components.RoundedIconButton
import com.github.group06.chelas_reversi.domain.Game
import com.github.group06.chelas_reversi.domain.Piece
import com.github.group06.chelas_reversi.domain.Play
import com.github.group06.chelas_reversi.domain.Player
import com.github.group06.chelas_reversi.screens.game.Board

const val BACK_TAG = "back"

const val GAME_CARD_FAVOURITE_TAG = "game_card_favourite"
const val GAME_CARD_PLAY_TAG = "game_card_play"

const val REPLAY_BACK_TAG = "replay_back_button"
const val REPLAY_FORWARD_TAG = "replay_forward_button"

fun Game.toMyPointsTag(play: Play) = "points_${this.getMyPoints(play)}"
fun Game.toOpponentPointsTag(play: Play) = "points_${this.getOpponentPoints(play)}"

fun Game.playersTag() = "game_${this.me.nick.value}-${this.opponent.nick.value}"
fun Game.dateTag() = "game_${this.formattedDate}"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplayView(
    modifier: Modifier = Modifier,
    game: Game,
    play: Play,
    previousPlay: () -> Unit,
    nextPlay: () -> Unit,
    backIntent: () -> Unit
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                navigationIcon = {

                    IconButton(
                        modifier = Modifier.testTag(BACK_TAG),
                        onClick = backIntent
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "")
                    }

                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    modifier = Modifier.testTag(game.toMyPointsTag(play)),
                    text = stringResource(R.string.favorites_player_points)
                        .replace("{player}", game.me.nick.value)
                        .replace("{points}", game.getMyPoints(play).toString()),
                    fontSize = TextUnit(5f, TextUnitType.Em)
                )

                Text(
                    modifier = Modifier.testTag(game.toOpponentPointsTag(play)),
                    text = stringResource(R.string.favorites_player_points)
                        .replace("{player}", game.opponent.nick.value)
                        .replace("{points}", game.getOpponentPoints(play).toString()),
                    fontSize = TextUnit(5f, TextUnitType.Em)
                )

            }

            Board(
                game = game,
                play = play
            ) { }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                RoundedIconButton(
                    modifier = Modifier.testTag(REPLAY_BACK_TAG),
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    backgroundColor = Color.Gray,
                    onClick = previousPlay
                )

                val player = play.player

                if (player != null) {

                    Text(
                        text = stringResource(R.string.favorites_player_turn)
                            .replace("{player}", player.nick.value),
                        fontSize = TextUnit(5f, TextUnitType.Em),
                        fontWeight = FontWeight.Bold
                    )

                }else {
                    Text(
                        text = stringResource(R.string.favorites_first_play),
                        fontSize = TextUnit(5f, TextUnitType.Em),
                        fontWeight = FontWeight.Bold
                    )
                }

                RoundedIconButton(
                    modifier = Modifier.testTag(REPLAY_FORWARD_TAG),
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    backgroundColor = Color.Gray,
                    onClick = nextPlay
                )

            }

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesView(
    modifier: Modifier = Modifier,
    backIntent: () -> Unit,
    removeFromFavoritesIntent: (Game) -> Unit,
    startReplay: (Game) -> Unit,
    games: List<Game>
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                navigationIcon = {

                    IconButton(
                        modifier = Modifier.testTag(BACK_TAG),
                        onClick = backIntent
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "")
                    }

                }
            )
        }
    ) { innerPadding ->

        if (games.isEmpty()) {

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(R.string.favorites_no_games),
                    fontSize = TextUnit(5f, TextUnitType.Em),
                    fontWeight = FontWeight.Bold
                )

            }

        }else {

            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(games) { game ->
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    GameCard(game, removeFromFavoritesIntent, startReplay)
                }
            }

        }

    }

}

@Composable
fun GameCard(
    game: Game,
    removeFromFavoritesIntent: (Game) -> Unit,
    startReplay: (Game) -> Unit
) {

    Row(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth(0.95f)
            .background(colorResource(R.color.game_card_background)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        Column(
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier.testTag(game.playersTag()),
                    text = stringResource(R.string.favorites_players)
                        .replace("{me}", game.me.nick.value)
                        .replace("{opponent}", game.opponent.nick.value),
                    fontSize = TextUnit(4f, TextUnitType.Em),
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                modifier = Modifier.testTag(game.dateTag()),
                text = game.formattedDate,
                fontSize = TextUnit(3f, TextUnitType.Em)
            )

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            RoundedIconButton(
                modifier = Modifier.padding(vertical = 8.dp).testTag(GAME_CARD_PLAY_TAG),
                backgroundColor = colorResource(R.color.game_card_play_color),
                imageVector = Icons.Filled.PlayArrow,
                onClick = { startReplay(game) }
            )

            RoundedIconButton(
                modifier = Modifier.padding(vertical = 8.dp).testTag(GAME_CARD_FAVOURITE_TAG),
                backgroundColor = colorResource(R.color.game_card_favourite_color),
                imageVector = Icons.Filled.Favorite,
                onClick = { removeFromFavoritesIntent(game) }
            )

        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameCardPreview() {
    GameCard(Game(id = "1", me = Player("Manuel"), opponent = Player("Joao")), removeFromFavoritesIntent = {}, startReplay = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FavouritesViewPreview() {
    FavoritesView(backIntent = {}, removeFromFavoritesIntent = {}, startReplay = {}, games = (1..10).map { Game(id = it.toString(), me = Player("Joao"), opponent = Player("Manuel")) })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReplayPreview() {

    val game = Game(me = Player(nickname = "Diogo", piece = Piece.BLACK), opponent = Player(nickname = "Mihail", piece = Piece.WHITE))

    ReplayView(
        game = game,
        play = game.plays.last(),
        previousPlay = {},
        nextPlay = {},
        backIntent = {}
    )

}