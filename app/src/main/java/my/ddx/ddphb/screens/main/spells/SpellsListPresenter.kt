package my.ddx.ddphb.screens.main.spells

import android.content.Context
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import my.ddx.ddphb.App
import my.ddx.ddphb.R
import my.ddx.ddphb.entities.SpellEntity
import my.ddx.ddphb.entities.common.SpellFilterEntity
import my.ddx.ddphb.repositories.DataRepository
import my.ddx.ddphb.ui.adapters.simple.CellModel
import my.ddx.mvp.Presenter
import javax.inject.Inject

class SpellsListPresenter : Presenter<SpellsList.View>(), SpellsList.Presenter {

    @Inject
    lateinit var context: Context
    @Inject
    lateinit var dataRepository: DataRepository

    private var spells: MutableList<SpellEntity> = ArrayList()
    private var filter = SpellFilterEntity()

    init {
        App.appComponent.inject(this)
        loadSpells()
    }

    override fun onAttachView(view: SpellsList.View) {
        super.onAttachView(view)

        viewDisposable?.add(view.searchQueryFlowable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(({ this.filterByName(it) })))
        viewDisposable?.add(view.spellClickFlowable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(({ this.showDetailsForFiltered(it) })))
        viewDisposable?.add(view.filterClickFlowable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(({ this.openFilterSettings() })))
        viewDisposable?.add(view.spellFilterChangeFlowable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(({ this.filterChanged(it) })))

        if (spells.isNotEmpty()) {
            publishSpells()
        }
    }

    private fun publishSpells() {
        view?.let {
            val models = ArrayList<CellModel>()
            spells.forEach { spell: SpellEntity ->
                val levelAndSchool = if (spell.level == 0) {
                    context.getString(R.string.spells_text_cantrips_and_school, spell.school)
                } else {
                    context.getString(R.string.spells_text_level_and_school, spell.level, spell.school)
                }
                models.add(CellModel(spell.id, spell.name, levelAndSchool, null))
            }
            it.showSpellsList(models)
        }
    }

    private fun filterByName(string: String) {
        Logger.d(string)
        filter.name = string
        loadSpells()
    }

    private fun filterChanged(spellFilterEntity: SpellFilterEntity) {
        filter = spellFilterEntity
        loadSpells()
    }

    private fun loadSpells() {
        mainDisposable?.add(dataRepository.spellsDao.getSpells(filter).subscribe {
            spells.clear()
            spells.addAll(it)
            publishSpells()
        })
    }

    private fun showDetailsForFiltered(cellModel: CellModel) {
        view?.let { view ->
            val spellIds = ArrayList<String>()
            spells.forEach {
                spellIds.add(it.id)
            }
            view.openSpellDetails(cellModel.id, spellIds)
        }
    }

    private fun openFilterSettings() {
        view?.openFilterSettings(filter)
    }
}
