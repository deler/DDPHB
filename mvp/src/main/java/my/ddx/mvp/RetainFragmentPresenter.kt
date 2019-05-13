package my.ddx.mvp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class RetainFragmentPresenter<T : Mvp.Presenter<*>> : Fragment() {

    companion object {

        fun <T : Mvp.Presenter<*>> findOrCreate(activity: FragmentActivity, tag: String): RetainFragmentPresenter<T> {
            var retainFragment = find<T>(activity, tag)

            if (retainFragment == null) {
                retainFragment = RetainFragmentPresenter()
                activity.supportFragmentManager.beginTransaction()
                        .add(retainFragment, tag)
                        .commitAllowingStateLoss()
            }

            return retainFragment
        }

        fun <T : Mvp.Presenter<*>> find(activity: FragmentActivity, tag: String): RetainFragmentPresenter<T>? {
            val fragment = activity.supportFragmentManager.findFragmentByTag(tag)

            var retainFragment: RetainFragmentPresenter<T>? = null
            if (fragment != null && fragment is RetainFragmentPresenter<*>) {
                try {

                    retainFragment = fragment as RetainFragmentPresenter<T>
                } catch (i: ClassCastException) {
                    retainFragment = null
                }

            }
            return retainFragment
        }

        // Remove from FragmentManager
        fun remove(activity: FragmentActivity, tag: String) {
            if (!activity.supportFragmentManager.isDestroyed) {
                val fragment = find<Mvp.Presenter<*>>(activity, tag)
                activity.supportFragmentManager.beginTransaction()
                        .remove(fragment as Fragment)
                        .commitAllowingStateLoss()
            }
        }
    }

    var presenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onDestroy() {
        if (presenter != null) {
            presenter!!.onDestroy()
        }
        presenter = null
        super.onDestroy()
    }


}
