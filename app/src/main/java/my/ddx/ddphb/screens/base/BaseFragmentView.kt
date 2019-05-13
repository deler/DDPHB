package my.ddx.ddphb.screens.base

import my.ddx.mvp.FragmentView
import my.ddx.mvp.Mvp

abstract class BaseFragmentView<V : Mvp.View, P : Mvp.Presenter<V>> : FragmentView<V, P>()
