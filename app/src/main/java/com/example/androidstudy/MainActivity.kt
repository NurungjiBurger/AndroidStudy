package com.example.androidstudy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "Lifecycle"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: 새로운 액티비티 생성")

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LifecycleLoggerScreen()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: 액티비티가 사용자에게 보이기 직전")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: 사용자와 상호작용 시작")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: 다른 액티비티에 가려짐, 포커스 잃음")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: 액티비티가 완전히 가려짐")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: 중지 상태에서 다시 시작 준비")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: 액티비티 소멸")
    }
}

@Composable
fun LifecycleLoggerScreen() {
    var counter by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            counter++
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Activity Lifecycle Test",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Counter: $counter",
            fontSize = 20.sp
        )
    }
}
