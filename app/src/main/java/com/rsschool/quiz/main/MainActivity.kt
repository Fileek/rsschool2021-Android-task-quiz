package com.rsschool.quiz.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import com.rsschool.quiz.result.ExitDialogFragment
import com.rsschool.quiz.question.QuizFragment
import com.rsschool.quiz.R
import com.rsschool.quiz.result.ResultsFragment

class MainActivity : AppCompatActivity(), FragmentController {

    private var fragmentsCounter = 1
    private var lastBackPressTime: Long = 0
    private var toast: Toast? = null
    private var checkedButtons: MutableMap<Int, Int> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) initVariables(savedInstanceState)
        else openQuizFragment()
    }

    private fun initVariables(savedInstanceState: Bundle) {
        with(savedInstanceState) {
            fragmentsCounter = getInt(QUESTION_NUMBER_KEY)
            @Suppress("Unchecked_cast")
            checkedButtons = get(CHECKED_BUTTONS_MAP_KEY) as MutableMap<Int, Int>
            lastBackPressTime = getLong(LAST_BACK_PRESS_TIME_KEY)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putAll(bundleOf(
            QUESTION_NUMBER_KEY to fragmentsCounter,
            CHECKED_BUTTONS_MAP_KEY to checkedButtons,
            LAST_BACK_PRESS_TIME_KEY to lastBackPressTime,
        ))
    }

    private fun openQuizFragment() {
        val quizFragment: Fragment = QuizFragment()
        quizFragment.arguments =
            bundleOf(
                QUESTION_NUMBER_KEY to fragmentsCounter,
                CHECKED_BUTTON_KEY to checkedButtons[fragmentsCounter]
            )
        supportFragmentManager.commit {
            replace(R.id.container, quizFragment)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun updateStatusBarColor(color: Int) {
        window.statusBarColor = getColor(color)
    }

    override fun onBackPressed() {
        when (fragmentsCounter) {
            1 -> showDoubleBackToastOrExit()
            in 2..7 -> onPreviousButtonClicked()
            else -> showExitDialog()
        }
    }

    private fun showDoubleBackToastOrExit() {
        if (lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this,
                getString(R.string.click_back_again_to_exit),
                Toast.LENGTH_LONG)
            toast?.show()
            lastBackPressTime = System.currentTimeMillis()
        } else {
            toast?.cancel()
            super.onBackPressed()
        }
    }

    override fun onPreviousButtonClicked() {
        fragmentsCounter--
        openQuizFragment()
    }

    override fun onRadioButtonSelected(checkedButton: Int) {
        checkedButtons[fragmentsCounter] = checkedButton
    }

    override fun onNextOrSubmitButtonClicked() {
        if (fragmentsCounter != 7) {
            fragmentsCounter++
            openQuizFragment()
        } else openResultsFragment()
    }

    private fun openResultsFragment() {
        val resultsFragment: Fragment = ResultsFragment()
        val args = bundleOf(CHECKED_BUTTONS_MAP_KEY to checkedButtons)
        resultsFragment.arguments = args
        fragmentsCounter++

        supportFragmentManager.commit { replace(R.id.container, resultsFragment) }
    }

    private fun showExitDialog() {
        val exitDialogFragment = ExitDialogFragment()
        exitDialogFragment.show(supportFragmentManager, null)
    }

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
        fragmentsCounter = 1
        checkedButtons.clear()
        openQuizFragment()
    }

    override fun onExitButtonClicked() = finish()
}