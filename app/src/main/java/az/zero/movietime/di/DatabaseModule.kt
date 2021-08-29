package az.zero.movietime.di

import android.app.Application
import androidx.room.Room
import az.zero.movietime.db.ShowDatabase
import az.zero.movietime.utils.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(
        app: Application
    ) = Room.databaseBuilder(app, ShowDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideTaskDao(db: ShowDatabase) = db.showDao()
}