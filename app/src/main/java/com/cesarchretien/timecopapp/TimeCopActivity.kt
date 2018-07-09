package com.cesarchretien.timecopapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_time_cop.*
import java.util.concurrent.TimeUnit
import android.media.MediaPlayer
import android.graphics.Color
import android.support.annotation.RawRes
import android.util.Log
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean


class TimeCopActivity : AppCompatActivity() {

    var isStopped = false
    val hasWarned = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_cop)

        button_start.setOnClickListener {
            val amount = Integer.valueOf(time_minutes_amount.text.toString()).toLong()
            val warning = TimeUnit.MINUTES.toMillis(Integer.valueOf(time_minutes_amount_warning.text.toString()).toLong())
            val start = System.currentTimeMillis()
            val end = start + TimeUnit.MINUTES.toMillis(amount)
            val thread = Thread(Runnable { refreshTime(start, end, warning) })
            thread.start()

            button_reset.setOnClickListener {
                isStopped = true
                hasWarned.set(false)
                stopwatch_text.setTextColor(Color.WHITE)
            }
        }
    }

    private fun refreshTime(current: Long, end: Long, warning: Long) {
        if (current < end) {
            stopwatch_text.postDelayed({
                if (!isStopped) {
                    setRemainingTime(end, current, warning)
                    refreshTime(System.currentTimeMillis(), end, warning)
                } else {
                    isStopped = false
                    val amountR = Integer.valueOf(time_minutes_amount.text.toString()).toLong()
                    val startR = System.currentTimeMillis()
                    val endR = startR + TimeUnit.MINUTES.toMillis(amountR)
                    setRemainingTime(endR, startR, warning)
                }
            }, 50L)
        } else {
            stopwatch_text.postDelayed({
                setRemainingTime(end, end, warning)
                stopwatch_text.setTextColor(Color.WHITE)
                play(R.raw.smb_stage_clear)
            }, 50L)
        }
    }

    private fun setRemainingTime(end: Long, current: Long, warning: Long) {
        val remaining = end - current

        if (remaining < warning && !hasWarned.getAndSet(true)) {
            stopwatch_text.setTextColor(Color.RED)
            play(R.raw.smb_warning)
        }

        val remainingSecondsRaw = remaining / 1000
        val remainingMinutes = String.format("%02d", remainingSecondsRaw / 60)
        val remainingSeconds = String.format("%02d", remainingSecondsRaw % 60)
        val remainingMillis = String.format("%03d", remaining % 1000)
        stopwatch_text.text = "$remainingMinutes : $remainingSeconds : $remainingMillis"
    }

    private fun play(@RawRes musicRaw: Int) {
        val afd = resources.openRawResourceFd(musicRaw)
        val fileDescriptor = afd.fileDescriptor
        val player = MediaPlayer()
        try {
            player.setDataSource(fileDescriptor, afd.startOffset, afd.length)
            player.isLooping = false
            player.prepare()
            player.start()
            player.setOnCompletionListener {
                it.release()
            }
        } catch (ex: IOException) {
            Log.e("Oops", ex.localizedMessage)
        }
    }

}
