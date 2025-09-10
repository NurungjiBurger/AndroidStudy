package com.example.androidstudy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "MyBroadcastReceiver"
        const val CUSTOM_ACTION = "com.example.androidstudy.CUSTOM_ACTION"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                val isAirplaneModeOn = intent.getBooleanExtra("state", false)
                Log.d(TAG, "비행기 모드 변경: $isAirplaneModeOn")
                Toast.makeText(context, "비행기 모드: $isAirplaneModeOn", Toast.LENGTH_SHORT).show()
            }
            
            Intent.ACTION_BATTERY_LOW -> {
                Log.d(TAG, "배터리 부족")
                Toast.makeText(context, "배터리가 부족합니다!", Toast.LENGTH_LONG).show()
            }
            
            Intent.ACTION_POWER_CONNECTED -> {
                Log.d(TAG, "전원 연결됨")
                Toast.makeText(context, "충전기가 연결되었습니다", Toast.LENGTH_SHORT).show()
            }
            
            Intent.ACTION_POWER_DISCONNECTED -> {
                Log.d(TAG, "전원 연결 해제됨")
                Toast.makeText(context, "충전기가 연결 해제되었습니다", Toast.LENGTH_SHORT).show()
            }
            
            CUSTOM_ACTION -> {
                val message = intent.getStringExtra("message") ?: "기본 메시지"
                Log.d(TAG, "커스텀 브로드캐스트 수신: $message")
                Toast.makeText(context, "커스텀 메시지: $message", Toast.LENGTH_SHORT).show()
            }
            
            else -> {
                Log.d(TAG, "알 수 없는 액션: ${intent.action}")
            }
        }
    }
} 