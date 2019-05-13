package my.ddx.mvp

import io.reactivex.disposables.CompositeDisposable

abstract class Presenter<V: Mvp.View> : Mvp.Presenter<V> {
    var mainDisposable: CompositeDisposable? = null
    var viewDisposable: CompositeDisposable? = null
    var view: V? = null

    init {
        mainDisposable = CompositeDisposable()
    }

    override fun onAttachView(view: V) {
        viewDisposable = CompositeDisposable()
        this.view = view
    }

    override fun onDetachView() {
        viewDisposable?.dispose()
        view = null
    }

    override fun onDestroy() {
        mainDisposable?.dispose()
    }
}