package com.rsschool.quiz

import android.os.Bundle
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
    private var correctAnswersText = ""
    private var sharedText = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        generateResult()

        listener = context as FragmentController
        binding.resultView.text = correctAnswers.toString()
        binding.textView2.text = correctAnswersText

        binding.shareButton.setOnClickListener { listener?.onShareButtonClicked(sharedText) }

        binding.backButton.setOnClickListener { listener?.onBackButtonClicked() }

        binding.exitButton.setOnClickListener { listener?.onExitButtonClicked() }
    }

    private fun generateResult() {

        @Suppress("Unchecked_cast")
        val checkedButtons = arguments?.get(CHECKED_BUTTONS_MAP_KEY) as Map<Int, Int>
        var chosenAnswers = ""

        for ((questionNumber, selectedButton) in checkedButtons) {
            with(Question(questionNumber)) {
                if (selectedButton == correctButtonId) correctAnswers++
                chosenAnswers += """
                    |
                    |
                    |$questionNumber) ${getString(question)}
                    |${getString(R.string.your_answer)} ${getString(answersMap[selectedButton] ?: -1)}""".trimMargin()
            }
        }

        /** Choose right russian localization for words "correct" and "answers" */
        correctAnswersText = "${
            when (correctAnswers) {
                1 -> "${getString(R.string.correct_if_1)} ${getString(R.string.answers_if_1)}"
                in 2..4 -> "${getString(R.string.correct_default)} ${getString(R.string.answers_if_2__4)}"
                else -> "${getString(R.string.correct_default)} ${getString(R.string.answers_default)}"
            }
        } ${getString(R.string.out_of_7)}"

        sharedText =
            "${getString(R.string.your_result)} $correctAnswers $correctAnswersText $chosenAnswers"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}