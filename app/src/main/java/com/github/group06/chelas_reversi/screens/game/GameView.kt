package com.github.group06.chelas_reversi.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.github.group06.chelas_reversi.R
import com.github.group06.chelas_reversi.components.CustomButton
import com.github.group06.chelas_reversi.components.RoundedIconButton
import com.github.group06.chelas_reversi.domain.Game
import com.github.group06.chelas_reversi.domain.Piece
import com.github.group06.chelas_reversi.domain.Play
import com.github.group06.chelas_reversi.domain.Player
import com.github.group06.chelas_reversi.domain.Position
import com.github.group06.chelas_reversi.domain.Slot
import java.time.LocalDateTime

@Composable
private fun ChooseTheColorView(
    modifier: Modifier = Modifier,
    timeout: Int,
    black: Boolean,
    white: Boolean,
    onChooseColorIntent: (Piece) -> Unit
) {

    Scaffold(
        modifier = modifier
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                Text(
                    text = stringResource(R.string.game_choose_color),
                    fontSize = TextUnit(6f, TextUnitType.Em),
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .background(Color.Green, CircleShape)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = timeout.toString(),
                        fontSize = TextUnit(5f, TextUnitType.Em)
                    )
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
            ) {

                CustomButton(
                    text = Piece.BLACK.name,
                    enabled = black,
                    onClick = { onChooseColorIntent(Piece.BLACK) }
                )

                CustomButton(
                    text = Piece.WHITE.name,
                    enabled = white,
                    onClick = { onChooseColorIntent(Piece.WHITE) }
                )

            }

        }

    }

}

@Composable
fun LoadingGameView(
    modifier: Modifier = Modifier
) = ChooseTheColorView(
    modifier = modifier,
    timeout = 0,
    onChooseColorIntent = {},
    black = false,
    white = false
)

@Composable
fun ChoosingColorView(
    modifier: Modifier = Modifier,
    player: Player,
    timeout: Int,
    opponent: Player,
    onChooseColorIntent : (Piece) -> Unit
) {

    ChooseTheColorView(
        modifier = modifier,
        timeout = timeout,
        onChooseColorIntent = onChooseColorIntent,
        black = opponent.piece != Piece.BLACK && player.piece != Piece.BLACK,
        white = opponent.piece != Piece.WHITE && player.piece != Piece.WHITE
    )

}

@Composable
fun GameFinishedView (
    modifier: Modifier = Modifier,
    game: Game,
    addToFavoriteIntent: (Game) -> Unit,
    navigateToPairingIntent: () -> Unit
){

    Scaffold(
        modifier = modifier
    ) { innerPadding ->

        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            val content = when(game.getWinner()) {
                game.me -> stringResource(R.string.text_of_game_win)
                game.opponent -> stringResource(R.string.text_of_game_lose)
                else -> stringResource(R.string.text_of_game_draw)
            }

            Text(
                text = content,
                fontSize = TextUnit(6f, TextUnitType.Em),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(R.string.game_finish_your_points)
                    .replace("{points}", game.getMyPoints().toString())
            )

            Text(
                text = stringResource(R.string.game_finish_opponent_points)
                    .replace("{points}", game.getOpponentPoints().toString())
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
            ){

                CustomButton(
                    text = stringResource(R.string.text_return_main_screen),
                    onClick = navigateToPairingIntent
                )

                RoundedIconButton(
                    imageVector = Icons.Filled.Favorite,
                    backgroundColor = Color.Gray,
                    onClick = { addToFavoriteIntent(game)  }
                )

            }

        }


    }

}

@Composable
fun GameView(
    modifier: Modifier = Modifier,
    game: Game,
    yourTurn: Boolean,
    clickable: (Slot) -> Unit
) {

    Scaffold(
        modifier = modifier
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
                    text = stringResource(R.string.game_opponent_text)
                        .replace("{player}", game.opponent.nick.value),
                    fontSize = TextUnit(5f, TextUnitType.Em)
                )

                Text(
                    text = stringResource(R.string.game_points)
                        .replace("{points}", game.getMyPoints().toString()),
                    fontSize = TextUnit(4f, TextUnitType.Em)
                )

            }

            Board(game = game, clickable = clickable)

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(if (yourTurn) R.string.game_your_turn else R.string.game_opponent_turn),
                    fontSize = TextUnit(5f, TextUnitType.Em)
                )

            }

        }

    }

}

@Composable
fun PlayingGameView(
    modifier: Modifier = Modifier,
    game: Game,
    clickable: (Slot) -> Unit
) {
    GameView(modifier = modifier, game = game, yourTurn = true, clickable)
}

@Composable
fun WaitingGameView(
    modifier: Modifier = Modifier,
    game: Game
) {
    GameView(modifier = modifier, game = game, yourTurn = false, clickable = {})
}

@Composable
fun Board(
    modifier: Modifier = Modifier,
    game: Game,
    play: Play = game.plays.last(),
    clickable: (Slot) -> Unit
) {

    Column(
        modifier = modifier
            .border(2.dp, Color.Black)
    ) {

        game.slotsToRender(play).forEach { rowOfSlots ->

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                rowOfSlots.forEach { slot ->
                    Slot(slot, clickable)
                }

            }

        }

    }

}

@Composable
private fun Slot(slot: Slot, clickable: (Slot) -> Unit) {
    val background = if ((slot.position.x + slot.position.y) % 2 == 0) {
        colorResource(R.color.board_background_1)
    } else {
        colorResource(R.color.board_background_2)
    }

    val piece = slot.piece

    val color = if (piece != Piece.NONE) {
        if (piece === Piece.BLACK) Color.Black else Color.White
    }else {
        background
    }

    Box(
        modifier = Modifier
            .size(45.dp)
            .background(background, RectangleShape)
            .clickable { }
            .background(background)
            .padding(0.dp),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(35.dp)
                .background(color, CircleShape)
                .clickable { clickable(slot) }
        )

    }

}

@Preview(showBackground = true , showSystemUi = true)
@Composable
fun GameFinishedPreview() {
    GameFinishedView(game = Game(me = Player("nick"), opponent = Player("nick2")), navigateToPairingIntent = {}, addToFavoriteIntent = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChoosingColorPreview() {
    ChoosingColorView(player = Player("Carlos"), opponent = Player("Manuel"), timeout = 10, onChooseColorIntent = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SlotPreview() {
    Slot(Slot(position = Position(2, 2), piece = Piece.BLACK), clickable = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BoardPreview() {
    Board(game = Game(date = LocalDateTime.now().minusSeconds(30), me = Player("Manuel"), opponent = Player("Joao")), clickable = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameViewPreview() {
    GameView(game = Game(date = LocalDateTime.now().minusSeconds(30), me = Player("Manuel"), opponent = Player("Joao")), yourTurn = true, clickable = {})
}