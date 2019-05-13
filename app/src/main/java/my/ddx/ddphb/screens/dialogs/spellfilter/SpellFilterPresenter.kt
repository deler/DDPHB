package my.ddx.ddphb.screens.dialogs.spellfilter

import my.ddx.ddphb.App
import my.ddx.ddphb.entities.ClassEntity
import my.ddx.ddphb.entities.common.SpellFilterEntity
import my.ddx.ddphb.repositories.DataRepository
import my.ddx.mvp.Presenter
import javax.inject.Inject

class SpellFilterPresenter(override val filter: SpellFilterEntity) : Presenter<SpellFilter.View>(), SpellFilter.Presenter {
    @Inject
    lateinit var dataRepository: DataRepository

    private var spellSchools: MutableList<String> = ArrayList()
    private var classes: MutableList<ClassEntity> = ArrayList()

    init {
        App.appComponent.inject(this)

        loadClasses()
        loadSpellSchools()
    }

    private fun loadSpellSchools() {
        mainDisposable?.add(dataRepository.spellsDao.getSchools().subscribe {
            spellSchools.clear()
            spellSchools.addAll(it)

            publishSchools()
        })
    }

    private fun loadClasses() {

        mainDisposable?.add(dataRepository.classesDao.getClasses().subscribe {
            classes.clear()
            classes.addAll(it)

            publishClasses()
        })
    }


    private fun publishSchools() {
        view?.let {
            val models = ArrayList<SpellFilter.View.SchoolViewModel>()
            spellSchools.forEach {
                models.add(SpellFilter.View.SchoolViewModel(it, it.toUpperCase()))
            }
            it.setupSchoolFilter(filter.schools, models)
        }
    }

    private fun publishClasses() {
        view?.let {
            val models = ArrayList<SpellFilter.View.ClassViewModel>()
            classes.forEach {
                models.add(SpellFilter.View.ClassViewModel(it.id, it.name.toUpperCase()))
            }
            it.setupClassFilter(filter.classIds, models)
        }
    }

    private fun publishLevels() {
        view?.setupLevelFilter(filter.minLevel, filter.maxLevel)
    }


    override fun onAttachView(view: SpellFilter.View) {
        super.onAttachView(view)

        if (classes.isNotEmpty()) {
            publishClasses()
        }

        if (spellSchools.isNotEmpty()) {
            publishSchools()
        }

        publishLevels()

        viewDisposable?.add(view.levelRangeFlowable.subscribe { levelRangeEvent ->
            filter.minLevel = levelRangeEvent.min
            filter.maxLevel = levelRangeEvent.max
        })
        viewDisposable?.add(view.classesIdsFlowable.subscribe { ids -> filter.classIds = ids })
        viewDisposable?.add(view.schoolsFlowable.subscribe { schools -> filter.schools = schools })
    }
}
