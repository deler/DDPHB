package my.ddx.ddphb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classes")
data class ClassEntity(
        @PrimaryKey var id: String,
        var name: String,
        var description: String,
        var hitDice: Int = 0,
        var mainAbility: String? = null,
        var savingThorws: String? = null,
        var equipSkills: String? = null
)