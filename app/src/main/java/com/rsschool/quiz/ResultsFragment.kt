package com.rsschool.quiz

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentResultsBinding

class ResultsFragment : Fragment() {

    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!
    private var listener: FragmentController? = null
    private var correctAnswers = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        listener = context as FragmentController
        _binding = FragmentResultsBinding.inflate(inflater, container, false)

        setTheme()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedAnswers = calculateAndReturnSelected()
        val correctAnswersText = selectCorrectAnswersText()
        val resultsText =
            "${getString(R.string.your_result)} $correctAnswers $correctAnswersText $selectedAnswers"

        binding.apply {
            resultView.text = correctAnswers.toString()
            textView2.text = correctAnswersText

            shareButton.setOnClickListener { listener?.onShareButtonClicked(resultsText) }

            backButton.setOnClickListener { listener?.onBackButtonClicked() }

            exitButton.setOnClickListener { listener?.onExitButtonClicked() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Choose status bar color depends on day/night mode on device */
    private fun setTheme() {
        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> listener?.updateStatusBarColor(R.color.black)
            Configuration.UI_MODE_NIGHT_NO -> listener?.updateStatusBarColor(R.color.white)
            else -> listener?.updateStatusBarColor(R.color.lavender_gray)
        }
    }

    /** Choose right russian localization for words "correct" and "answers" */
    private fun selectCorrectAnswersText(): String {
        return "${
            when (correctAnswers) {
                1 -> "${getString(R.string.correct_if_1)} ${getString(R.string.answers_if_1)}"
                in 2..4 -> "${getString(R.string.correct_default)} ${getString(R.string.answers_if_2__4)}"
                else -> "${getString(R.string.correct_default)} ${getString(R.string.answers_default)}"
            }
        } ${getString(R.string.out_of_7)}"
    }

    /** Calculate correct answers and return selected answers */
    private fun calculateAndReturnSelected(): String {
        @Suppress("Unchecked_cast")
        val checkedButtons = arguments?.get(CHECKED_BUTTONS_MAP_KEY) as Map<Int, Int>
        var selectedAnswers = ""

        for ((questionNumber, selectedButton) in checkedButtons) {
            with(Question(questionNumber)) {
                if (selectedButton == correctButtonId) correctAnswers++
                selectedAnswers += """
                    |
                    |
                    |$questionNumber) ${getString(question)}
                    |${getString(R.string.your_answer)} ${getString(answersMap[selectedButton] ?: -1)}""".trimMargin()
            }
        }
        return selectedAnswers
    }
}