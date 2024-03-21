package com.example.calculatorapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val operators = arrayOf("+", "-", "/", "*")
    private var tvResult: TextView? = null

    private var lastIsNumeric: Boolean = false
    private var lastIsDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvResult = findViewById(R.id.tvResult)
    }

    private fun hasOperator(value: String): Boolean {
        var result = false

        if (value.startsWith("-")) {
            result = false
        } else {
            operators.forEach {
                if (value.contains(it)) {
                    result = true
                }
            }
        }

        return result
    }

    fun onClear(view: View) {
        tvResult?.text = ""
    }

    fun onDecimalPoint(view: View) {
        if (lastIsNumeric && !lastIsDot) {
            tvResult?.append(".")

            lastIsDot = true
            lastIsNumeric = false
        }
    }

    fun onDigit(view: View) {
        lastIsNumeric = true
        lastIsDot = false

        val value = (view as Button).text
        tvResult?.append(value)
    }

    fun onEqual(view: View) {
        if (lastIsNumeric) {
            var tvValue = tvResult?.text.toString()

            var prefix = ""
            var result = ""

            try {
                if (tvValue.startsWith("-")) {
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }

                if (tvValue.contains("-")) {
                    val splitValue = tvValue.split("-")

                    var firstValue = splitValue.elementAt(0)
                    val secondValue = splitValue.elementAt(1)

                    if (prefix.isNotEmpty()) {
                        firstValue = prefix + firstValue
                    }

                    result =
                        makeWholeNumber((firstValue.toDouble() - secondValue.toDouble()).toString())
                } else if (tvValue.contains("+")) {
                    val splitValue = tvValue.split("+")

                    val firstValue = splitValue.elementAt(0)
                    val secondValue = splitValue.elementAt(1)

                    result =
                        makeWholeNumber((firstValue.toDouble() + secondValue.toDouble()).toString())
                } else if (tvValue.contains("/")) {
                    val splitValue = tvValue.split("/")

                    val firstValue = splitValue.elementAt(0)
                    val secondValue = splitValue.elementAt(1)

                    result =
                        makeWholeNumber((firstValue.toDouble() / secondValue.toDouble()).toString())
                } else if (tvValue.contains("*")) {
                    val splitValue = tvValue.split("*")

                    val firstValue = splitValue.elementAt(0)
                    val secondValue = splitValue.elementAt(1)

                    result =
                        makeWholeNumber((firstValue.toDouble() * secondValue.toDouble()).toString())

                }

                tvResult?.text = result
            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    fun onOperator(view: View) {
        val value = (view as Button).text

        tvResult?.text?.let {
            if (lastIsNumeric && !hasOperator(it.toString())) {
                tvResult?.append(value)

                lastIsNumeric = false
                lastIsDot = false
            }
        }
    }

    private fun makeWholeNumber(value: String): String {
        var result = value

        if (value.contains(".0")) {
            result = value.substring(0, value.length - 2)
        }

        return result
    }
}
