package com.rsschool.quiz.result

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.rsschool.quiz.databinding.FragmentExitDialogBinding
import com.rsschool.quiz.main.FragmentController


class ExitDialogFragment : DialogFragment() {

    private var listener: FragmentController? = null
    private var _binding: FragmentExitDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        listener = context as FragmentController
        _binding = FragmentExitDialogBinding.inflate(LayoutInflater.from(context))

        binding.apply {
            cancel.setOnClickListener {
                dialog?.dismiss()
            }
            restart.setOnClickListener {
                listener?.onBackButtonClicked()
                dialog?.dismiss()
            }
            exit.setOnClickListener {
                listener?.onExitButtonClicked()
            }
        }
        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}