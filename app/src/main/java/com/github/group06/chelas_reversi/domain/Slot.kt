package com.github.group06.chelas_reversi.domain

data class Slot(

    val position: Position,
    val piece: Piece = Piece.NONE

)

data class Position(

    val x: Int,
    val y: Int

)

enum class Piece {

    BLACK,
    WHITE,
    NONE

}