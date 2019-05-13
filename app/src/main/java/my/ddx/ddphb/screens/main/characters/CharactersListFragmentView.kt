package my.ddx.ddphb.screens.main.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tapadoo.alerter.Alerter
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragmet_character_list.view.*
import my.ddx.ddphb.R
import my.ddx.ddphb.screens.base.BaseFragmentView
import my.ddx.ddphb.ui.adapters.simple.CellModel
import my.ddx.ddphb.ui.adapters.simple.SimpleListAdapter

class CharactersListFragmentView : BaseFragmentView<CharactersList.View, CharactersList.Presenter>(), CharactersList.View {

    companion object {
        fun newInstance(): CharactersListFragmentView {
            val args = Bundle()

            val fragment = CharactersListFragmentView()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var layoutManager: LinearLayoutManager
    private var adapter: SimpleListAdapter? = null
    private lateinit var dividerItemDecoration: DividerItemDecoration
    private var disposable: Disposable? = null

    override fun createPresenter(): CharactersList.Presenter {
        return CharactersListPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragmet_character_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.recyclerCharacters?.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(context)
        view.recyclerCharacters?.layoutManager = layoutManager

        dividerItemDecoration = DividerItemDecoration(view.recyclerCharacters?.context, layoutManager.orientation)
        context?.let {
            ContextCompat.getDrawable(it, R.drawable.separator_items)?.let {
                dividerItemDecoration.setDrawable(it)
            }
        }
        view.recyclerCharacters?.addItemDecoration(dividerItemDecoration)

        activity?.setTitle(R.string.main_title_characters)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable?.dispose()
    }

    override fun showCharactersList(models: List<CellModel>) {
        disposable?.dispose()
        adapter = SimpleListAdapter(models)
        disposable = adapter?.clickFlowable?.subscribe {
            activity?.let {
                Alerter.create(it)
                        .setTitle(R.string.main_title_characters)
                        .setText("Not yet ready")
                        .setDuration(2000)
                        .setBackgroundColor(R.color.orange)
                        .show()
            }
        }
        view?.recyclerCharacters?.adapter = adapter
    }
}
