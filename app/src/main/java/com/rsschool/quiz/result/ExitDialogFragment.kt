package com.rsschool.quiz.result

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.rsschool.quiz.R
import com.rsschool.quiz.main.FragmentController
import java.lang.IllegalStateException

class ExitDialogFragment : DialogFragment() {

    private var listener: FragmentController? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        listener = context as FragmentController

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.exit_or_restart))
                .setPositiveButton(R.string.exit
                ) { _, _ ->
                    listener?.onExitButtonClicked()
                }
                .setNegativeButton(R.string.restart
                ) { _, _ ->
                    listener?.onBackButtonClicked()
                }
                .setNeutralButton(R.string.cancel
                ) { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}