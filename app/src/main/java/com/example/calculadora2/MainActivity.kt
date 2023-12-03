package com.example.calculadora2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var txt_num1: TextView // workingsTV
    lateinit var txt_num2: TextView // resultsTV

    private var addOperation = false
    private var addDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txt_num1 = findViewById(R.id.workingsTV)
        txt_num2 = findViewById(R.id.resultsTV)
    }

    fun numberAction (view: View) {
        if(view is Button){
            if(view.text == "."){
                if(addDecimal){
                    txt_num1.append(view.text)
                }
                addDecimal = false
            } else {
                txt_num1.append(view.text)

            }
            addOperation = true
        }
    }

    fun operationAction (view: View) {
        if(view is Button && addOperation) {
            txt_num1.append(view.text)
            addOperation = false
            addDecimal = true
        }
    }

    fun allClearAction (view: View) {
        txt_num1.text = ""
        txt_num2.text = ""
    }

    fun eraseAction (view: View) {
       val length = txt_num1.length()
       if(length > 0) {
           txt_num1.text = txt_num1.text.subSequence(0, length - 1)
       }
    }

    fun equalsAction (view: View) {
        txt_num2.text = calcularResultado()
    }

    private fun calcularResultado () : String {
        val digitsOperator = digitsOperators()
        if (digitsOperator.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperator)
        if (timesDivision.isEmpty()) return ""

        val result = addSubstractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubstractCalculate (passedList: MutableList<Any>) : Float {
        var result = passedList[0] as Float
        for (i in passedList.indices) {
            if(passedList[i] is Char && i != passedList.lastIndex) {
                val operador = passedList[i]
                val nextDigit = passedList[i + 1] as Float

                if(operador == '+')
                    result += nextDigit

                if(operador == '-')
                    result -= nextDigit
            }
        }

        return result
    }

    private fun timesDivisionCalculate (passedList: MutableList<Any>) : MutableList<Any> {
        var list = passedList
        while( list.contains('x') || list.contains('/') ){
            list = calculateTimesDiv(list)
        }

        return list
    }

    private fun calculateTimesDiv (passedList: MutableList<Any>) : MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size
        for (i in passedList.indices) {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float

                when(operator) {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }

            }

            if(i > restartIndex) {
                newList.add(passedList)
            }
        }

        return newList
    }

    private fun digitsOperators() : MutableList<Any> {
        val lista = mutableListOf<Any>()
        var currentDigit = ""

        for(character in txt_num1.text) {
            if (character.isDigit() || character == '.') {
                currentDigit += character
            } else {
                lista.add(currentDigit.toFloat())
                currentDigit = ""
                lista.add(character)
            }
        }

        if(currentDigit != "") {
            lista.add(currentDigit.toFloat())
        }

        return lista
    }
}