package my.ddx.mvp

import android.os.Bundle

interface Mvp {
    interface View

    interface ExtendPresenter {
        fun onRestore(savedInstanceState: Bundle?)
        fun onSave(bundle: Bundle)
        fun onResume()
        fun onPause()
    }

    interface Presenter<T : View> {
        fun onAttachView(view: T)
        fun onDetachView()
        fun onDestroy()
    }

    interface Delegate<V : View, P : Presenter<V>> {
        val presenter: P?
        val extendPresenter: ExtendPresenter?
        fun onCreate(savedInstanceState: Bundle?)
        fun onViewCreated()
        fun onResume()
        fun onPause()
        fun onSaveInstanceState(outState: Bundle?)
        fun onDestroyView()
        fun onDestroy()

        interface Callback<V : View, P : Presenter<V>> {
            fun isNeedRetainPresenter(): Boolean
            fun isNeedDestroyRetainedPresenter(): Boolean
            fun getRetainPresenterTag(): String
            fun createPresenter(): P
            fun view(): V
        }
    }

}