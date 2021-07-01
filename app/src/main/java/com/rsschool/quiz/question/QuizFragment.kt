package com.rsschool.quiz.question

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.forEach
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import com.rsschool.quiz.R
import com.rsschool.quiz.databinding.FragmentQuizBinding
import com.rsschool.quiz.main.*

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private var listener: FragmentController? = null
    private var questionNumber = 0
    private var checkedButtonId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        listener = context as FragmentController
        questionNumber = arguments?.getInt(QUESTION_NUMBER_KEY) ?: 0
        checkedButtonId = arguments?.getInt(CHECKED_BUTTON_KEY, -1) ?: -1
        val anyButtonChecked = checkedButtonId != -1

        setTheme(inflater, container)
        if (anyButtonChecked) setPreviouslyCheckedButton()

        return binding.root
    }

    /** Choose theme and status bar color */
    private fun setTheme(inflater: LayoutInflater, container: ViewGroup?) {
        val colors =
            listOf(
                R.color.lust_dark,
                R.color.green_blue_dark,
                R.color.green_dark,
                R.color.icterine_dark,
                R.color.middle_green_dark,
                R.color.jonquil_dark,
                R.color.lavender_gray_dark
            )
        val themes =
            listOf(
                R.style.Theme_Quiz_First,
                R.style.Theme_Quiz_Second,
                R.style.Theme_Quiz_Third,
                R.style.Theme_Quiz_Fourth,
                R.style.Theme_Quiz_Fifth,
                R.style.Theme_Quiz_Sixth,
                R.style.Theme_Quiz_Seventh
            )
        val themedInflater = inflater.cloneInContext(
            ContextThemeWrapper(activity, themes[questionNumber - 1])
        )
        _binding = FragmentQuizBinding.inflate(themedInflater, container, false)

        listener?.updateStatusBarColor(colors[questionNumber - 1])
    }

    private fun setPreviouslyCheckedButton() {
        binding.radioGroup.forEach { option ->
            option as RadioButton
            if (option.id == checkedButtonId) option.isChecked = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setButtons()
        setQuestionAndAnswers()
    }

    private fun setToolbar() {
        binding.apply {
            toolbar.title = "Question $questionNumber"

            if (questionNumber == 1) toolbar.navigationIcon = null
            else toolbar.setNavigationOnClickListener { listener?.onPreviousButtonClicked() }
        }
    }

    private fun setButtons() {
        val noButtonChecked = checkedButtonId == -1

        binding.apply {
            when (questionNumber) {
                1 -> previousButton.isInvisible = true
                7 -> nextButton.text = getString(R.string.submit)
            }

            if (noButtonChecked) nextButton.isEnabled = false

            previousButton.setOnClickListener { listener?.onPreviousButtonClicked() }

            nextButton.setOnClickListener { listener?.onNextOrSubmitButtonClicked() }

            radioGroup.setOnCheckedChangeListener { _, _ ->
                nextButton.isEnabled = true
                listener?.onRadioButtonSelected(radioGroup.checkedRadioButtonId)
            }
        }
    }

    private fun setQuestionAndAnswers() {
        binding.apply {
            with(Question(questionNumber)) {
                questionView.text = getString(question)
                radioGroup.forEach { option ->
                    option as RadioButton
                    option.text = getString(answers[radioGroup.indexOfChild(option)])
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}