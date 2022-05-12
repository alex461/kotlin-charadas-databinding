package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    // The current word
    private val _word = MutableLiveData<String>()
     val word : LiveData<String> get() =  _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score:LiveData<Int> = _score

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish : LiveData<Boolean> = _eventGameFinish

    private val _currentTime = MutableLiveData<Long>()
    val currentTime : LiveData<Long> = _currentTime

    private val timer: CountDownTimer

    // The list of words - the front of the list is the next word to guess
    private  lateinit var wordList: MutableList<String>



    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    init {

        _word.value = ""
        _score.value = 0


        resetList()
        nextWord()
        Log.i("GameViewModel", "GameViewModel created!")


        timer =object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){

            override fun onTick(millisUntilFinished: Long) {

                _currentTime.value = millisUntilFinished/ ONE_SECOND

            }

            override fun onFinish() {

                _currentTime.value = DONE
                onGameFinish()

            }


        }

        timer.start()

    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        if (wordList.isEmpty()) {
            resetList()
        }else{
            //Select and remove a word from the list
            _word.value = wordList.removeAt(0)
        }

    }


    /** Methods for buttons presses **/

     fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

     fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinish(){
        _eventGameFinish.value = true

    }


    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()

    }


    val currentTimeString = Transformations.map(currentTime){ tiempo ->

        DateUtils.formatElapsedTime(tiempo)
    }


    val wordHint = Transformations.map(word){ palabra ->

        val randomPosition = (1..palabra.length).random()

        "Current word has " + palabra.length + " letters" +
                "\nThe letter at position " + randomPosition + " is " +
                palabra.get(randomPosition - 1).toUpperCase()

    }

    companion object{


    private const val DONE =0L

    private const val ONE_SECOND = 1000L

    private const val COUNTDOWN_TIME = 6000L

}


}


