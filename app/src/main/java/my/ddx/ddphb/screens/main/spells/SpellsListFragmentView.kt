package my.ddx.ddphb.screens.main.spells

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.jakewharton.rxbinding2.view.RxMenuItem
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragmet_spell_list.view.*
import my.ddx.ddphb.R
import my.ddx.ddphb.entities.common.SpellFilterEntity
import my.ddx.ddphb.screens.base.BaseFragmentView
import my.ddx.ddphb.screens.dialogs.spellfilter.SpellFilter
import my.ddx.ddphb.screens.dialogs.spellfilter.SpellFilterDialogFragmentView
import my.ddx.ddphb.screens.spelldetail.SpellDetailActivity
import my.ddx.ddphb.ui.adapters.simple.CellModel
import my.ddx.ddphb.ui.adapters.simple.SimpleListAdapter
import java.util.concurrent.TimeUnit

class SpellsListFragmentView : BaseFragmentView<SpellsList.View, SpellsList.Presenter>(), SpellsList.View {

    companion object {
        private const val TAG_SPELL_FILTER_DIALOG = "TAG_SPELL_FILTER_DIALOG"

        fun newInstance(): SpellsListFragmentView {
            val args = Bundle()

            val fragment = SpellsListFragmentView()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var layoutManager: LinearLayoutManager
    private var adapter: SimpleListAdapter? = null
    private lateinit var dividerItemDecoration: DividerItemDecoration

    private var spellClickSubject = PublishSubject.create<CellModel>()
    private var spellFilterSubject = PublishSubject.create<SpellFilterEntity>()
    private var filterClickSubject = PublishSubject.create<Any>()
    private var searchSubject = BehaviorSubject.createDefault("")

    private var compositeDisposable: CompositeDisposable? = null

    private var searchSubscribe: Disposable? = null
    private var spellClickSubscribe: Disposable? = null
    private var filterClickSubscribe: Disposable? = null

    override val filterClickFlowable: Flowable<Any>
        get() = filterClickSubject.toFlowable(BackpressureStrategy.DROP)

    override val spellClickFlowable: Flowable<CellModel>
        get() = spellClickSubject.toFlowable(BackpressureStrategy.DROP)

    override val searchQueryFlowable: Flowable<String>
        get() = searchSubject.toFlowable(BackpressureStrategy.LATEST)

    override val spellFilterChangeFlowable: Flowable<SpellFilterEntity>
        get() = spellFilterSubject.toFlowable(BackpressureStrategy.DROP)

    override fun createPresenter(): SpellsList.Presenter {
        return SpellsListPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragmet_spell_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compositeDisposable = CompositeDisposable()

        view.recyclerSpells?.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(context)
        layoutManager?.let { layoutManager: LinearLayoutManager ->
            view.recyclerSpells?.layoutManager = layoutManager
            dividerItemDecoration = DividerItemDecoration(view.recyclerSpells?.context, layoutManager.orientation)
            dividerItemDecoration?.let { dividerItemDecoration: DividerItemDecoration ->
                context?.let {
                    ContextCompat.getDrawable(it, R.drawable.separator_items)?.let {
                        dividerItemDecoration.setDrawable(it)
                    }
                }
                view.recyclerSpells?.addItemDecoration(dividerItemDecoration)
            }
        }

        activity?.setTitle(R.string.main_title_spells)

        savedInstanceState?.let {
            val spellFilterDialogFragmentView = fragmentManager?.findFragmentByTag(TAG_SPELL_FILTER_DIALOG) as SpellFilterDialogFragmentView?
            spellFilterDialogFragmentView?.listener = SpellFilterDialogListener()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.fragment_main_spells, menu)
        val filterItem = menu!!.findItem(R.id.action_filter)
        if (filterItem != null) {
            filterClickSubscribe?.dispose()
            filterClickSubscribe = RxMenuItem.clicks(filterItem)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .doOnNext { filterClickSubject.onNext(it) }
                    .subscribe()
            filterClickSubscribe?.let {
                compositeDisposable?.add(it)
            }
        }

        val searchItem = menu.findItem(R.id.action_search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchSubscribe?.dispose()
            searchSubscribe = RxSearchView.queryTextChanges(searchView)
                    .debounce(1, TimeUnit.SECONDS)
                    .map<String> { it.toString() }
                    .doOnNext { searchSubject.onNext(it) }
                    .subscribe()
            searchSubscribe?.let {
                compositeDisposable?.add(it)
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        compositeDisposable?.dispose()
        super.onDestroyView()
    }

    override fun showSpellsList(models: List<CellModel>) {
        adapter = SimpleListAdapter(models)
        spellClickSubscribe?.dispose()
        adapter?.let {
            spellClickSubscribe = it.clickFlowable
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .doOnNext { spellClickSubject.onNext(it) }
                    .subscribe()
        }
        spellClickSubscribe?.let {
            compositeDisposable?.add(it)
        }
        view?.recyclerSpells?.adapter = adapter
    }

    override fun openSpellDetails(spellId: String, spellIds: List<String>) {
        context?.let { SpellDetailActivity.start(it, spellId, spellIds) }
    }

    override fun openFilterSettings(filter: SpellFilterEntity) {
        val spellFilterDialogFragmentView = SpellFilterDialogFragmentView.newInstance(filter)
        spellFilterDialogFragmentView.listener = SpellFilterDialogListener()
        spellFilterDialogFragmentView.show(fragmentManager, TAG_SPELL_FILTER_DIALOG)
    }

    private inner class SpellFilterDialogListener : SpellFilter.Listener {
        override fun onFilterChanged(spellFilterEntity: SpellFilterEntity) {
            spellFilterSubject.onNext(spellFilterEntity)
        }
    }
}
