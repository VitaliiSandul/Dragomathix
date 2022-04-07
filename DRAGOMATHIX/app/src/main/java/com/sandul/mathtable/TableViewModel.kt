package com.sandul.mathtable

import androidx.lifecycle.ViewModel

class TableViewModel  : ViewModel() {
    val tables = mutableListOf<MultTable>()

    init {
        for (i in 1 until 9) {
            val table = MultTable()
            table.number = i
            table.progress = 0
            table.isVisible = i != 0
        }
    }

    var currentIndex = 0
    private var questionBank = listOf(

        Question("1 x 1 = ", 8, 1,4,6,1, 1),
        Question("1 x 2 = ", 3, 8,5,2,2, 1),
        Question("1 x 3 = ", 1, 3,6,30,3, 1),
        Question("1 x 4 = ", 12, 6,4,2,4, 1),
        Question("1 x 5 = ", 6, 1,5,4,5, 1),
        Question("1 x 6 = ", 7, 5,9,6,6, 1),
        Question("1 x 7 = ", 7, 2,17,8,7, 1),
        Question("1 x 8 = ", 5, 8,3,7,8, 1),
        Question("1 x 9 = ", 19, 10,9,1,9, 1),
        Question("1 x 10 = ", 10, 3,12,13,10, 1),

        Question("2 x 1 = ", 21, 3,4,2,2, 2),
        Question("2 x 2 = ", 8, 13,4,7,4, 2),
        Question("2 x 3 = ", 6, 3,2,4,6, 2),
        Question("2 x 4 = ", 1, 6,8,10,8, 2),
        Question("2 x 5 = ", 10, 13,15,17,10, 2),
        Question("2 x 6 = ", 6, 3,12,8,12, 2),
        Question("2 x 7 = ", 12, 14,5,17,14, 2),
        Question("2 x 8 = ", 20, 13,15,16,16, 2),
        Question("2 x 9 = ", 18, 31,24,27,18, 2),
        Question("2 x 10 = ", 20, 43,22,18,20, 2),

        Question("3 x 1 = ", 30, 3,35,71,3, 3),
        Question("3 x 2 = ", 32, 66,16,6,6, 3),
        Question("3 x 3 = ", 8, 43,55,9,9, 3),
        Question("3 x 4 = ", 12, 43,34,7,12, 3),
        Question("3 x 5 = ", 50, 35,15,8,15, 3),
        Question("3 x 6 = ", 21, 17,51,18,18, 3),
        Question("3 x 7 = ", 20, 21,65,72,21, 3),
        Question("3 x 8 = ", 24, 42,38,83,24, 3),
        Question("3 x 9 = ", 40, 33,25,27,27, 3),
        Question("3 x 10 = ", 20, 30,50,10,30, 3),

        Question("4 x 1 = ", 10, 4,53,47,4, 4),
        Question("4 x 2 = ", 8, 43,45,42,8, 4),
        Question("4 x 3 = ", 40, 34,12,17,12, 4),
        Question("4 x 4 = ", 44, 53,75,16,16, 4),
        Question("4 x 5 = ", 20, 25,35,40,20, 4),
        Question("4 x 6 = ", 10, 32,24,37,24, 4),
        Question("4 x 7 = ", 11, 47,55,28,28, 4),
        Question("4 x 8 = ", 30, 32,55,24,32, 4),
        Question("4 x 9 = ", 29, 36,49,27,36, 4),
        Question("4 x 10 = ", 40, 30,50,14,40, 4),

        Question("5 x 1 = ", 50, 15,5,51,5, 5),
        Question("5 x 2 = ", 10, 25,30,13,10, 5),
        Question("5 x 3 = ", 24, 35,15,17,15, 5),
        Question("5 x 4 = ", 8, 33,52,20,20, 5),
        Question("5 x 5 = ", 10, 25,35,45,25, 5),
        Question("5 x 6 = ", 0, 30,5,7,30, 5),
        Question("5 x 7 = ", 0, 35,5,7,35, 5),
        Question("5 x 8 = ", 22, 4,85,40,40, 5),
        Question("5 x 9 = ", 50, 47,45,46,45, 5),
        Question("5 x 10 = ", 60, 50,5,15,50, 5),

        Question("6 x 1 = ", 61, 6,15,17,6, 6),
        Question("6 x 2 = ", 18, 30,15,12,12, 6),
        Question("6 x 3 = ", 36, 30,18,27,18, 6),
        Question("6 x 4 = ", 24, 46,31,49,24, 6),
        Question("6 x 5 = ", 20, 30,40,50,30, 6),
        Question("6 x 6 = ", 40, 63,35,36,36, 6),
        Question("6 x 7 = ", 14, 38,50,42,42, 6),
        Question("6 x 8 = ", 60, 48,55,77,48, 6),
        Question("6 x 9 = ", 26, 39,54,47,54, 6),
        Question("6 x 10 = ", 60, 28,54,72,60, 6),

        Question("7 x 1 = ", 21, 31,8,7,7, 7),
        Question("7 x 2 = ", 14, 17,27,10,14, 7),
        Question("7 x 3 = ", 37, 25,21,24,21, 7),
        Question("7 x 4 = ", 11, 74,15,28,28, 7),
        Question("7 x 5 = ", 75, 23,35,72,35, 7),
        Question("7 x 6 = ", 42, 66,13,27,42, 7),
        Question("7 x 7 = ", 1, 99,35,49,49, 7),
        Question("7 x 8 = ", 2, 62,56,75,56, 7),
        Question("7 x 9 = ", 9, 63,45,79,63, 7),
        Question("7 x 10 = ", 80, 17,12,70,70, 7),

        Question("8 x 1 = ", 18, 8,34,81,8, 8),
        Question("8 x 2 = ", 16, 10,15,17,16, 8),
        Question("8 x 3 = ", 4, 56,24,27,24, 8),
        Question("8 x 4 = ", 32, 48,65,77,32, 8),
        Question("8 x 5 = ", 50, 30,40,27,40, 8),
        Question("8 x 6 = ", 86, 64,32,48,48, 8),
        Question("8 x 7 = ", 15, 38,56,67,56, 8),
        Question("8 x 8 = ", 64, 16,36,42,64, 8),
        Question("8 x 9 = ", 65, 28,17,72,72, 8),
        Question("8 x 10 = ", 80,75,64,19,80, 8),

        Question("9 x 1 = ", 36, 18,9,27,9, 9),
        Question("9 x 2 = ", 7, 92,11,18,18, 9),
        Question("9 x 3 = ", 18, 27,52,72,27, 9),
        Question("9 x 4 = ", 36, 13,35,72,36, 9),
        Question("9 x 5 = ", 14, 38,45,67,45, 9),
        Question("9 x 6 = ", 54, 3,15,96,54, 9),
        Question("9 x 7 = ", 20, 63,79,16,63, 9),
        Question("9 x 8 = ", 70, 1,17,72,72, 9),
        Question("9 x 9 = ", 81, 18,39,72,81, 9),
        Question("9 x 10 = ", 70, 100,80,90,90, 9),

        Question("10 x 1 = ", 1, 10,20,101,10, 10),
        Question("10 x 2 = ", 22, 12,20,35,20, 10),
        Question("10 x 3 = ", 13, 30,15,40,30, 10),
        Question("10 x 4 = ", 40, 14,25,55,40, 10),
        Question("10 x 5 = ", 25, 15,50,19,50, 10),
        Question("10 x 6 = ", 16, 36,45,60,60, 10),
        Question("10 x 7 = ", 20, 10,17,70,70, 10),
        Question("10 x 8 = ", 81, 8,80,18,80, 10),
        Question("10 x 9 = ", 19, 90,21,36,90, 10),
        Question("10 x 10 = ", 100, 30,20,2,100, 10)
    )

    val currentQuestionCategory: Int
        get() = questionBank[currentIndex].category

    val currentQuestionAnswer: Int
        get() = questionBank[currentIndex].answer

    val currentQuestionVar1: Int
        get() = questionBank[currentIndex].var1

    val currentQuestionVar2: Int
        get() = questionBank[currentIndex].var2

    val currentQuestionVar3: Int
        get() = questionBank[currentIndex].var3

    val currentQuestionVar4: Int
        get() = questionBank[currentIndex].var4

    val currentQuestionText: String
        get() = questionBank[currentIndex].questionText

    val questionBankSize: Int
        get() = questionBank.size

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun shuffleQuestionBank() { 
        questionBank = questionBank.shuffled()
    }
}