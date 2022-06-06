package com.limited.training.stamina.objects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coordenadas")
data class Coordenada(
    @ColumnInfo(name = "longitude") var longitude: Double,
    @ColumnInfo(name = "latitude") var latitude: Double
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="coordinateId")
    var coordId: Int = 0
}