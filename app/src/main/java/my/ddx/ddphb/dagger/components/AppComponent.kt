package my.ddx.ddphb.dagger.components

import javax.inject.Singleton

import dagger.Component
import my.ddx.ddphb.dagger.modules.AppModule
import my.ddx.ddphb.dagger.modules.DataModule
import my.ddx.ddphb.screens.main.characters.CharactersListPresenter
import my.ddx.ddphb.screens.main.spells.SpellsListPresenter
import my.ddx.ddphb.screens.spelldetail.spelldetail.SpellDetailPresenter
import my.ddx.ddphb.screens.dialogs.spellfilter.SpellFilterPresenter

@Component(modules = [AppModule::class, DataModule::class])
@Singleton
interface AppComponent {
    fun inject(presenter: CharactersListPresenter)

    fun inject(presenter: SpellsListPresenter)

    fun inject(presenter: SpellDetailPresenter)

    fun inject(presenter: SpellFilterPresenter)
}
