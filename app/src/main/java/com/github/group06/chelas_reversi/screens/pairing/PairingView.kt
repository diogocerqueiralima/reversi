package com.github.group06.chelas_reversi.screens.pairing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.github.group06.chelas_reversi.R
import com.github.group06.chelas_reversi.components.CustomButton
import com.github.group06.chelas_reversi.components.DotsFlashing
import com.github.group06.chelas_reversi.domain.Player

const val IDLE_NAVIGATE_AUTHOR_BUTTON_TAG = "idle_navigate_author_button"
const val IDLE_IMAGE_TAG = "idle_image"
const val IDLE_PLAY_BUTTON_TAG = "idle_play_button"
const val IDLE_FAVOURITES_BUTTON_TAG = "idle_favourites_button"
const val IDLE_QUIT_BUTTON_TAG = "idle_quit_button"

const val SEARCHING_ANIMATION_TAG = "searching_animation"
const val SEARCHING_TEXT_TAG = "searching_test"
const val SEARCHING_BUTTON_TAG = "searching_button"
const val SEARCHING_PLAYER_LIST_TAG = "searching_player_list"

const val FOUND_PLAYER_TAG = "found_player"
const val FOUND_TEXT_TAG = "found_text"
const val FOUND_ANIMATION_TAG = "found_animation"

const val PLAYER_CARD_TEXT_TAG = "player_card_text"

private fun Player.testTag() = "player_${this.nick.value}"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PairingIdleView(
    modifier: Modifier = Modifier,
    onStartSearchingClick: () -> Unit,
    authorScreenIntent: () -> Unit,
    quitScreenIntent: () -> Unit,
    nickScreenIntent: () -> Unit,
    favoritesScreenIntent: () -> Unit
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                actions = {

                    IconButton(
                        onClick = authorScreenIntent,
                        modifier = Modifier.testTag(IDLE_NAVIGATE_AUTHOR_BUTTON_TAG)
                        ) {
                        Icon(Icons.Filled.Info, contentDescription = "")
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

            Image(
                modifier = Modifier
                    .height(LocalConfiguration.current.screenHeightDp.dp * 0.25f)
                    .testTag(IDLE_IMAGE_TAG),
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.board),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom)
            ) {

                CustomButton(
                    modifier = Modifier.testTag(IDLE_PLAY_BUTTON_TAG),
                    text = stringResource(R.string.idle_play_button),
                    onClick = onStartSearchingClick
                )

                CustomButton(
                    modifier = Modifier.testTag(IDLE_FAVOURITES_BUTTON_TAG),
                    text = stringResource(R.string.idle_favourite_button),
                    onClick = favoritesScreenIntent
                )

                CustomButton(
                    text = stringResource(R.string.idle_nick_text),
                    onClick = nickScreenIntent
                )

                CustomButton(
                    modifier = Modifier.testTag(IDLE_QUIT_BUTTON_TAG),
                    text = stringResource(R.string.idle_quit_button),
                    onClick = quitScreenIntent
                )

            }

        }

    }

}

@Composable
fun PairingSearchingView(
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit,
    text: String = stringResource(R.string.searching_player_text),
    enabled: Boolean = true,
    colors: ButtonColors = ButtonColors(Color.Red, Color.White, Color.Red, Color.White),
    players: List<Player> = emptyList()
) {

    Scaffold(
        modifier = modifier
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            DotsFlashing(
                modifier = Modifier.testTag(SEARCHING_ANIMATION_TAG)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    modifier = Modifier.testTag(SEARCHING_TEXT_TAG),
                    text = text,
                    fontSize = TextUnit(7f, TextUnitType.Em)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(SEARCHING_PLAYER_LIST_TAG),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(players) { player ->
                        PlayerCard(modifier = Modifier.testTag(player.testTag()), player = player)
                    }
                }

                CustomButton(
                    modifier = Modifier.testTag(SEARCHING_BUTTON_TAG),
                    text = stringResource(R.string.searching_cancel_button),
                    onClick = onCancelClick,
                    fontSize = 6f,
                    colors = colors,
                    enabled = enabled
                )

            }

        }

    }

}

@Composable
fun PairingCancelSearchingView(
    modifier: Modifier = Modifier
) {

    PairingSearchingView(
        modifier = modifier,
        text = stringResource(R.string.searching_cancelled_text),
        colors = ButtonColors(Color.Red, Color.White, Color.Gray, Color.LightGray),
        enabled = false,
        onCancelClick = {}
    )

}

@Composable
fun PairingFoundView(
    modifier: Modifier = Modifier,
    player: Player = Player("Manuel")
) {

    Scaffold(
        modifier = modifier
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                modifier = Modifier.testTag(FOUND_PLAYER_TAG),
                text = stringResource(R.string.found_player_text)
                    .replace("{playerName}", player.nick.value),
                fontSize = TextUnit(8f, TextUnitType.Em)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    modifier = Modifier.testTag(FOUND_TEXT_TAG),
                    text = stringResource(R.string.found_creating_game_text),
                    fontSize = TextUnit(7f, TextUnitType.Em)
                )

                DotsFlashing(
                    modifier = Modifier.testTag(FOUND_ANIMATION_TAG)
                )

            }

        }

    }

}

@Composable
fun PlayerCard(
    modifier: Modifier = Modifier,
    player: Player
) {

    Row(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .padding(4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = colorResource(R.color.player_card_background)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.testTag(PLAYER_CARD_TEXT_TAG),
            text = player.nick.value,
            fontSize = TextUnit(5f, TextUnitType.Em),
            fontWeight = FontWeight.Bold
        )

    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlayerCardPreview() {
    PlayerCard(player = Player("Manuel"))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PairingIdleViewPreview() {
    PairingIdleView(authorScreenIntent = {}, onStartSearchingClick = {}, quitScreenIntent = {}, nickScreenIntent = {}, favoritesScreenIntent = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PairingSearchingViewPreview() {
    PairingSearchingView(onCancelClick = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PairingFoundViewPreview() {
    PairingFoundView()
}