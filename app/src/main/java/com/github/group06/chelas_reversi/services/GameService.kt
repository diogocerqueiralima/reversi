package com.github.group06.chelas_reversi.services

import com.github.group06.chelas_reversi.domain.Game
import com.github.group06.chelas_reversi.domain.Piece
import com.github.group06.chelas_reversi.domain.Play
import com.github.group06.chelas_reversi.domain.Player
import com.github.group06.chelas_reversi.domain.Slot
import com.github.group06.chelas_reversi.dto.GameDto
import com.github.group06.chelas_reversi.dto.toDto
import com.github.group06.chelas_reversi.storage.GameDao
import com.github.group06.chelas_reversi.storage.toGameEntity
import com.github.group06.chelas_reversi.storage.toPlayEntity
import com.github.group06.chelas_reversi.storage.toSlotEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

interface GameService {

    suspend fun loadGame(gameId: String, me: Player): Game
    suspend fun chooseColor(piece: Piece, game: Game)
    suspend fun listeningChangesOfColors(game: Game): Flow<Game>
    suspend fun awaitColorChoice(game: Game): Flow<Game>
    suspend fun chooseSlot(game: Game, slot: Slot): Game?
    suspend fun awaitPlay(game: Game): Flow<Game>
    fun hasAnyPlay(game: Game): Boolean
    suspend fun addGameToFavorite(game: Game)
    suspend fun pass(game: Game): Game
    suspend fun deleteGame(game: Game)
    suspend fun ff(game: Game, player: Player): Game

}

class FakeGameService : GameService{
    override suspend fun loadGame(gameId: String, me: Player): Game {
        return Game(id = gameId, me = me, opponent = Player("Boas"))
    }

    override suspend fun chooseColor(piece: Piece, game: Game) {

    }

    override suspend fun listeningChangesOfColors(game: Game): Flow<Game> {
        return flowOf(Game(id = game.id, me = game.me, opponent = game.opponent, plays = game.plays.map { p-> p.copy(slots = game.plays.last().slots.map { it.copy(piece = Piece.BLACK) })}))
    }

    override suspend fun awaitColorChoice(game: Game): Flow<Game> {
        return flowOf(game)
    }

    override suspend fun chooseSlot(game: Game, slot: Slot): Game? {
        return null
    }

    override suspend fun awaitPlay(game: Game): Flow<Game> {
        return flowOf(game)
    }

    override fun hasAnyPlay(game: Game): Boolean {
        return false
    }

    override suspend fun addGameToFavorite(game: Game) {

    }

    override suspend fun pass(game: Game): Game {
        return game
    }

    override suspend fun deleteGame(game: Game) {

    }

    override suspend fun ff(game: Game, player: Player): Game {
        return game
    }

}

class RealGameService(

    private val db: FirebaseFirestore,
    private val gameDao: GameDao

) : GameService {

    override suspend fun loadGame(gameId: String, me: Player): Game =
        db.collection("games")
            .document(gameId)
            .get()
            .await()
            .toObject(GameDto::class.java)!!
            .toGame(me = me)

    override suspend fun chooseColor(piece: Piece, game: Game) {

        val document = db.collection("games")
            .document(game.id)

        document.set(game.toDto()).await()
    }

    override suspend fun listeningChangesOfColors(game: Game): Flow<Game> = callbackFlow {

        val document = db.collection("games")
            .document(game.id)

        val listener = document
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val newGame = snapshot?.toObject(GameDto::class.java)?.toGame(me = game.me)

                if (newGame == null) {
                    close()
                    return@addSnapshotListener
                }

                trySend(newGame)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun awaitColorChoice(game: Game) = callbackFlow {

        val document = db.collection("games")
            .document(game.id)

        val listener = document
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val dto = snapshot?.toObject(GameDto::class.java)

                if (dto == null) {
                    close()
                    return@addSnapshotListener
                }

                if (dto.players.all { it.piece != Piece.NONE }) {
                    trySend(dto.toGame(me = game.me))
                    close()
                }

            }

        awaitClose { listener.remove() }

    }

    override suspend fun chooseSlot(game: Game, slot: Slot): Game? {

        val document = db.collection("games")
            .document(game.id)

        val lastPlay = game.plays.last()
        val currentSlots = lastPlay.slots
        val currentSlotsAffected = currentSlots.count { it.piece != Piece.NONE }

        if (currentSlotsAffected < 4 && !(slot.position.x in 4..5 && slot.position.y  in 4..5))
            return null

        val affectedPieces = getAffectedPieces(game, slot)

        if (affectedPieces.isEmpty() && currentSlotsAffected >= 4)
            return null

        val newSlots = currentSlots.map { existingSlot ->
            when {
                existingSlot.position == slot.position -> existingSlot.copy(piece = game.me.piece)
                affectedPieces.any { it.position == existingSlot.position } -> existingSlot.copy(piece = game.me.piece)
                else -> existingSlot
            }
        }

        val newPlay = Play(game.me, newSlots)
        val newGame = game.copy(
            plays = listOf(*game.plays.toTypedArray(), newPlay)
        )

        val gameOver = !hasAnyPlay(newGame) && !hasAnyPlay(newGame.copy(me = game.opponent))
        val theGame = newGame.copy(gameOver = gameOver)
        document.set(theGame.toDto()).await()

        return theGame
    }



    override suspend fun awaitPlay(game: Game): Flow<Game> = callbackFlow{

        val document = db.collection("games")
            .document(game.id)

        val listener = document
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val dto = snapshot?.toObject(GameDto::class.java)

                if (dto == null) {
                    close()
                    return@addSnapshotListener
                }

                val newGame = dto.toGame(game = game, me = game.me)
                if(game.pass != newGame.pass || newGame.plays.last() != game.plays.last()) {
                    trySend(newGame)
                    close()
                }

            }

        awaitClose { listener.remove() }

    }

    override suspend fun pass(game: Game): Game {

        val document = db.collection("games")
            .document(game.id)

        val newGame = game.copy(pass = !game.pass)
        document.set(newGame.toDto()).await()

        return newGame
    }

    override fun hasAnyPlay(game: Game): Boolean {

        val lastPlay = game.plays.last()
        val currentSlots = lastPlay.slots
        val slots = currentSlots.filter { it.piece == Piece.NONE }

        if (currentSlots.count { it.piece != Piece.NONE } < 4)
            return true

        if (slots.isEmpty() || currentSlots.count { it.piece == game.me.piece } == 0)
            return false

        for (slot in slots)
            if (getAffectedPieces(game, slot).isNotEmpty())
                return true

        return false
    }

    private fun getAffectedPieces(game: Game, slot: Slot): List<Slot> {

        val lastPlay = game.plays.last()
        val currentSlots = lastPlay.slots
        val row = currentSlots.filter { it.position.x == slot.position.x }
        val piecesEqualToMineInRow = row.filter { it.position != slot.position && it.piece == game.me.piece }

        val rowAffected = piecesEqualToMineInRow.flatMap { p ->
            row.filter {
                ((it.position.y > p.position.y && it.position.y < slot.position.y) ||
                        (it.position.y < p.position.y && it.position.y > slot.position.y)) &&
                        it.piece != game.me.piece && it.piece != Piece.NONE
            }.map {
                it.copy(piece = game.me.piece)
            }
        }

        val column = currentSlots.filter { it.position.y == slot.position.y }
        val piecesEqualToMineInColumn = column.filter { it.position != slot.position && it.piece == game.me.piece }

        val columnAffected = piecesEqualToMineInColumn.flatMap { p ->
            column.filter {
                ((it.position.x > p.position.x && it.position.x < slot.position.x) ||
                        (it.position.x < p.position.x && it.position.x > slot.position.x)) &&
                        it.piece != game.me.piece && it.piece != Piece.NONE
            }.map {
                it.copy(piece = game.me.piece)
            }
        }

        return rowAffected + columnAffected
    }

    override suspend fun addGameToFavorite(game: Game) {

        gameDao.insertGame(game.toGameEntity())

        game.plays.forEach { play ->

            val playId = gameDao.insertPlay(play.toPlayEntity(game.id))

            play.slots.forEach { slot ->
                gameDao.insertSlot(slot.toSlotEntity(playId))
            }

        }

    }

    override suspend fun deleteGame(game: Game) {
        db.collection("games").document(game.id).delete().await()
    }

    override suspend fun ff(game: Game, player: Player): Game {


        val currentPlay = game.plays.last()

        val newPlay = currentPlay.copy(slots = currentPlay.slots.map {
            it.copy(piece = player.piece)
        })

        val newGame = game.copy(plays = listOf(*game.plays.toTypedArray(), newPlay), gameOver = true)

        db.collection("games").document(newGame.id).set(newGame.toDto()).await()

        return newGame
    }


}
