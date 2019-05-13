package my.ddx.ddphb.repositories

import android.content.Context
import android.content.SharedPreferences

/**
 * SettingsRepository
 * Created by deler on 20.03.17.
 */

class SettingsRepository(context: Context) {

    companion object {
        private const val APP_SETTINGS_PREFS = "APP_SETTINGS_PREFS"
    }


    private val mPreferences: SharedPreferences

    init {
        mPreferences = context.getSharedPreferences(APP_SETTINGS_PREFS, 0)
    }
}
