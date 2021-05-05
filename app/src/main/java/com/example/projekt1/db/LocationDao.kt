package com.example.projekt1.db

import androidx.room.*
import com.example.projekt1.models.LocationResponse

@Dao
interface LocationDao {

    @Query("SELECT * FROM LocationResponse")
    suspend fun getAllLocations(): List<LocationResponse>

    @Query("SELECT * FROM LocationResponse WHERE isFavourite=1")
    suspend fun getFavourites(): List<LocationResponse>

    @Query("SELECT * FROM LocationResponse WHERE recent > 0 ORDER BY recent DESC")
    suspend fun getRecent(): List<LocationResponse>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocation(locationResponse: LocationResponse)

    @Update
    suspend fun updateLocation(locationResponse: LocationResponse)

    @Delete
    suspend fun deleteLocation(locationResponse: LocationResponse)

    @Query("UPDATE LocationResponse SET isFavourite = 0 WHERE isFavourite = 1")
    suspend fun clearFavourites()

    @Query("UPDATE LocationResponse SET recent = 0 WHERE recent >= 0")
    suspend fun clearRecent()

    @Query("DELETE FROM LocationResponse")
    suspend fun deleteAll();
}