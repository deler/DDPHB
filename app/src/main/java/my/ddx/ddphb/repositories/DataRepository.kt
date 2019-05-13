package my.ddx.ddphb.repositories

import android.content.Context
import my.ddx.ddphb.repositories.models.room.AppDatabase
import my.ddx.ddphb.repositories.models.room.Daos

class DataRepository(private val context: Context, private val settingsRepository: SettingsRepository) {

    val spellsDao: Daos.Spells
    val classesDao: Daos.Classes

    init {
        val database = AppDatabase.getInstance(context)

        spellsDao = database.spellsDao()
        classesDao = database.classesDao()
    }
}
