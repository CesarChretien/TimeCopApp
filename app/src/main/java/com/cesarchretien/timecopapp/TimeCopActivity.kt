package com.cesarchretien.timecopapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_time_cop.*
import java.util.concurrent.TimeUnit

class TimeCopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_cop)

        button_start.setOnClickListener {
            val amount = Integer.valueOf(time_minutes_amount.text.toString()).toLong()
            val start = System.currentTimeMillis()
            val end = start + TimeUnit.MINUTES.toMillis(amount)
            refreshTime(start, end)
        }
    }

    private fun refreshTime(current: Long, end: Long) {
        if (current < end) {
            stopwatch_text.postDelayed({
                val currentTime = end - System.currentTimeMillis()
                val currentSecondsRaw = currentTime / 1000
                val currentMinutes = currentSecondsRaw / 60
                val currentSeconds = currentSecondsRaw % 60
                val currentMillis = current % 1000
                stopwatch_text.text = "$currentMinutes : $currentSeconds : $currentMillis"
                refreshTime(System.currentTimeMillis(), end)
            }, 100L)
        }
    }

}
