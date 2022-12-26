package com.learn.kotlinflow

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var tvSecs: TextView
    private lateinit var tvSecs2: TextView

    private lateinit var oldTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvSecs = findViewById(R.id.tvSecs)
        tvSecs2 = findViewById(R.id.tvSecs2)
//        val example = FlowVsSuspend()
//        GlobalScope.launch {
//            //suspend
////            val data = example.producerSuspend()
////            data.forEach {
////                Log.d("rkpsx7KotlinFlow","$it")
////            }
//
//            //flow
//            val data = example.producerFlow()
//            data.collect() {
//                Log.d("rkpsx7KotlinFlow", "value -->$it")
//            }
//        }

        val flowCountDownTimer = FlowCountDownTimer(lifecycleScope)

        CoroutineScope(Dispatchers.IO).launch {
            flowCountDownTimer.remainingSecsFlow.collect {
                runOnUiThread {
                    tvSecs.text = (it/1000).toString()
                }
            }
        }

        oldTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvSecs2.text = (millisUntilFinished / 1000L).toString()
            }

            override fun onFinish() {}

        }

        flowCountDownTimer.getTotalSeconds()

        tvSecs.setOnClickListener {
            flowCountDownTimer.start(60000)
            oldTimer.start()

            tvSecs.setOnClickListener {
                flowCountDownTimer.resume()
            }
        }

    }
}