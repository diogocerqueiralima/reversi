package com.github.group06.chelas_reversi.screens.nick

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.github.group06.chelas_reversi.R
import com.github.group06.chelas_reversi.screens.ErrorView

const val NICK_LOADING_VIEW_TAG = "nick_loading_view"
const val NICK_SAVING_VIEW_TAG = "nick_saving_view"
const val NICK_DISPLAYING_VIEW_TAG = "nick_displaying_view"
const val NICK_ERROR_VIEW_TAG = "nick_error_view"

@Composable
fun NickScreen(
    viewModel: NickViewModel,
    navigateToPairing: () -> Unit
) {

    when (val state = viewModel.state.collectAsState().value) {
        is NickState.Loading -> NickLoadingView(
            modifier = Modifier.testTag(NICK_LOADING_VIEW_TAG)
        )
        is NickState.Saving -> NickSavingView(
            modifier = Modifier.testTag(NICK_SAVING_VIEW_TAG)
        )
        is NickState.Displaying -> NickDisplayingView(
            modifier = Modifier.testTag(NICK_DISPLAYING_VIEW_TAG),
            nickname = state.nickname,
            onNicknameChange = { viewModel.onNicknameChange(it) },
            isEditing = state.isEditing,
            onOkClick = { viewModel.onOk(nickname = state.nickname, navigateToPairing = navigateToPairing) },
            onCancelClick = { viewModel.onCancel(navigateToPairing = navigateToPairing) }
        )
        NickState.Error -> ErrorView(
            modifier = Modifier.testTag(NICK_ERROR_VIEW_TAG),
            msg = stringResource(R.string.error_saving_nickname)
        )
    }

}