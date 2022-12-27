package com.learn.kotlinflow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FlowCountDownTimer(private val coroutineScope: CoroutineScope) {

    private var _timerStateFlow = MutableStateFlow(0L)
    val remainingSecsFlow: StateFlow<Long> = _timerStateFlow

    private var mTotalSecs = 0L
    private var mRemainingSecs = 0L
    private var mInterval = 1000L

    private var job: Job? = null

    /**
     * Starts the countdown timer.
     * @see <Caution> Calling this fn on a paused timer will restart the timer from beginning. Use resume() instead.
     * @param totalSeconds :(In MilliSeconds) Timer starts from this value and run till 0.
     * @param interval : (In MilliSeconds) this value defines the delay between every time tick callback. Default Value: 1000L
     */
    fun start(
        totalSeconds: Long,
        interval: Long = 1000L
    ) {
        if (job == null) {
            this.mTotalSecs = totalSeconds
            this.mRemainingSecs = totalSeconds
            this.mInterval = interval

            initialiseTimer(mTotalSecs)
        }
    }

    fun pause() {
        job?.cancel()
    }

    /**
     * Resumes the timer.
     * @param newInterval (Long) Changes the interval of time tick callback resuming from this point of time.
     * @see <Caution> "newInterval" filed is for specific cases where you need to change the interval of time tick callback
     * at some point of time, else don't pass any value.
     */
    fun resume(newInterval: Long?=null) {
        newInterval?.let { this.mInterval = it }
        initialiseTimer(mRemainingSecs)
    }

    private fun initialiseTimer(remainingSecsInMillisToInitialiseWith: Long) {
        job = coroutineScope.launch {
            prepareTimer(remainingSecsInMillisToInitialiseWith).collect { remainingSecs ->
                mRemainingSecs = remainingSecs
                _timerStateFlow.emit(remainingSecs)
            }
        }
    }

    private suspend fun prepareTimer(totalSecs: Long): Flow<Long> =
        runTimer(totalSecs)
            .conflate()
            .transform {
                emit(it)
            }

    private suspend fun runTimer(totalSecs: Long) = flow {
        var remSeconds = totalSecs
        while (remSeconds >= 0L) {
            emit(remSeconds)
            delay(mInterval)
            remSeconds -= mInterval
        }
    }

    fun reset() {
        job?.let {
            it.cancel()
            job = null
            mTotalSecs = 0
            mRemainingSecs = 0
            mInterval = 1000L
        }
    }

    fun getTotalSeconds() = this.mTotalSecs

}