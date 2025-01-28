package com.github.group06.chelas_reversi.services

import com.github.group06.chelas_reversi.domain.Game
import com.github.group06.chelas_reversi.domain.Player
import com.github.group06.chelas_reversi.dto.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

interface PairingService {

    suspend fun searchGame(player: Player): Flow<Game>
    suspend fun leaveQueue(player: Player)
    suspend fun listenPlayers(): Flow<List<Player>>
    suspend fun awaitGame(game: Game): Flow<Boolean>
    suspend fun deleteGame(game: Game)

}

class FakePairingService : PairingService {

    override suspend fun searchGame(player: Player): Flow<Game> {
        return flowOf(Game(me = Player("João"), opponent = Player("Paulo")))
    }

    override suspend fun listenPlayers(): Flow<List<Player>> {
        return flowOf(listOf(Player("João"), Player("Pedro")))
    }

    override suspend fun leaveQueue(player: Player) {

    }

    override suspend fun awaitGame(game: Game): Flow<Boolean> {
        return flowOf(true)
    }

    override suspend fun deleteGame(game: Game) {

    }

}

class RealPairingService(

    private val db: FirebaseFirestore

) : PairingService {

    override suspend fun searchGame(player: Player): Flow<Game> = callbackFlow {

        val collection = db.collection("queue")
        val documents = collection.get()
            .await()
            .documents

        if (documents.isEmpty()) {

            collection.document(player.nick.id)
                .set(player)
                .await()

            val listener = collection.document(player.nick.id)
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    if (snapshot?.data?.contains("game") == true) {

                        val dto = snapshot.toObject(PlayerChallengeDto::class.java)
                        val gamesCollection = db.collection("games")

                        runBlocking {

                            val game = gamesCollection.document(dto?.game!!)
                                .get()
                                .await()
                                .toObject(GameDto::class.java)
                                ?.toGame(me = player)!!

                            db.collection("games").document(game.id)
                                .set(game.toDto(ready = listOf(game.me.toDto(), game.opponent.toDto())))

                            trySend(game)
                        }

                        close()
                    }

                }

            awaitClose { listener.remove() }

        }else {

            val document = documents.first()
            val dto = document.toObject(PlayerDto::class.java)!!

            val game = Game(me = player, opponent = dto.toPlayer())
            db.collection("games").document(game.id)
                .set(game.toDto(ready = listOf(game.me.toDto())))

            document.reference
                .set(
                    PlayerChallengeDto(
                        nick = dto.nick!!,
                        piece = dto.piece,
                        game = game.id
                    )
                ).await()

            trySend(game)
            close()
        }

    }

    override suspend fun awaitGame(game: Game): Flow<Boolean> = callbackFlow {

        val listener = db.collection("games").document(game.id)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val dto = snapshot?.toObject(GameDto::class.java)
                if (dto?.ready?.size == 2) {
                    trySend(true)
                    close()
                }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun listenPlayers(): Flow<List<Player>> = callbackFlow {

        val listenerRegistration = db.collection("queue")
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val players = snapshot?.toObjects(PlayerDto::class.java)?.map { it.toPlayer() } ?: emptyList()

                trySend(players)
            }

        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun leaveQueue(player: Player) {
        val collection = db.collection("queue")
        collection.document(player.nick.id).delete()
    }

    override suspend fun deleteGame(game: Game) {
        db.collection("games").document(game.id).delete().await()
    }

}