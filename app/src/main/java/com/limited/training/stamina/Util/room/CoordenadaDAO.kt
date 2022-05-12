package com.limited.training.stamina.Util.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.limited.training.stamina.objects.Coordenada

@Dao
interface CoordenadaDAO {
    @Query("SELECT * FROM coordenadas")
    suspend fun getAll(): List<Coordenada>

//    @Query("SELECT * FROM coordenada WHERE coordinateId IN (:coordIds)")
//    fun loadAllByIds(coordIds: IntArray): List<Coordenada>

    @Insert
    suspend fun insert(vararg coordenada: Coordenada)

//    @Delete
//    fun delete(coordenada: Coordenada)
}