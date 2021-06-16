package com.rsschool.quiz

interface FragmentController {

    fun updateStatusBarColor(color: Int)

    fun onRadioButtonSelected(checkedButton: Int)

    fun onPreviousButtonClicked()

    fun onNextOrSubmitButtonClicked()

    fun onShareButtonClicked(sharedText: String)

    fun onBackButtonClicked()

    fun onExitButtonClicked()
}