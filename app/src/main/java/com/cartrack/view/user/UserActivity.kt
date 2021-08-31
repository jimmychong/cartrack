package com.cartrack.view.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cartrack.BUNDLE_IS_LOGOUT
import com.cartrack.R
import com.cartrack.base.BaseActivity
import com.cartrack.util.LoginManager
import com.cartrack.view.login.LoginActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class UserActivity : BaseActivity() {
    lateinit var topAppBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pushFragment(UserListingFragment(), getMainFragmentContainer(), isAddToBackStack = false)

        topAppBar = findViewById(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

        topAppBar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_logout) {
                logout()
            }
            true

        }

    }


    fun showBackButton(show: Boolean) {
        if (show) {
            topAppBar.navigationIcon =
                ContextCompat.getDrawable(this, R.drawable.outline_arrow_back_black_large)
        } else {
            topAppBar.navigationIcon = null
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_user
    }

    override fun getMainFragmentContainer(): Int {
        return R.id.fragmentContainer
    }

    fun logout() {
        MaterialAlertDialogBuilder(this)
            .setMessage(getString(R.string.logout_title))
            .setPositiveButton(R.string.button_confirm) { dialog, _ ->
                startActivity(Intent(this, LoginActivity::class.java))
                LoginManager.isLogin = false
                finish()
            }
            .setNegativeButton(R.string.button_cancel) { dialog, _ ->
            }
            .setCancelable(false)
            .show()
    }

    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount == 0) {
            logout()
        } else {
            super.onBackPressed()
        }
    }
}