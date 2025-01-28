package com.github.group06.chelas_reversi.domain

data class Player(

    val nick: Nick,
    val piece: Piece = Piece.NONE

) {

    constructor(nickname: String, piece: Piece = Piece.NONE): this(Nick(value = nickname), piece)

}
