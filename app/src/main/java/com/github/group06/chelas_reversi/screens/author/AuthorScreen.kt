package com.github.group06.chelas_reversi.screens.author

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.github.group06.chelas_reversi.R
import com.github.group06.chelas_reversi.domain.Author
import com.github.group06.chelas_reversi.ui.theme.ChelasreversiTheme

fun Author.toTestTag() = "author_$number"

const val AUTHOR_SCREEN_TAG = "author_screen_tag"
const val IMAGE_TAG = "image_tag"
const val EMAIL_BUTTON_TAG = "button_tag"
const val BACK_TAG = "back_tag"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorScreen(
    authors: List<Author> =
        listOf(
            Author("Mihail Arcus", 50870, "A50870@alunos.isel.pt"),
            Author("Diogo Lima", 50879, "A50879@alunos.isel.pt")
        ),
        backIntent: () -> Unit,
        emailIntent: (List<Author>) -> Unit
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag(AUTHOR_SCREEN_TAG),
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
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                modifier = Modifier
                    .height(LocalConfiguration.current.screenHeightDp.dp * 0.25f)
                    .testTag(IMAGE_TAG),
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.board),
                contentDescription = null
            )

            Column {

                authors.forEach {
                    SingleAuthor(modifier = Modifier.testTag(it.toTestTag()), author = it)
                }

            }

            Button(
                modifier = Modifier.testTag(EMAIL_BUTTON_TAG),
                onClick = {
                    emailIntent(authors)
                }
            ) {
                Text(
                    text = stringResource(R.string.send_email)
                )
            }

        }

    }

}

@Composable
private fun SingleAuthor(modifier: Modifier = Modifier, author: Author) {

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${author.number} - ${author.name}",
            fontSize = TextUnit(5f, TextUnitType.Em)
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthorScreenPreview() {
    ChelasreversiTheme {
        AuthorScreen(emailIntent = {}, backIntent = {})
    }
}