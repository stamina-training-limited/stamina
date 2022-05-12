package com.limited.training.stamina.Util.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.limited.training.stamina.R
import com.limited.training.stamina.objects.Coordenada


@Database(entities = [Coordenada::class], version = 1)
abstract class CoordenadaDB : RoomDatabase() {
    abstract fun coordenadaDao(): CoordenadaDAO

    companion object {
        private const val DATABASE_NAME = "coordenadas_database" //R.string.coordenadas_database.toString()
        @Volatile
        private var INSTANCE: CoordenadaDB? = null

        fun getInstance(context: Context): CoordenadaDB? {
            INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    CoordenadaDB::class.java,
                    DATABASE_NAME
                ).build()
            }
            return INSTANCE
        }
    }
}