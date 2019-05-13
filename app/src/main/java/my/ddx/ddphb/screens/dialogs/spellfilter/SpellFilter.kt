package my.ddx.ddphb.screens.dialogs.spellfilter

import io.reactivex.Flowable
import my.ddx.ddphb.entities.common.SpellFilterEntity
import my.ddx.mvp.Mvp

interface SpellFilter {

    interface Presenter : Mvp.Presenter<View> {
        val filter: SpellFilterEntity
    }

    interface View : Mvp.View {

        val levelRangeFlowable: Flowable<LevelRangeEvent>
        val classesIdsFlowable: Flowable<Set<String>>
        val schoolsFlowable: Flowable<Set<String>>
        var listener: Listener?

        fun setupClassFilter(ids: Set<String>, classViewModels: List<ClassViewModel>)

        fun setupSchoolFilter(ids: Set<String>, schoolViewModels: List<SchoolViewModel>)

        fun setupLevelFilter(min: Int, max: Int)


        class LevelRangeEvent(val min: Int = 0, val max: Int = 9)

        class ClassViewModel(val id: String, val name: CharSequence)

        class SchoolViewModel(val id: String, val name: CharSequence)
    }

    interface Listener {
        fun onFilterChanged(spellFilterEntity: SpellFilterEntity)
    }
}
