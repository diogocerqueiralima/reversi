package com.github.group06.chelas_reversi.services

import com.github.group06.chelas_reversi.domain.Game
import com.github.group06.chelas_reversi.domain.Nick
import com.github.group06.chelas_reversi.domain.Play
import com.github.group06.chelas_reversi.domain.Player
import com.github.group06.chelas_reversi.domain.Position
import com.github.group06.chelas_reversi.domain.Slot
import com.github.group06.chelas_reversi.storage.GameDao
import com.github.group06.chelas_reversi.storage.NickEntity
import com.github.group06.chelas_reversi.storage.PlayerEntity
import com.github.group06.chelas_reversi.storage.SlotEntity
import kotlinx.coroutines.delay

interface FavoritesService {

    suspend fun loadGames(): List<Game>
    suspend fun removeFromFavorites(game: Game)

}

class FakeFavoritesService : FavoritesService{
    override suspend fun loadGames(): List<Game> {
        delay(1000L)
        return listOf(Game(me = Player("Vais perder"), opponent = Player("Vou ganhar")))
    }

    override suspend fun removeFromFavorites(game: Game) {

    }

}

class RealFavoritesService(

    private val gameDao: GameDao

) : FavoritesService {

    override suspend fun loadGames(): List<Game> {

        val gamesWithPlays = gameDao.getAllGameWithPlays()

        return gamesWithPlays.map { gameWithPlay ->

            val gameEntity = gameWithPlay.game
            val playsWithSlots = gameDao.getAllPlayWithSlots(gameEntity.gameId)

            Game(
                id = gameEntity.gameId,
                date = gameEntity.date,
                me = gameEntity.me.toPlayer(),
                opponent = gameEntity.opponent.toPlayer(),
                plays = playsWithSlots.map { playWithSlot ->

                    val playEntity = playWithSlot.play

                    Play(
                        player = playEntity.player?.toPlayer(),
                        slots = playWithSlot.slots.map { slotEntity -> slotEntity.toSlot() }
                    )
                }
            )

        }
    }

    override suspend fun removeFromFavorites(game: Game) {

        val gameEntity = gameDao.getGameById(game.id)
        val playsWithSlots = gameDao.getAllPlayWithSlots(gameEntity.gameId)

        playsWithSlots.forEach { playWithSlot ->

            playWithSlot.slots.forEach { slotEntity ->
                gameDao.deleteSlot(slotEntity)
            }

            gameDao.deletePlay(playWithSlot.play)
        }

        gameDao.deleteGame(gameEntity)
    }

}

private fun PlayerEntity.toPlayer() = Player(
    nick = this.nick.toNick(),
    piece = this.piece
)

private fun NickEntity.toNick() = Nick(
    id = this.nickId,
    value = this.value
)

private fun SlotEntity.toSlot() = Slot(
    position = Position(x = this.x, y = this.y),
    piece = this.piece
)