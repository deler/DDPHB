package my.ddx.mvp

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class Delegate <V : Mvp.View, P:Mvp.Presenter<V>>(val host: FragmentActivity?, var callback: Mvp.Delegate.Callback<V, P>?) : Mvp.Delegate<V, P> {

    override val presenter: P?
        get() {
            var presenter:P? = null
            callback?.let {
                if (it.isNeedRetainPresenter() && host != null) {
                    val fragmentPresenter = RetainFragmentPresenter.findOrCreate<P>(host, it.getRetainPresenterTag())
                    presenter = fragmentPresenter.presenter
                    if (presenter == null) {
                        presenter = it.createPresenter()
                        fragmentPresenter.presenter = presenter
                    }
                } else {
                    presenter = it.createPresenter()
                }
            }
            return presenter
        }

    override val extendPresenter: Mvp.ExtendPresenter?
        get() {
            return if (presenter is Mvp.ExtendPresenter) {
                presenter as Mvp.ExtendPresenter
            } else {
                null
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        presenter?.let {
            if (savedInstanceState != null) {
                extendPresenter?.let {
                    it.onRestore(savedInstanceState.getBundle(callback?.getRetainPresenterTag()))
                }
            }
        }
    }

    override fun onViewCreated() {
        callback?.let {
            presenter?.onAttachView(it.view())
        }
    }

    override fun onResume() {
        extendPresenter?.onResume()
    }

    override fun onPause() {
        extendPresenter?.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        extendPresenter?.let {
            val bundle = Bundle()
            it.onSave(bundle)
            callback?.let {
                outState?.putBundle(it.getRetainPresenterTag(), bundle)
            }
        }
    }

    override fun onDestroyView() {
        presenter?.onDetachView()
    }

    override fun onDestroy() {
        callback?.let { callback: Mvp.Delegate.Callback<V, P> ->
            if (callback.isNeedDestroyRetainedPresenter()) {
                host?.let { host: FragmentActivity ->
                    RetainFragmentPresenter.remove(host, callback.getRetainPresenterTag())
                }
            }

            if (callback.isNeedRetainPresenter()) {
                presenter?.onDestroy()
            }
        }
        callback = null
    }
}