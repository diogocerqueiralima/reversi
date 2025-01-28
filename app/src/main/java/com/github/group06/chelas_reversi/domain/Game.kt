package com.github.group06.chelas_reversi.domain

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Game(

    val id: String = UUID.randomUUID().toString(),
    val date: LocalDateTime = LocalDateTime.now(),
    val me: Player,
    val opponent: Player,
    val plays: List<Play> = listOf(
        Play(player = null, slots = List(8 * 8) {
            val x = (it/8) + 1
            val y = (it%8) + 1
            Slot(position = Position(x, y))
        })
    ),
    val gameOver: Boolean = false,
    val pass: Boolean = false

) {

    fun slotsToRender(play: Play) =
        play.slots.chunked(8)

    val formattedDate: String
        get() {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")
            return date.format(formatter)
        }

    fun getMyPoints(play: Play = plays.last()): Int =
        play.slots.count { it.piece == me.piece }

    fun getOpponentPoints(play: Play = plays.last()): Int =
        play.slots.count { it.piece == opponent.piece }

    fun getWinner(): Player?{

        val myPoints = getMyPoints()
        val opponentPoints = getOpponentPoints()

        return if (myPoints > opponentPoints) me
        else if (opponentPoints > myPoints) opponent
        else null

    }

}
