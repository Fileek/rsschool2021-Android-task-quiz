package com.rsschool.quiz

import android.os.Build
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private var listener: FragmentController? = null
    private var questionNumber = 0
    private val colors =
        listOf(
            R.color.lust_dark,
            R.color.green_blue_dark,
            R.color.green_dark,
            R.color.icterine_dark,
            R.color.middle_green_dark,
            R.color.jonquil_dark,
            R.color.lavender_gray_dark
        )
    private val themes =
        listOf(
            R.style.Theme_Quiz_First,
            R.style.Theme_Quiz_Second,
            R.style.Theme_Quiz_Third,
            R.style.Theme_Quiz_Fourth,
            R.style.Theme_Quiz_Fifth,
            R.style.Theme_Quiz_Sixth,
            R.style.Theme_Quiz_Seventh
        )

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        listener = context as FragmentController
        questionNumber = arguments?.getInt(QUESTION_NUMBER_KEY) ?: 0
        val themedInflater = inflater.cloneInContext(
            ContextThemeWrapper(activity, themes[questionNumber - 1])
        )
        _binding = FragmentQuizBinding.inflate(themedInflater, container, false)

        listener?.updateStatusBarColor(colors[questionNumber - 1])

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val questionObj = Question(questionNumber)
        val checkedButtonId = arguments?.getInt(CHECKED_BUTTON_KEY, -1) ?: -1

        with(binding) {
            toolbar.title = "Question $questionNumber"
            toolbar.setNavigationOnClickListener { listener?.onPreviousButtonClicked() }

            when (questionNumber) {
                1 -> {
                    toolbar.navigationIcon = null
                    previousButton.isInvisible = true
                }
                7 -> nextButton.text = getString(R.string.submit)
            }

            if (checkedButtonId == -1) nextButton.isEnabled = false
            else radioGroup.check(checkedButtonId)

            radioGroup.setOnCheckedChangeListener { _, _ ->
                nextButton.isEnabled = true
                listener?.onRadioButtonSelected(radioGroup.checkedRadioButtonId)
            }

            with(questionObj) {
                questionView.text = getString(question)
                optionOne.text = getString(answers[0])
                optionTwo.text = getString(answers[1])
                optionThree.text = getString(answers[2])
                optionFour.text = getString(answers[3])
                optionFive.text = getString(answers[4])
            }

            previousButton.setOnClickListener { listener?.onPreviousButtonClicked() }

            nextButton.setOnClickListener { listener?.onNextOrSubmitButtonClicked() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}