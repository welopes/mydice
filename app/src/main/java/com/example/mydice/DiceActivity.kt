package com.example.mydice

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mydice.viewModel.DiceViewModel
import kotlinx.android.synthetic.main.activity_dice.*
import kotlinx.android.synthetic.main.content_dice.*

class DiceActivity : AppCompatActivity() {

    private lateinit var viewModel: DiceViewModel

    private val imageViews by lazy {
        arrayOf<ImageView>(
            findViewById(R.id.die1),
            findViewById(R.id.die2),
            findViewById(R.id.die3),
            findViewById(R.id.die4),
            findViewById(R.id.die5)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_check)

        viewModel = ViewModelProvider(this).get(DiceViewModel::class.java)

        viewModel.headline.observe(this, {
            headline.text = it
        })

        viewModel.dice.observe(this, {
            updateDisplay(it)
        })

        val configChange = savedInstanceState?.getBoolean(CONFIG_CHANGE)
            ?: false
        if (configChange.not()) viewModel.rollDice()
    }

    private fun updateDisplay(dice: IntArray) {
        for (i in imageViews.indices) {
            val drawableId = when (dice[i]) {
                1 -> R.drawable.die_1
                2 -> R.drawable.die_2
                3 -> R.drawable.die_3
                4 -> R.drawable.die_4
                5 -> R.drawable.die_5
                6 -> R.drawable.die_6
                else -> R.drawable.die_6
            }
            imageViews[i].setImageResource(drawableId)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.putBoolean(CONFIG_CHANGE, true)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dice, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> shareResult()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareResult(): Boolean {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "I rolled the dice: ${viewModel.headline.value}"
            )
            type = "text/plain"
        }
        startActivity(intent)
        return true
    }

}
