package com.github.group06.chelas_reversi.exceptions

import kotlinx.coroutines.CancellationException

class TimerCancelException(

    override val message: String? = "Timer timeout"

) : CancellationException(message)
