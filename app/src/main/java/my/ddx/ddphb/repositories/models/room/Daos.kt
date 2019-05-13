package my.ddx.ddphb.repositories.models.room

import android.text.TextUtils
import androidx.room.*
import io.reactivex.Flowable
import my.ddx.ddphb.entities.ClassEntity
import my.ddx.ddphb.entities.SpellEntity
import my.ddx.ddphb.entities.common.SpellFilterEntity


interface Daos {
    @Dao
    interface Classes {

        @Query("SELECT * FROM classes ORDER BY name")
        fun getClasses(): Flowable<List<ClassEntity>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertAll(spells: List<ClassEntity>)
    }

    @Dao
    interface Spells {

        @RawQuery
        fun getSpells(query: String): Flowable<List<SpellEntity>>

        @Query("SELECT * FROM spells ORDER BY level, name")
        fun getSpells(): Flowable<List<SpellEntity>>

        @Query("SELECT * FROM spells WHERE id = :spellId")
        fun getSpell(spellId: String): Flowable<SpellEntity>

        @Query("SELECT DISTINCT school FROM spells ORDER BY school")
        fun getSchools(): Flowable<List<String>>

        fun getSpells(aFilter: SpellFilterEntity?): Flowable<List<SpellEntity>> {
            aFilter?.let { filter: SpellFilterEntity ->
                val queryBuilder = StringBuilder("SELECT * FROM spells")
                var where = false;
                if (filter.name?.isNotEmpty() == true) {
                    if (!where) queryBuilder.append(" WHERE")
                    else queryBuilder.append(" AND")
                    where = true

                    queryBuilder.append(" LOWER(name) like LOWER('%${filter.name}%')")
                }

                if (filter.classIds.isNotEmpty()) {
                    if (!where) queryBuilder.append(" WHERE")
                    else queryBuilder.append(" AND")
                    where = true

                    var noFirst = false
                    filter.classIds.forEach {
                        if (noFirst) queryBuilder.append(" AND")
                        queryBuilder.append(" LOWER(classIds) like LOWER('%$it%')")
                        noFirst = true
                    }
                }

                if (filter.schools.isNotEmpty()) {
                    if (!where) queryBuilder.append(" WHERE")
                    else queryBuilder.append(" AND")
                    where = true

                    val schools = TextUtils.join(", ", filter.schools)
                    queryBuilder.append(" LOWER(school) in LOWER($schools)")
                }

                if (!where) queryBuilder.append(" WHERE")
                else queryBuilder.append(" AND level BETWEEN ${filter.minLevel} AND ${filter.maxLevel}")

                queryBuilder.append(" ORDER BY level, name")
                return getSpells(queryBuilder.toString())
            }
            return getSpells()
        }

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertAll(spells: List<SpellEntity>)
    }
}