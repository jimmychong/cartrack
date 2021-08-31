package com.cartrack.base

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cartrack.MainApplication
import com.cartrack.R
import com.cartrack.model.ErrorResponse
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.Disposable


abstract class BaseActivity : AppCompatActivity() {
    protected var allowOnBackPress = true
    private var disposable: Disposable? = null

    private lateinit var snackbar: Snackbar

    private lateinit var progressDialog: AlertDialog


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setMainLayout()
        //inflate base layout
        layoutInflater.inflate(R.layout.activity_base, findViewById(android.R.id.content))

        progressDialog = ProgressDialog(this).create()

        //When No Network display snackbar
        snackbar = Snackbar.make(
            findViewById(R.id.baseActivityViewGroup),
            R.string.label_network_dropped,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.label_cancel) {
                snackbar.dismiss()
            }

        (application as MainApplication).networkConnected.observe(this) {
            if (!it)
                snackbar.show()
            else
                snackbar.dismiss()

        }
    }

    //when activity or fragment need show loading call this
    fun showLoadingIndicator(isShowLoading: Boolean) {
        if (isShowLoading)
            progressDialog.show()
        else
            progressDialog.dismiss()
    }

    open fun handleApiError(error: ErrorResponse) {
        val errorMessage = error.getDisplayMessage(this)

        showApiError(errorMessage) { dialog ->
            dialog.dismiss()
        }
    }

    private fun showApiError(message: String, confirmCallback: ((DialogInterface) -> Unit)) {
        showLoadingIndicator(false)

        MaterialAlertDialogBuilder(this)
            .setMessage(message)
            .setPositiveButton(R.string.button_confirm) { dialog, _ ->
                confirmCallback.invoke(dialog)
            }
            .setCancelable(false)
            .show()
    }

    /**
     * Add / replace fragment
     */
    fun pushFragment(
        fragment: BaseFragment,
        @IdRes containerId: Int,
        action: PushFragmentAction = PushFragmentAction.Replace,
        isAddToBackStack: Boolean = true
    ) {
        supportFragmentManager.beginTransaction().apply {
            if (action == PushFragmentAction.Replace) {
                replace(containerId, fragment)
            } else {
                add(containerId, fragment)
            }
            if (isAddToBackStack) {
                addToBackStack(fragment.javaClass.name)
            }
            commit()
        }
    }

    open fun setMainLayout() {
        setContentView(getLayoutResId())
    }

    @LayoutRes
    abstract fun getLayoutResId(): Int

    @IdRes
    abstract fun getMainFragmentContainer(): Int?

    @CallSuper
    override fun onDestroy() {
        progressDialog.dismiss()

        if (disposable?.isDisposed == false) {
            disposable?.dispose()
        }
        super.onDestroy()
    }

}

enum class PushFragmentAction {
    Add,
    Replace
}