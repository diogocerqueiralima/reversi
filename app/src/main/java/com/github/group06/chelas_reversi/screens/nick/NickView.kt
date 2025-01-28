package com.github.group06.chelas_reversi.screens.nick

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.github.group06.chelas_reversi.R
import com.github.group06.chelas_reversi.components.CustomButton
import com.github.group06.chelas_reversi.screens.WaitingView

const val OK_BUTTON_TAG = "ok_button"
const val CANCEL_BUTTON_TAG = "cancel_button"

@Composable
fun NickLoadingView(
    modifier: Modifier = Modifier
) = WaitingView(modifier, stringResource(R.string.nick_loading))

@Composable
fun NickSavingView(
    modifier: Modifier = Modifier
) = WaitingView(modifier, stringResource(R.string.nick_saving))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NickDisplayingView(
    modifier: Modifier = Modifier,
    nickname: String,
    onNicknameChange: (String) -> Unit,
    isEditing: Boolean,
    onOkClick: () -> Unit,
    onCancelClick: () -> Unit
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                TextField(
                    modifier = Modifier
                        .testTag("nickname_${nickname}"),
                    value = nickname,
                    onValueChange = onNicknameChange,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = TextUnit(6f, TextUnitType.Em),
                        fontWeight = FontWeight.Bold
                    ),
                    placeholder = { Text(stringResource(R.string.nick_text_field_palceholder)) }
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    CustomButton(
                        modifier = Modifier.testTag(OK_BUTTON_TAG),
                        text = stringResource(R.string.nick_ok),
                        enabled = isEditing,
                        onClick = onOkClick
                    )

                    CustomButton(
                        modifier = Modifier.testTag(CANCEL_BUTTON_TAG),
                        text = stringResource(R.string.nick_cancel),
                        onClick = onCancelClick
                    )

                }

            }

        }

    }

}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun NickDisplayingPreview() {
    NickDisplayingView(nickname = "Carlos", onNicknameChange = {}, isEditing = true, onOkClick = {}, onCancelClick = {})
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun NickLoadingPreview() {
    NickLoadingView()
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun NickSavingPreview() {
    NickSavingView()
}