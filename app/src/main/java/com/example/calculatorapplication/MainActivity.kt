package com.example.calculatorapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculatorapplication.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    var lastNumeric=false;
    var stateError=false;
    var lastDot=false;
    private lateinit var expression:Expression
    private lateinit var binding:ActivityMainBinding//declaring a variable named binding inheriting the activityMainBinding Class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        // Here ActivityMainBinding.inflate() is a method used to create an instance of the ActivityMainBinding class, which is a generated binding class specifically for the activity_main.xml layout file
        setContentView(binding.root)
    }

    fun onEqualClick(view: View) {
        onEqual()
        binding.dataTv.text =binding.resultTv.text.toString().drop(1)
    }
    fun onDigitClick(view: View) {
        if(stateError){
            binding.dataTv.text=(view as Button).text
            stateError=false
        }else{
            binding.dataTv.append((view as Button).text)
        }
        lastNumeric=true
        onEqual()
    }
    fun onAllClearClick(view: View) {
        binding.dataTv.text=""
        binding.resultTv.text=""
        stateError=false
        lastDot=false
        lastNumeric=false
        binding.resultTv.visibility=View.GONE
    }
    fun onOperatorClick(view: View) {
        if(!stateError&&lastNumeric){
            binding.dataTv.append((view as Button).text)
            lastDot=false
            lastNumeric=false
            onEqual()
        }
    }

    fun onBackClick(view: View) {
        binding.dataTv.text=binding.dataTv.text.toString().dropLast(1)
        try {
            var lastChar=binding.dataTv.text.toString().last()
            if(lastChar.isDigit()){
                onEqual()
            }
        }catch (e:Exception){
            binding.resultTv.text=""
            binding.resultTv.visibility=View.GONE
            Log.e("last char error",e.toString())
        }
    }
    fun onClearClick(view: View) {
        binding.dataTv.text=""
        lastNumeric=false
    }
    fun onEqual(){
        if(lastNumeric&&!stateError){
            val txt=binding.dataTv.text.toString()
            expression=ExpressionBuilder(txt).build()
            try{
                val result=expression.evaluate();
                binding.resultTv.visibility=View.VISIBLE
                binding.resultTv.text="="+result.toString()
            }catch (ex:ArithmeticException){
                Log.e("Evaluate Errors",ex.toString())
                binding.resultTv.text="ERROR"
                stateError=true
                lastNumeric=false
            }
        }
    }
}