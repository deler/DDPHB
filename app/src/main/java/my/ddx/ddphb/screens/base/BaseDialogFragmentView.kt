package my.ddx.ddphb.screens.base

import my.ddx.mvp.DialogFragmentView
import my.ddx.mvp.Mvp

abstract class BaseDialogFragmentView<V : Mvp.View, P : Mvp.Presenter<V>> : DialogFragmentView<V, P>()
