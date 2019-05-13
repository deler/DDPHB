package my.ddx.ddphb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spells")
data class SpellEntity(
        @PrimaryKey var id: String,
        var classIds: String,
        var source: String,
        var number: Int = 0,
        var name: String,
        var level: Int = 0,
        var school: String,
        var isRitual: Boolean = false,
        var castingTime: String,
        var range: String,
        var components: String? = null,
        var duration: String? = null,
        var isConcentration: Boolean = false,
        var description: String,
        var upLevel: String? = null,
        var classes: MutableList<ClassEntity> = ArrayList()
) {}
