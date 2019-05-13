package my.ddx.ddphb.dagger.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import my.ddx.ddphb.repositories.DataRepository
import my.ddx.ddphb.repositories.SettingsRepository
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideDataManager(context: Context, settingsRepository: SettingsRepository): DataRepository {
        return DataRepository(context, settingsRepository)
    }
}
