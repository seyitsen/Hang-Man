package com.example.hangman

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hangman.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var falseCount=0
    private var gameOverFlag=true
    private lateinit var word:String
    private lateinit var targetWord:String
    private lateinit var indexes:MutableList<Int>
    private var randomNumber=0
    val words=Words

    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        startGame()
        for(letter in 'a'..'z'){
            val buttonId=resources.getIdentifier(letter.toString(),"id",packageName)
            val button=findViewById<View>(buttonId)

            button.setOnClickListener{
                indexes=findIndexes(binding,word,letter)
                targetWord=displayLetters(indexes,targetWord,letter)
                button.visibility=View.GONE
            }
        }

        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        */

    }

    private fun startGame(){
        callBackButtons()
        falseCount=0
        binding.hangman.setImageResource(0)
        randomNumber= Random.nextInt(0,320)
        word=words.DICTIONARY[randomNumber]
        createBlands(word.length,binding)
        targetWord=binding.word.text.toString()
    }

    @SuppressLint("DiscouragedApi")
    private fun callBackButtons() {
        for(letter in 'a'..'z'){
            val buttonId=resources.getIdentifier(letter.toString(),"id",packageName)
            val button=findViewById<View>(buttonId)

            button.visibility=View.VISIBLE
        }
    }

    private fun createBlands(size:Int,binding: ActivityMainBinding){
        binding.word.text="_ ".repeat(size)
    }

    private fun findIndexes(binding: ActivityMainBinding,word:String,letter:Char):MutableList<Int> {
        val indexes = mutableListOf<Int>()
        word.mapIndexed { index, char ->
            if (char == letter)
                indexes.add(index)
        }
        if(indexes.size==0){
            if(falseCount==10){
                gameOverFlag=false
                showAlertDialog(gameOverFlag)
            }
            falseCount++
            updateImage(binding,falseCount)
        }
        return indexes
    }

    @SuppressLint("DiscouragedApi")
    private fun updateImage(binding: ActivityMainBinding, falseCount: Int) {
        val imageName="hangman_$falseCount"
        val imageResourceId=resources.getIdentifier(imageName,"drawable",packageName)
        binding.hangman.setImageResource(imageResourceId)
    }

    private fun displayLetters(indexes:MutableList<Int>,targetWord:String,letter:Char):String{
        val stringBuilder=StringBuilder(targetWord)
        if(indexes.size>0){
            indexes.map { index-> stringBuilder.setCharAt(index*2,letter.uppercaseChar())}
            binding.word.text=stringBuilder.toString()
        }
        if(!stringBuilder.contains("_")) {
            gameOverFlag=true
            showAlertDialog(gameOverFlag)
        }
        return stringBuilder.toString()
    }

    private fun showAlertDialog(gameOverFlag: Boolean) {
        val builder=AlertDialog.Builder(this)
        builder.setCancelable(false)
        if(gameOverFlag){
            builder.setTitle("YOU WON")
            builder.setMessage("Congrats! You Won The Game")

            builder.setPositiveButton("Play Again"){dialog,which->startGame()}
            builder.setNegativeButton("Exit"){dialog,which->System.exit(0)}
        }
        else{
            builder.setTitle("GAME OVER")
            builder.setMessage("You Lost The Game. The word was ${word.uppercase()}")

            builder.setPositiveButton("Play Again"){dialog,which->startGame()}
            builder.setNegativeButton("Exit"){dialog,which->System.exit(0)}
        }
        builder.show()
    }

}