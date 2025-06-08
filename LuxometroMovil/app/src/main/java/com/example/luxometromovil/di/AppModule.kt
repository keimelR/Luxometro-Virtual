package com.example.luxometromovil.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.luxometromovil.data.data_source.LightMeasurementDatabase
import com.example.luxometromovil.data.data_source.ScenesDatabase
import com.example.luxometromovil.data.repository.LightMeasurementRepositoryImpl
import com.example.luxometromovil.data.repository.ScenesRepositoryImpl
import com.example.luxometromovil.data.sensor.LightSensor
import com.example.luxometromovil.data.util.DataStoreUtil
import com.example.luxometromovil.domain.repository.LightMeasurementRepository
import com.example.luxometromovil.domain.repository.ScenesRepository
import com.example.luxometromovil.domain.use_case.light_history.DeleteLightMeasurement
import com.example.luxometromovil.domain.use_case.light_history.GetAllLightMeasurement
import com.example.luxometromovil.domain.use_case.light_history.GetAllLightMeasurementWithDetails
import com.example.luxometromovil.domain.use_case.light_history.InsertLightMeasurement
import com.example.luxometromovil.domain.use_case.light_history.InsertLightMeasurementDetails
import com.example.luxometromovil.domain.use_case.light_history.LightMeasurementUseCases
import com.example.luxometromovil.domain.use_case.light_history.UpdateTitleLightMeasurement
import com.example.luxometromovil.domain.use_case.scenes.GetAllScenes
import com.example.luxometromovil.domain.use_case.scenes.GetByIdScene
import com.example.luxometromovil.domain.use_case.scenes.InsertDefaultScenes
import com.example.luxometromovil.domain.use_case.scenes.ScenesUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLightMeasurementDatabase(app: Application) : LightMeasurementDatabase {
        return Room.databaseBuilder(
            app,
            LightMeasurementDatabase::class.java,
            LightMeasurementDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideScenesDatabase(app: Application): ScenesDatabase {
        return Room.databaseBuilder(
            app,
            ScenesDatabase::class.java,
            ScenesDatabase.DATABASE_NAME
        )
            .createFromAsset("database/scenes_db.db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.d("RoomDatabase", "Database created from asset")
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    Log.d("RoomDatabase", "Database opened")
                }
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideLightMeasurementRepository(db: LightMeasurementDatabase): LightMeasurementRepository {
        return LightMeasurementRepositoryImpl(db.lightMeasurementDao)
    }

    @Provides
    @Singleton
    fun provideScenesRepository(db: ScenesDatabase): ScenesRepository {
        return ScenesRepositoryImpl(db.scenesDao)
    }

    @Provides
    @Singleton
    fun provideUseCases(repository: LightMeasurementRepository): LightMeasurementUseCases {
        return LightMeasurementUseCases(
            getAllLightMeasurementWithDetails = GetAllLightMeasurementWithDetails(repository),
            getAllLightMeasurement = GetAllLightMeasurement(repository),
            deleteLightMeasurement =  DeleteLightMeasurement(repository),
            insertLightMeasurement = InsertLightMeasurement(repository),
            insertLightMeasurementDetails = InsertLightMeasurementDetails(repository),
            updateTitleLightMeasurement = UpdateTitleLightMeasurement(repository)
        )
    }

    @Provides
    @Singleton
    fun provideSceneUseCase(repository: ScenesRepository): ScenesUseCases {
        return ScenesUseCases(
            getAllScenes = GetAllScenes(repository),
            getByIdScene = GetByIdScene(repository),
            insertDefaultScenes = InsertDefaultScenes(repository)
        )
    }

    @Provides
    @Singleton
    fun provideLightSensor(@ApplicationContext context: Context): LightSensor {
        return LightSensor(context)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStoreUtil {
        return DataStoreUtil(context)
    }
}