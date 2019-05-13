package my.ddx.ddphb

import android.app.Application
import com.orhanobut.logger.LogLevel
import com.orhanobut.logger.Logger
import my.ddx.ddphb.dagger.components.AppComponent
import my.ddx.ddphb.dagger.components.DaggerAppComponent
import my.ddx.ddphb.dagger.modules.AppModule

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        Logger.init(getString(R.string.app_name))
                .methodCount(10)
                .logLevel(if (BuildConfig.DEBUG)
                    LogLevel.FULL
                else
                    LogLevel.NONE)

        appComponent = buildAppComponent()
    }

    private fun buildAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .appModule(AppModule(applicationContext))
                .build()
    }
}
