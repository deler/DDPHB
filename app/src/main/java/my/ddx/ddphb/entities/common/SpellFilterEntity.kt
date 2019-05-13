package my.ddx.ddphb.entities.common

import android.os.Parcel
import android.os.Parcelable
import my.ddx.ddphb.utils.ParcelUtils
import java.util.*

class SpellFilterEntity() : Parcelable {

    companion object CREATOR : Parcelable.Creator<SpellFilterEntity> {
        override fun createFromParcel(parcel: Parcel): SpellFilterEntity {
            return SpellFilterEntity(parcel)
        }

        override fun newArray(size: Int): Array<SpellFilterEntity?> {
            return arrayOfNulls(size)
        }
    }

    var name: String? = null
        set(value) {
            field = value?.toUpperCase()
        }
    var schools: Set<String> = HashSet()
    var classIds: Set<String> = HashSet()

    var minLevel = 0
        set(value) {
            field = if (value < 0) 0 else value
        }
    var maxLevel = 9
        set(value) {
            field = if (value > 9) 9 else value
        }


    private constructor(parcel: Parcel) : this() {
        if (ParcelUtils.readBooleanFromParcel(parcel)) this.name = parcel.readString()
        minLevel = parcel.readInt()
        maxLevel = parcel.readInt()

        classIds = HashSet(parcel.createStringArrayList())
        schools = HashSet(parcel.createStringArrayList())
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        if (ParcelUtils.writeBooleanToParcel(dest, this.name)) dest.writeString(this.name)
        dest.writeInt(this.minLevel)
        dest.writeInt(this.maxLevel)

        dest.writeStringList(ArrayList(classIds))
        dest.writeStringList(ArrayList(schools))
    }
}
