package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface AsteroidDao {

    @Query("select * from asteroids_table WHERE closeApproachDate = :currentDate ORDER BY closeApproachDate")
    fun getTodayAsteroids(currentDate: String): LiveData<List<DatabaseAsteroid>>

    @Query("select * from asteroids_table WHERE closeApproachDate >= :currentDate ORDER BY closeApproachDate")
    fun getWeekAsteroids(currentDate: String): LiveData<List<DatabaseAsteroid>>

    @Query("select * from asteroids_table ORDER BY closeApproachDate")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(vararg asteroids: DatabaseAsteroid)

}

@Dao
interface PictureOfTheDayDao {

    @Query("select * from potd_table")
    fun getAllPictureOfTheDay(): LiveData<List<DatabasePictureOfTheDay>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPotd(vararg pictureOfTheDay: DatabasePictureOfTheDay)
}

@Database(entities = [DatabaseAsteroid::class, DatabasePictureOfTheDay::class], version = 1)
abstract class AsteroidsDatabase: RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val potdDao: PictureOfTheDayDao
}


// Singleton pattern to access the database through one instance
private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {

    // Thread safe
    synchronized(AsteroidsDatabase::class.java) {
        // Kotlin check for if the variable has been initialized already, if not then create it
        if(!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidsDatabase::class.java,
                "maindb").build()
        }
    }

    return INSTANCE
}