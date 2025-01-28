package com.github.group06.chelas_reversi.dto

import com.github.group06.chelas_reversi.domain.Game
import com.github.group06.chelas_reversi.domain.Nick
import com.github.group06.chelas_reversi.domain.Piece
import com.github.group06.chelas_reversi.domain.Play
import com.github.group06.chelas_reversi.domain.Player
import com.github.group06.chelas_reversi.domain.Position
import com.github.group06.chelas_reversi.domain.Slot
import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

data class GameDto(
    val id: String? = null,
    val date: Timestamp? = null,
    val players: List<PlayerDto> = emptyList(),
    val ready: List<PlayerDto> = emptyList(),
    val lastPlay: PlayDto? = null,
    val gameOver: Boolean = false,
    val pass: Boolean = false
) {

    fun toGame(game: Game? = null, me: Player): Game {

        return if (game != null) {
            Game(
                id = this.id!!,
                date = date?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime()!!,
                me = players.first { it.toPlayer().nick.id == me.nick.id }.toPlayer(),
                opponent = players.first { it.toPlayer().nick.id != me.nick.id }.toPlayer(),
                plays = listOf(*game.plays.toTypedArray(), lastPlay?.toPlay()!!),
                gameOver = gameOver,
                pass = pass
            )
        }else {
            Game(
                id = this.id!!,
                date = date?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime()!!,
                me = players.first { it.toPlayer().nick.id == me.nick.id }.toPlayer(),
                opponent = players.first { it.toPlayer().nick.id != me.nick.id }.toPlayer(),
                gameOver = gameOver,
                pass = pass
            )
        }

    }

}

fun Game.toDto(ready: List<PlayerDto> = emptyList()) = GameDto(
    id = this.id,
    date = this.date.toTimestamp(),
    players = listOf(this.me.toDto(), this.opponent.toDto()),
    lastPlay = this.plays.last().toDto(),
    ready = ready,
    gameOver = this.gameOver,
    pass = this.pass
)

data class PlayerChallengeDto(

    val nick: NickDto? = null,
    val piece: Piece? = null,
    val game: String? = null

)

data class PlayerDto(

    val nick: NickDto? = null,
    val piece: Piece = Piece.NONE

) {

    fun toPlayer() = Player(nick = nick!!.toNick(), piece = piece)

}

class NickDto(

    val id: String? = null,
    val value: String? = null

) {

    fun toNick() = Nick(id = id!!, value = value!!)

}

fun Nick.toDto() = NickDto(
    id = this.id,
    value = this.value
)

fun Player.toDto() = PlayerDto(
    nick = this.nick.toDto(),
    piece = this.piece
)

fun LocalDateTime.toTimestamp(): Timestamp {
    val instant = this.atZone(ZoneId.systemDefault()).toInstant()
    val date = Date.from(instant)
    return Timestamp(date)
}

fun Slot.toDto() = SlotDto(position.toDto(), piece)

fun Position.toDto() = PositionDto(x, y)

data class SlotDto(
    val position: PositionDto? = null,
    val piece: Piece = Piece.NONE
){
    fun toSlot() = Slot(position!!.toPosition(), piece)
}

data class PositionDto(
    val x: Int = 0,
    val y: Int = 0
){
    fun toPosition() = Position(x, y)
}

data class PlayDto(

    val player: PlayerDto? = null,
    val slots: List<SlotDto> = emptyList()

) {

    fun toPlay() = Play(
        player = player?.toPlayer(),
        slots = slots.map { it.toSlot() }
    )

}

fun Play.toDto() = PlayDto(
    player = this.player?.toDto(),
    slots = this.slots.map { it.toDto() }
)