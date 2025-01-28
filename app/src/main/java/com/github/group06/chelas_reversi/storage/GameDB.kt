package com.github.group06.chelas_reversi.storage

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.github.group06.chelas_reversi.domain.Game
import com.github.group06.chelas_reversi.domain.Nick
import com.github.group06.chelas_reversi.domain.Piece
import com.github.group06.chelas_reversi.domain.Play
import com.github.group06.chelas_reversi.domain.Player
import com.github.group06.chelas_reversi.domain.Slot
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DateTimeConverter {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime): String {
        return date.format(formatter)
    }

    @TypeConverter
    fun toLocalDateTime(dateString: String): LocalDateTime {
        return LocalDateTime.parse(dateString, formatter)
    }
}

class PieceConverter {

    @TypeConverter
    fun fromPiece(value: Piece): String {
        return value.name
    }

    @TypeConverter
    fun toPiece(value: String): Piece {
        return Piece.valueOf(value)
    }

}


@Entity(tableName = "game")
data class GameEntity(

    @PrimaryKey
    val gameId: String,

    val date: LocalDateTime,

    @Embedded(prefix = "_me")
    val me: PlayerEntity,

    @Embedded(prefix = "_opponent")
    val opponent: PlayerEntity

)

data class PlayerEntity(

    @Embedded
    val nick: NickEntity,

    val piece: Piece

)

data class NickEntity(

    val nickId: String,

    val value: String

)

@Entity(tableName = "play")
data class PlayEntity(

    @PrimaryKey(autoGenerate = true)
    val playId: Long = 0,

    val gameCreatorId: String,

    @Embedded
    val player: PlayerEntity?

)

data class GameWithPlays(

    @Embedded
    val game: GameEntity,

    @Relation(
        parentColumn = "gameId",
        entityColumn = "gameCreatorId"
    )
    val plays: List<PlayEntity>

)

@Entity(tableName = "slot")
data class SlotEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val playCreatorId: Long,

    val x: Int,

    val y: Int,

    val piece: Piece

)

data class PlayWithSlots(

    @Embedded
    val play: PlayEntity,

    @Relation(
        parentColumn = "playId",
        entityColumn = "playCreatorId"
    )
    val slots: List<SlotEntity>

)

@Dao
interface GameDao {

    @Transaction
    @Query("SELECT * FROM game")
    suspend fun getAllGameWithPlays(): List<GameWithPlays>

    @Transaction
    @Query("SELECT * FROM game WHERE gameId = :gameId")
    suspend fun getGameById(gameId: String): GameEntity

    @Transaction
    @Query("SELECT * FROM play WHERE gameCreatorId = :gameId")
    suspend fun getAllPlayWithSlots(gameId: String): List<PlayWithSlots>

    @Insert
    suspend fun insertGame(game: GameEntity)

    @Insert
    suspend fun insertPlay(play: PlayEntity): Long

    @Insert
    suspend fun insertSlot(slot: SlotEntity)

    @Delete
    suspend fun deleteGame(game: GameEntity)

    @Delete
    suspend fun deletePlay(play: PlayEntity)

    @Delete
    suspend fun deleteSlot(slot: SlotEntity)

}

@Database(
    entities = [GameEntity::class, PlayEntity::class, SlotEntity::class],
    version = 1
)
@TypeConverters(PieceConverter::class, DateTimeConverter::class)
abstract class GameDB : RoomDatabase() {

    abstract fun gameDao(): GameDao

}

fun Game.toGameEntity() = GameEntity(
    gameId = this.id,
    date = this.date,
    me = this.me.toPlayerEntity(),
    opponent = this.opponent.toPlayerEntity()
)

fun Game.toGameWithPlays() = GameWithPlays(
    game = this.toGameEntity(),
    plays = this.plays.map { it.toPlayEntity(this.id) }
)

fun Play.toPlayEntity(gameId: String) = PlayEntity(
    gameCreatorId = gameId,
    player = this.player?.toPlayerEntity()
)

fun Player.toPlayerEntity() = PlayerEntity(
    nick = this.nick.toNickEntity(),
    piece = this.piece
)

fun Nick.toNickEntity() = NickEntity(
    nickId = this.id,
    value = this.value
)

fun Slot.toSlotEntity(playId: Long) = SlotEntity(
    playCreatorId = playId,
    x = this.position.x,
    y = this.position.y,
    piece = this.piece
)