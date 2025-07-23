package seid.fakeapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import seid.fakeapi.model.favorite.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
