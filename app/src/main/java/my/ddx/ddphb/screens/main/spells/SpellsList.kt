package my.ddx.ddphb.screens.main.spells

import io.reactivex.Flowable
import my.ddx.ddphb.entities.common.SpellFilterEntity
import my.ddx.ddphb.ui.adapters.simple.CellModel
import my.ddx.mvp.Mvp

interface SpellsList {
    interface Presenter : Mvp.Presenter<View>

    interface View : Mvp.View {

        val filterClickFlowable: Flowable<Any>
        val spellClickFlowable: Flowable<CellModel>
        val searchQueryFlowable: Flowable<String>
        val spellFilterChangeFlowable: Flowable<SpellFilterEntity>

        fun showSpellsList(models: List<CellModel>)
        fun openSpellDetails(spellId: String, spellIds: List<String>)
        fun openFilterSettings(filter: SpellFilterEntity)
    }

}
