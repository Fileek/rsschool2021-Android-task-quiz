package com.rsschool.quiz

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.*

class MainActivity : AppCompatActivity(), FragmentController {

    private var fragmentsCounter = 0
    private var addToBackStack = false
    private var checkedButtons: MutableMap<Int, Int> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        with(savedInstanceState) {
            if (this != null) {
                fragmentsCounter = getInt(QUESTION_NUMBER_KEY)
                addToBackStack = getBoolean(ADD_TO_BACKSTACK_KEY)
                @Suppress("Unchecked_cast")
                checkedButtons = get(CHECKED_BUTTONS_MAP_KEY) as MutableMap<Int, Int>
            } else
                openQuizFragment()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putAll(bundleOf(
            QUESTION_NUMBER_KEY to fragmentsCounter,
            ADD_TO_BACKSTACK_KEY to addToBackStack,
            CHECKED_BUTTONS_MAP_KEY to checkedButtons,
        ))
    }

    override fun onBackPressed() {
        when (fragmentsCounter) {
            1 -> super.onBackPressed()
            in 2..7 -> onPreviousButtonClicked()
            else -> onBackButtonClicked()
        }
    }

    private fun openQuizFragment() {
        val quizFragment: Fragment = QuizFragment()
        quizFragment.arguments =
            bundleOf(
                QUESTION_NUMBER_KEY to ++fragmentsCounter,
                CHECKED_BUTTON_KEY to checkedButtons[fragmentsCounter]
            )

        supportFragmentManager.commit {
            replace(R.id.container, quizFragment)
            if (addToBackStack) addToBackStack(null) else addToBackStack = true
        }
    }

    private fun openResultsFragment() {
        val resultsFragment: Fragment = ResultsFragment()
        val args = bundleOf(CHECKED_BUTTONS_MAP_KEY to checkedButtons)
        resultsFragment.arguments = args
        fragmentsCounter++

        supportFragmentManager.commit { replace(R.id.container, resultsFragment) }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun updateStatusBarColor(color: Int) {
        window.statusBarColor = getColor(color)
    }

    // Methods below are calling by QuizFragment

    override fun onRadioButtonSelected(checkedButton: Int) {
        checkedButtons[fragmentsCounter] = checkedButton
    }

    override fun onPreviousButtonClicked() {
        fragmentsCounter--
        supportFragmentManager.popBackStack()
    }

    override fun onNextOrSubmitButtonClicked() {
        if (fragmentsCounter != 7) openQuizFragment() else openResultsFragment()
    }

    // Methods below are calling by ResultsFragment

    override fun onShareButtonClicked(resultsText: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, resultsText)
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onBackButtonClicked() {
        while (--fragmentsCounter != 0) supportFragmentManager.popBackStack()
        checkedButtons.clear()
        addToBackStack = false
        openQuizFragment()
    }

    override fun onExitButtonClicked() = finish()
}