package my.ddx.ddphb.screens.spelldetail.spelldetail

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragmet_spell_detail.view.*

import my.ddx.ddphb.R
import my.ddx.ddphb.screens.base.BaseFragmentView

class SpellDetailFragmentView : BaseFragmentView<SpellDetail.View, SpellDetail.Presenter>(), SpellDetail.View {

    companion object {
        private const val KEY_SPELL_ID = "KEY_SPELL_ID"

        fun newInstance(spellId: String): SpellDetailFragmentView {
            val args = Bundle()
            args.putString(KEY_SPELL_ID, spellId)
            val fragment = SpellDetailFragmentView()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var spellId: String

    override fun getRetainPresenterTag(): String {
        return String.format("%s_%s", super.getRetainPresenterTag(), spellId)
    }

    override fun createPresenter(): SpellDetail.Presenter {
        return SpellDetailPresenter(spellId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        spellId = arguments?.getString(KEY_SPELL_ID)!!
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragmet_spell_detail, container, false)

    override fun setupView(spellModel: SpellDetail.View.SpellViewModel) {
        view?.textName?.text = spellModel.getName()
        view?.textSchoolAndLevel?.text = spellModel.getSchoolAndLevel()
        view?.textCastingTime?.text = spellModel.getCastingTime()
        view?.textLabelRitual?.visibility = if (spellModel.isRitual()) View.VISIBLE else View.GONE
        view?.textRange?.text = spellModel.getRange()
        view?.textDuration?.text = spellModel.getDuration()
        view?.textLabelConcentration?.visibility = if (spellModel.isConcentration()) View.VISIBLE else View.GONE
        view?.textComponents?.text = spellModel.getComponents()
        view?.textDescription?.text = spellModel.getDescription()
        view?.layoutUpLevel?.visibility = if (TextUtils.isEmpty(spellModel.getUpLevel())) View.GONE else View.VISIBLE
        view?.textUpLevel?.text = spellModel.getUpLevel()
        view?.textClasses?.text = spellModel.getClasses()
    }
}
