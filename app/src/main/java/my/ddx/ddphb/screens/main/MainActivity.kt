package my.ddx.ddphb.screens.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_main.*
import my.ddx.ddphb.R
import my.ddx.ddphb.screens.base.BaseActivity
import my.ddx.ddphb.screens.main.characters.CharactersListFragmentView
import my.ddx.ddphb.screens.main.spells.SpellsListFragmentView

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var selectedMenuId : Int = R.id.nav_characters
    private var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.main_drawer_open, R.string.main_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        savedInstanceState?.let {
            selectedMenuId = it.getInt("selectedMenuId", R.id.nav_characters)
        }

        navView.setCheckedItem(selectedMenuId)
        navView.setNavigationItemSelectedListener(this)

        fragment = supportFragmentManager.findFragmentById(content.id)

        if (fragment == null) {
            fragment = CharactersListFragmentView.newInstance()
            supportFragmentManager.beginTransaction().replace(content.id, fragment as CharactersListFragmentView).commitAllowingStateLoss()
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("selectedMenuId", selectedMenuId)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_characters -> {
                selectedMenuId = item.itemId
                fragment = CharactersListFragmentView.newInstance()
                supportFragmentManager.beginTransaction().replace(content.id, fragment as CharactersListFragmentView).commitAllowingStateLoss()
            }
            R.id.nav_spells -> {
                selectedMenuId = item.itemId
                fragment = SpellsListFragmentView.newInstance()
                supportFragmentManager.beginTransaction().replace(content.id, fragment as SpellsListFragmentView).commitAllowingStateLoss()
            }
            R.id.nav_skills -> {
                Alerter.create(this)
                        .setTitle(R.string.main_title_skills)
                        .setText("Not yet ready")
                        .setDuration(2000)
                        .setBackgroundColor(R.color.orange)
                        .show()
                return false
            }
            R.id.nav_items -> {
                Alerter.create(this)
                        .setTitle(R.string.main_title_items)
                        .setText("Not yet ready")
                        .setDuration(2000)
                        .setBackgroundColor(R.color.orange)
                        .show()
                return false
            }
            R.id.nav_monsters -> {
                Alerter.create(this)
                        .setTitle(R.string.main_title_monsters)
                        .setText("Not yet ready")
                        .setDuration(2000)
                        .setBackgroundColor(R.color.orange)
                        .show()
                return false
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
