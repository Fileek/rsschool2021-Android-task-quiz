package com.rsschool.quiz

data class Question(val questionNumber: Int) {

    private val questions =
        listOf(
            R.string.first_question,
            R.string.second_question,
            R.string.third_question,
            R.string.fourth_question,
            R.string.fifth_question,
            R.string.sixth_question,
            R.string.seventh_question,
        )
    private val answersArray =
        arrayOf(
            listOf(
                R.string.first_question_answer_one,
                R.string.first_question_answer_two,
                R.string.first_question_answer_three,
                R.string.first_question_answer_four,
                R.string.first_question_answer_five,
            ),
            listOf(
                R.string.second_question_answer_one,
                R.string.second_question_answer_two,
                R.string.second_question_answer_three,
                R.string.second_question_answer_four,
                R.string.second_question_answer_five,
            ),
            listOf(
                R.string.third_question_answer_one,
                R.string.third_question_answer_two,
                R.string.third_question_answer_three,
                R.string.third_question_answer_four,
                R.string.third_question_answer_five,
            ),
            listOf(
                R.string.fourth_question_answer_one,
                R.string.fourth_question_answer_two,
                R.string.fourth_question_answer_three,
                R.string.fourth_question_answer_four,
                R.string.fourth_question_answer_five,
            ),
            listOf(
                R.string.fifth_question_answer_one,
                R.string.fifth_question_answer_two,
                R.string.fifth_question_answer_three,
                R.string.fifth_question_answer_four,
                R.string.fifth_question_answer_five,
            ),
            listOf(
                R.string.sixth_question_answer_one,
                R.string.sixth_question_answer_two,
                R.string.sixth_question_answer_three,
                R.string.sixth_question_answer_four,
                R.string.sixth_question_answer_five,
            ),
            listOf(
                R.string.seventh_question_answer_one,
                R.string.seventh_question_answer_two,
                R.string.seventh_question_answer_three,
                R.string.seventh_question_answer_four,
                R.string.seventh_question_answer_five,
            )
        )

    val question: Int get() = questions[questionNumber - 1]
    val answers: List<Int> get() = answersArray[questionNumber - 1]
    val answersMap: Map<Int, Int>
        get() = mapOf(
            Pair(R.id.option_one, answers[0]),
            Pair(R.id.option_two, answers[1]),
            Pair(R.id.option_three, answers[2]),
            Pair(R.id.option_four, answers[3]),
            Pair(R.id.option_five, answers[4]),
        )
    val correctButtonId
        get() =
            when (questionNumber) {
                1 -> R.id.option_four
                2 -> R.id.option_three
                3 -> R.id.option_two
                4 -> R.id.option_one
                5 -> R.id.option_five
                6 -> R.id.option_four
                7 -> R.id.option_two
                else -> -1
            }
}
