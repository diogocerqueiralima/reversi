package com.github.group06.chelas_reversi.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.github.group06.chelas_reversi.R
import com.github.group06.chelas_reversi.components.DotsFlashing

const val DEFAULT_MESSAGE_TAG = "default_message"
const val MESSAGE_TAG = "message"
const val ANIMATION_TAG = "animation"

@Composable
fun ErrorView (
    modifier: Modifier = Modifier,
    msg: String
) {

    Column (
        modifier = modifier.fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .testTag(MESSAGE_TAG)
                .padding(50.dp),
            text = msg,
            fontSize = TextUnit(6f, TextUnitType.Em)
        )

        DotsFlashing(modifier = Modifier.testTag(ANIMATION_TAG))

        Text(
            modifier = Modifier
                .testTag(DEFAULT_MESSAGE_TAG)
                .padding(10.dp),
            text = stringResource(R.string.error_default_message),
            fontSize = TextUnit(6f, TextUnitType.Em)
        )

    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ErrorPreview (){
    ErrorView(modifier = Modifier, "Error!")
}
