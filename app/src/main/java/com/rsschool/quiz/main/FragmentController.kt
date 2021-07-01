package com.rsschool.quiz.main

interface FragmentController {

    fun updateStatusBarColor(color: Int)

    fun onPreviousButtonClicked()

    fun onRadioButtonSelected(checkedButton: Int)

    fun onNextOrSubmitButtonClicked()

    fun onShareButtonClicked(resultsText: String)

    fun onBackButtonClicked()

    fun onExitButtonClicked()
}