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
    private var checkedButtons: MutableMap<Int, Int> = mutableMapOf()
    private var starterIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        starterIntent = intent
        setContentView(R.layout.activity_main)
        openQuizFragment()
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
                Pair(QUESTION_NUMBER_KEY, ++fragmentsCounter),
                Pair(CHECKED_BUTTON_KEY, checkedButtons[fragmentsCounter])
            )

        supportFragmentManager.commit {
            replace(R.id.container, quizFragment)
            if (addToBackStack) addToBackStack(null) else addToBackStack = true
        }
    }

    private fun openResultsFragment() {
        val resultsFragment: Fragment = ResultsFragment()
        val args = bundleOf(Pair(CHECKED_BUTTONS_MAP_KEY, checkedButtons))
        resultsFragment.arguments = args
        fragmentsCounter++

        supportFragmentManager.commit { replace(R.id.container, resultsFragment) }
    }

    /** Methods below are calling by QuizFragment */

    @RequiresApi(Build.VERSION_CODES.M)
    override fun updateStatusBarColor(color: Int) {
        window.statusBarColor = getColor(color)
    }

    override fun onRadioButtonSelected(checkedButton: Int) {
        checkedButtons[fragmentsCounter] = checkedButton
    }

    override fun onPreviousButtonClicked() {
        fragmentsCounter--
        this.supportFragmentManager.popBackStack()
    }

    override fun onNextOrSubmitButtonClicked() {
        if (fragmentsCounter != 7) openQuizFragment() else openResultsFragment()
    }

    /** Methods below are calling by ResultsFragment */

    override fun onShareButtonClicked(sharedText: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, sharedText)
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onBackButtonClicked() {
        finish()
        startActivity(starterIntent)
        overridePendingTransition(0, 0)
    }

    override fun onExitButtonClicked() = finish()
}