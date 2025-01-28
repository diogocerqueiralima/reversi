package com.github.group06.chelas_reversi.exceptions

import kotlinx.coroutines.CancellationException

class StopListeningGameChangeColorException(

    override val message: String? = "Colors choice, stop listening changes of colors"

) : CancellationException(message)