package my.ddx.ddphb.screens.dialogs.spellfilter

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragmet_spell_filter.view.*
import my.ddx.ddphb.R
import my.ddx.ddphb.entities.common.SpellFilterEntity
import my.ddx.ddphb.screens.base.BaseDialogFragmentView
import my.ddx.ddphb.ui.widgets.TagSelectorView
import java.util.*

class SpellFilterDialogFragmentView : BaseDialogFragmentView<SpellFilter.View, SpellFilter.Presenter>(), SpellFilter.View {

    companion object {
        private const val KEY_SPELL_FILTER = "KEY_SPELL_FILTER"
        private const val TAG_ALL_ID = "TAG_ALL_ID"

        fun newInstance(spellFilterEntity: SpellFilterEntity): SpellFilterDialogFragmentView {
            val args = Bundle()
            args.putParcelable(KEY_SPELL_FILTER, spellFilterEntity)
            val fragment = SpellFilterDialogFragmentView()
            fragment.arguments = args
            return fragment
        }
    }

    private val levelRangeEventSubject = PublishSubject.create<SpellFilter.View.LevelRangeEvent>()
    private val classesEventSubject = PublishSubject.create<Set<String>>()
    private val schoolsEventSubject = PublishSubject.create<Set<String>>()

    override var listener: SpellFilter.Listener? = null

    override val levelRangeFlowable: Flowable<SpellFilter.View.LevelRangeEvent>
        get() = levelRangeEventSubject.toFlowable(BackpressureStrategy.DROP)

    override val classesIdsFlowable: Flowable<Set<String>>
        get() = classesEventSubject.toFlowable(BackpressureStrategy.DROP)

    override val schoolsFlowable: Flowable<Set<String>>
        get() = schoolsEventSubject.toFlowable(BackpressureStrategy.DROP)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragmet_spell_filter, container, false)

    fun setChaingingDelayed(delayed: Boolean) {
        val layoutTransition = view?.layoutRoot?.layoutTransition
        layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        layoutTransition?.setStartDelay(LayoutTransition.CHANGING, (if (delayed) 350 else 0).toLong())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.rangeLevel?.setOnRangeBarChangeListener { _, leftPinIndex, rightPinIndex, _, _ -> levelRangeEventSubject.onNext(SpellFilter.View.LevelRangeEvent(leftPinIndex, rightPinIndex)) }

        view.buttonCancel?.setOnClickListener { dismissAllowingStateLoss() }
        view.buttonOk?.setOnClickListener {
            presenter?.let {
                listener?.onFilterChanged(it.filter)
            }
            dismissAllowingStateLoss()
        }
    }

    override fun createPresenter(): SpellFilterPresenter {
        val spellFilterModel = arguments?.getParcelable<SpellFilterEntity>(KEY_SPELL_FILTER)
        return SpellFilterPresenter(spellFilterModel!!)
    }

    override fun setupClassFilter(ids: Set<String>, classViewModels: List<SpellFilter.View.ClassViewModel>) {
        val tags = ArrayList<TagSelectorView.Tag>()
        tags.add(TagSelectorView.Tag(TAG_ALL_ID, getString(R.string.spell_filter_label_all)))
        for (model in classViewModels) {
            tags.add(TagSelectorView.Tag(model.id, model.name))
        }
        view?.layoutClasses?.setTags(tags)
        if (ids.isEmpty()) {
            view?.layoutClasses?.setSelectedTag(TAG_ALL_ID, true, false)
        } else {
            for (id in ids) {
                view?.layoutClasses?.setSelectedTag(id, true, false)
            }
        }
        view?.layoutClasses?.setListener { tag, selected ->
            if (tag.id.equals(TAG_ALL_ID, true)) {
                view?.layoutClasses?.resetSelected(true)
                view?.layoutClasses?.setSelectedTag(tag, true, true)
            } else {
                view?.layoutClasses?.setSelectedTag(TAG_ALL_ID, false, true)
                view?.layoutClasses?.setSelectedTag(tag, selected, true)
            }

            if (view?.layoutClasses?.selectedTags?.isEmpty() == true) {
                view?.layoutClasses?.setSelectedTag(TAG_ALL_ID, true, true)
            }
            //            else if (mLayoutClasses.isSeletedAllExclude(TAG_ALL_ID)){
            //                mLayoutClasses.resetSelected(true);
            //                mLayoutClasses.setSelectedTag(tag, true, true);
            //            }

            val selectedIds = HashSet<String>()
            view?.layoutClasses?.selectedTags?.forEach {
                it.id?.let {
                    if (!it.equals(TAG_ALL_ID, true)) selectedIds.add(it)
                }
            }
            classesEventSubject.onNext(selectedIds)
        }
    }

    override fun setupSchoolFilter(ids: Set<String>, schoolViewModels: List<SpellFilter.View.SchoolViewModel>) {
        val tags = ArrayList<TagSelectorView.Tag>()
        tags.add(TagSelectorView.Tag(TAG_ALL_ID, getString(R.string.spell_filter_label_all)))
        for (model in schoolViewModels) {
            tags.add(TagSelectorView.Tag(model.id, model.name))
        }
        view?.layoutSchools?.setTags(tags)
        if (ids.isEmpty()) {
            view?.layoutSchools?.setSelectedTag(TAG_ALL_ID, true, false)
        } else {
            for (id in ids) {
                view?.layoutSchools?.setSelectedTag(id, true, false)
            }
        }
        view?.layoutSchools?.setListener { tag, selected ->
            if (tag.id.equals(TAG_ALL_ID, true)) {
                view?.layoutSchools?.resetSelected(true)
                view?.layoutSchools?.setSelectedTag(tag, true, true)
            } else {
                view?.layoutSchools?.setSelectedTag(TAG_ALL_ID, false, true)
                view?.layoutSchools?.setSelectedTag(tag, selected, true)
            }

            if (view?.layoutSchools?.selectedTags?.isEmpty() == true) {
                view?.layoutSchools?.setSelectedTag(TAG_ALL_ID, true, true)
            }
            //            else if (mLayoutSchools.isSeletedAllExclude(TAG_ALL_ID)){
            //                mLayoutSchools.resetSelected(true);
            //                mLayoutSchools.setSelectedTag(tag, true, true);
            //            }

            val selectedIds = HashSet<String>()
            view?.layoutSchools?.selectedTags?.forEach {
                it.id?.let {
                    if (!it.equals(TAG_ALL_ID, true))
                        selectedIds.add(it)
                }
            }
            schoolsEventSubject.onNext(selectedIds)
        }
    }

    override fun setupLevelFilter(min: Int, max: Int) {
        view?.rangeLevel?.setRangePinsByIndices(min, max)
    }
}
