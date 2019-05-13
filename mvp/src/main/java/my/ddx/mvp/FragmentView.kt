package my.ddx.mvp

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class FragmentView<V:Mvp.View, P:Mvp.Presenter<V>> : Fragment(), Mvp.View, Mvp.Delegate.Callback<V, P>{

    private var delegate: Mvp.Delegate<V, P>? = null

    val presenter: P?
        get() = delegate?.presenter

    fun isCustomViewCreateEvent(): Boolean {
        return false
    }

    override fun isNeedRetainPresenter(): Boolean {
        return true
    }

    override fun isNeedDestroyRetainedPresenter(): Boolean {
        return activity?.isFinishing == true || isRemoving
    }

    override fun getRetainPresenterTag(): String {
        return "Presenter_" + this.javaClass.simpleName
    }

    override fun view(): V {
        return this as V
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate = Delegate(activity, this)
        delegate?.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isCustomViewCreateEvent()) {
            delegate?.onViewCreated()
        }
    }

    override fun onResume() {
        delegate?.onResume()
        super.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        delegate?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        delegate?.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        delegate?.onDestroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        delegate?.onDestroy()
        delegate = null
        super.onDestroy()
    }
}