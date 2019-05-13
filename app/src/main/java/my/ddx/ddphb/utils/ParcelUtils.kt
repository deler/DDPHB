package my.ddx.ddphb.utils

import android.os.Parcel

object ParcelUtils {

    fun writeBooleanToParcel(parcel: Parcel, value: Any?): Boolean {
        return writeBooleanToParcel(parcel, value != null)
    }

    fun writeBooleanToParcel(parcel: Parcel, value: Boolean): Boolean {
        parcel.writeInt(if (value) 1 else 0)
        return value
    }

    fun readBooleanFromParcel(parcel: Parcel): Boolean {
        return parcel.readInt() != 0
    }
}
