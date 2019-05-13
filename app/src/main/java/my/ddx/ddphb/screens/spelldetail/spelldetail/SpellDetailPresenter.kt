package my.ddx.ddphb.screens.spelldetail.spelldetail

import android.content.Context
import my.ddx.ddphb.App
import my.ddx.ddphb.R
import my.ddx.ddphb.entities.SpellEntity
import my.ddx.ddphb.repositories.DataRepository
import my.ddx.mvp.Presenter
import javax.inject.Inject

class SpellDetailPresenter internal constructor(spellId: String) : Presenter<SpellDetail.View>(), SpellDetail.Presenter {

    @Inject
    lateinit var dataRepository: DataRepository

    @Inject
    lateinit var context: Context

    private var spellEntity: SpellEntity? = null

    init {
        App.appComponent.inject(this)

        mainDisposable?.add(dataRepository.spellsDao.getSpell(spellId).subscribe {
            spellEntity = it
            publishSpell()
        })
    }

    override fun onAttachView(view: SpellDetail.View) {
        super.onAttachView(view)

        spellEntity?.let {
            publishSpell()
        }
    }

    private fun publishSpell() {
        view?.let { view: SpellDetail.View ->
            val levelAndSchool = if (spellEntity?.level == 0) {
                context.getString(R.string.spell_details_text_cantrips_and_school, spellEntity?.school)
            } else {
                context.getString(R.string.spell_details_text_level_and_school, spellEntity?.level, spellEntity?.school)
            }

            val classesBuilder = StringBuilder()
            val classes = spellEntity?.classes
            classes?.let { classes ->
                if (classes.size > 1) {
                    classes.sortBy { it.name }
                    classes.forEach {
                        classesBuilder.append(it.name)
                        classesBuilder.append(", ")
                    }
                } else if (classes.size > 0) {
                    val classModel = classes.last()
                    classesBuilder.append(classModel.name)
                }
            }

            spellEntity?.let { spellEntity: SpellEntity ->
                view.setupView(SpellDetail.View.SpellViewModel(spellEntity.id)
                        .setName(spellEntity.name)
                        .setSchoolAndLevel(levelAndSchool)
                        .setRitual(spellEntity.isRitual)
                        .setCastingTime(spellEntity.castingTime)
                        .setRange(spellEntity.range)
                        .setDuration(spellEntity.duration ?: "")
                        .setConcentration(spellEntity.isConcentration)
                        .setComponents(spellEntity.components ?: "")
                        .setDescription(spellEntity.description)
                        .setUpLevel(spellEntity.upLevel ?: "")
                        .setClasses(classesBuilder.toString())
                )
            }
        }
    }
}
