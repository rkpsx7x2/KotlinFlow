package com.learn.kotlinflow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.util.*

class FlowVsSuspend {

    private val list1 = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    private val list2 = listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)


    /**
     * producer with kotlin flow
     * will keep emitting objects as a stream of data as soon as they get created.
     *
     * IMPORTANT: below you can see "delay()" is used but there is no coroutine launched
     *            because flow manages a coroutine scope by itself.
     */
    fun producerFlow() = flow<Int> {
        for (index in 1..10) {
            delay(1000)
            emit(getObjectAfterOperations(index))
        }
    }

    /**
     * producer with suspend fun
     * will complete the list first and then return all data at once.
     */
    suspend fun producerSuspend(): List<Int> {
        val list = ArrayList<Int>()

        CoroutineScope(Dispatchers.Main).async {
            for (index in 0..9) {
                delay(1000)
                list.add(getObjectAfterOperations(index))
            }
        }.await()

        return list
    }


    private fun getObjectAfterOperations(index: Int): Int {
        return list1[index] + list2[index] + Random().nextInt()
    }
}