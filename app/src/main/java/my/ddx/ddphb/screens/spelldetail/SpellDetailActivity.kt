package my.ddx.ddphb.screens.spelldetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_spell_detail.*
import my.ddx.ddphb.R
import my.ddx.ddphb.screens.spelldetail.spelldetail.SpellDetailFragmentView
import java.util.*

class SpellDetailActivity : AppCompatActivity() {

    companion object {
        private const val KEY_SPELL_IDS = "KEY_SPELL_IDS"
        private const val KEY_CURRENT_SPELL_ID = "KEY_CURRENT_SPELL_ID"

        fun start(context: Context, currentSpellId: String, spellIds: List<String>) {
            val starter = Intent(context, SpellDetailActivity::class.java)
            starter.putExtra(KEY_CURRENT_SPELL_ID, currentSpellId)
            starter.putStringArrayListExtra(KEY_SPELL_IDS, ArrayList(spellIds))
            context.startActivity(starter)
        }
    }

    private lateinit var spellIds: List<String>
    private lateinit var currentSpellId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent.extras
        spellIds = extras?.getStringArrayList(KEY_SPELL_IDS) ?: ArrayList()
        currentSpellId = extras?.getString(KEY_CURRENT_SPELL_ID) ?: ""

        if (savedInstanceState != null) {
            currentSpellId = savedInstanceState.getString(KEY_CURRENT_SPELL_ID, currentSpellId)
        }

        setContentView(R.layout.activity_spell_detail)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        pager.adapter = PagerAdapter(supportFragmentManager, spellIds)
        pager.addOnPageChangeListener(PageChangeListener())
        val pageIndex = spellIds.indexOf(currentSpellId)
        pager.currentItem = pageIndex
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_CURRENT_SPELL_ID, currentSpellId)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class PageChangeListener : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            currentSpellId = spellIds[position]
        }
    }

    private class PagerAdapter(fm: FragmentManager, private val mSpellIds: List<String>) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return SpellDetailFragmentView.newInstance(mSpellIds[position])
        }

        override fun getCount(): Int {
            return mSpellIds.size
        }
    }
}
