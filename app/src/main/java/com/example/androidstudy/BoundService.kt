package com.example.androidstudy

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*
import kotlin.random.Random

class BoundService : Service() {

    companion object {
        private const val TAG = "BoundService"
    }

    inner class LocalBinder : Binder() {
        fun getService(): BoundService = this@BoundService
    }

    private val binder = LocalBinder()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var randomNumberJob: Job? = null
    private var currentNumber = 0

    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG, "서비스가 바인드됨")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "서비스가 언바인드됨")
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "바인드 서비스 생성됨")
        startRandomNumberGeneration()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "바인드 서비스 소멸됨")
        randomNumberJob?.cancel()
        serviceScope.cancel()
    }

    fun getCurrentNumber(): Int {
        Log.d(TAG, "현재 숫자 요청: $currentNumber")
        return currentNumber
    }

    fun generateNewNumber(): Int {
        val newNumber = Random.nextInt(1, 100)
        currentNumber = newNumber
        Log.d(TAG, "새로운 숫자 생성: $newNumber")
        return newNumber
    }

    fun getServiceStatus(): String {
        return "바인드 서비스 실행 중 - 현재 숫자: $currentNumber"
    }

    private fun startRandomNumberGeneration() {
        randomNumberJob = serviceScope.launch {
            while (isActive) {
                delay(3000) // 3초마다
                currentNumber = Random.nextInt(1, 100)
                Log.d(TAG, "자동 숫자 생성: $currentNumber")
            }
        }
    }
} 