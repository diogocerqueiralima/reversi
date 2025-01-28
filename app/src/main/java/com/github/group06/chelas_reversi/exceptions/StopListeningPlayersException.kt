package com.github.group06.chelas_reversi.exceptions

import kotlinx.coroutines.CancellationException

class StopListeningPlayersException(

    override val message: String? = "Player found, stop listening players in queue"

): CancellationException(message)