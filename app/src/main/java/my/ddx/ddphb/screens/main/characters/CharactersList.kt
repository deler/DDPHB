package my.ddx.ddphb.screens.main.characters

import my.ddx.ddphb.ui.adapters.simple.CellModel
import my.ddx.mvp.Mvp

interface CharactersList {
    interface Presenter : Mvp.Presenter<View>

    interface View : Mvp.View {
        fun showCharactersList(models: List<CellModel>)
    }
}