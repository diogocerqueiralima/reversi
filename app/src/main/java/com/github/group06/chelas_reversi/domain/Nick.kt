package com.github.group06.chelas_reversi.domain

import java.util.UUID

private const val MIN_SIZE = 3
private const val MAX_SIZE = 20

data class Nick(
    val id: String = UUID.randomUUID().toString(),
    val value: String
) {

    init {
        require(isNickValid(value))
    }

}

fun isNickValid(value: String) = value.isNotBlank() && value.length in MIN_SIZE..MAX_SIZE
