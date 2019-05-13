package my.ddx.ddphb.screens.main.characters

import my.ddx.ddphb.App
import my.ddx.ddphb.R
import my.ddx.ddphb.repositories.DataRepository
import my.ddx.ddphb.ui.adapters.simple.CellModel
import my.ddx.mvp.Presenter
import java.util.*
import javax.inject.Inject

class CharactersListPresenter : Presenter<CharactersList.View>(), CharactersList.Presenter {

    @Inject
    lateinit var dataRepository: DataRepository

    init {
        App.appComponent.inject(this)
    }

    override fun onAttachView(view: CharactersList.View) {
        val models = ArrayList<CellModel>()
        for (i in 1..10) {
            models.add(CellModel(
                    i.toString(),
                    String.format(Locale.getDefault(), "Character name %d", i),
                    String.format(Locale.getDefault(), "Class %d", i),
                    R.drawable.ic_group_black_24dp))
        }
    }
}
