package com.example.company.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import java.lang.Math.abs


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val statusText = findViewById<TextView>(R.id.status)
        val listOfSpinners = listOf<Spinner>(
            findViewById(R.id.spinner11),
            findViewById(R.id.spinner12),
            findViewById(R.id.spinner13),
            findViewById(R.id.spinner21),
            findViewById(R.id.spinner22),
            findViewById(R.id.spinner23),
            findViewById(R.id.spinner31),
            findViewById(R.id.spinner32),
            findViewById(R.id.spinner33)
        )
        val checkerItems: ICheckerItems = ItemsChecker(listOfSpinners)
        val itemSelected = OnItemCustomSelection(checkerItems, statusText)
        listOfSpinners.forEach {
            it.onItemSelectedListener = itemSelected

        }
    }
}

class OnItemCustomSelection(private val checkerItem: ICheckerItems, private val resultTextView: TextView) :
    AdapterView.OnItemSelectedListener {
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (val checkedItem = checkerItem.checkItems()) {
            is GameResult.Win -> {

                p1?.showToast("${checkedItem.winner} won")
            }
            is GameResult.Draw -> p1?.showToast("Draw")
            is GameResult.Invalid -> p1?.showToast("Invalid")
        }
    }

    private fun View.showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        resultTextView.text =message

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}

sealed class GameResult {
    class Win(val winner: String) : GameResult()
    object Draw : GameResult()
    object Invalid : GameResult()
    object Nothing : GameResult()

}

interface ICheckerItems {
    fun checkItems(): GameResult
}

class ItemsChecker(private val listOfSpinners: List<Spinner>) : ICheckerItems {
    override fun checkItems(): GameResult {
        return checkerLine(toLine(listOfSpinners))
    }


}
fun toLine(listOfSpinners: List<Spinner>): String{
    var line = ""
    listOfSpinners.forEach {
        when (it.selectedItem.toString()) {
            "0" -> line += "1"
            "X" -> line += "2"
            "" -> line += "0"
        }

    }
    return line
}
fun checkerLine(line: String): GameResult {
    var sumN = 0
    var sumX = 0
    var gameResult: GameResult = GameResult.Nothing
    line.forEach {
        if (it.toString() == "1") sumN++
        if (it.toString() == "2") sumX++
    }
    gameResult = if (kotlin.math.abs(sumN - sumX) <= 1){
        Log.d("Tag", "${(sumN - sumX)}")
        when {
            checkWin(line, "1") -> {
                GameResult.Win("0")
            }
            checkWin(line, "2") -> {
                GameResult.Win("X")
            }
            ((sumX+sumN)==9)->GameResult.Draw
            else -> GameResult.Nothing
        }
    } else GameResult.Invalid
     Log.d("Tag","$gameResult")
    return gameResult
}

fun checkWin(line: String, n: String): Boolean {    //Проверка на победу
    val a =((line[0] == line[1] && line[2] == line[1]) && line[0].toString() == n ||
            (line[0] == line[3] && line[6] == line[0]) && line[0].toString() == n ||
            (line[0] == line[4] && line[4] == line[8]) && line[0].toString() == n ||
            (line[1] == line[4] && line[1] == line[7]) && line[1].toString() == n ||
            (line[2] == line[4] && line[2] == line[6]) && line[2].toString() == n ||
            (line[2] == line[5] && line[2] == line[8]) && line[2].toString() == n ||
            (line[6] == line[7] && line[6] == line[8]) && line[6].toString() == n ||
            (line[3] == line[4] && line[5] == line[3]) && line[3].toString() == n)
    Log.d("Tag","$a")
    return a
}


