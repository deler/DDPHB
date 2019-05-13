package my.ddx.ddphb.dagger.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import my.ddx.ddphb.repositories.SettingsRepository
import javax.inject.Singleton

@Module
class AppModule(private val mContext: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return mContext
    }

    @Provides
    @Singleton
    fun provideSettingsManager(context: Context): SettingsRepository {
        return SettingsRepository(context)
    }
}
