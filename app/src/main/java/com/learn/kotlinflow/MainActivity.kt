package com.learn.kotlinflow

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val example = FlowVsSuspend()
        GlobalScope.launch {
            //suspend
//            val data = example.producerSuspend()
//            data.forEach {
//                Log.d("rkpsx7KotlinFlow","$it")
//            }

            //flow
            val data = example.producerFlow()
            data.collect() {
                Log.d("rkpsx7KotlinFlow", "value -->$it")
            }
        }
    }
}