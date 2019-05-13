package my.ddx.ddphb.repositories.models.room

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import my.ddx.ddphb.entities.ClassEntity
import my.ddx.ddphb.entities.SpellEntity
import my.ddx.ddphb.utils.DATA_FILENAME

class SeedDatabaseWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val TAG by lazy { SeedDatabaseWorker::class.java.simpleName }

    override fun doWork(): Result {
        var result: Result? = null

        fun isCanCastClass(spellEntity: SpellEntity, classEntity: ClassEntity): Boolean {
            val classIds = spellEntity.classIds
            val strings = classIds
                    .split(",".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            val id = classEntity.id
            strings.forEach {
                if (it.trim { trimIt -> trimIt <= ' ' }.equals(id, true)) {
                    return true
                }
            }
            return false
        }

        try {
            applicationContext.assets.open(DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val gson = GsonBuilder().create()
                    val firstData: JsonObject = gson.fromJson(jsonReader, JsonObject::class.java)

                    val classType = object : TypeToken<List<ClassEntity>>() {}.type
                    val classes: List<ClassEntity> = gson.fromJson(firstData.get("classes"), classType)

                    val spellsType = object : TypeToken<List<SpellEntity>>() {}.type
                    val spells: List<SpellEntity> = gson.fromJson(firstData.get("spells"), spellsType)

                    spells.forEach { spellModel ->
                        classes.forEach { classModel ->
                            if (isCanCastClass(spellModel, classModel)) {
                                spellModel.classes.add(classModel)
                            }
                        }
                    }

                    val database = AppDatabase.getInstance(applicationContext)
                    database.spellsDao().insertAll(spells)
                    database.classesDao().insertAll(classes)

                    result = Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            result = Result.failure()
        }

        return result?:Result.failure()
    }

}
