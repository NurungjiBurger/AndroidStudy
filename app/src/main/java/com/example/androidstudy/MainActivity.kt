package com.example.androidstudy

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "Lifecycle"
    }

    var boundService: BoundService? = null
        private set
    var isServiceBound = false
        private set

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "바인드 서비스에 연결됨")
            val binder = service as BoundService.LocalBinder
            boundService = binder.getService()
            isServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "바인드 서비스 연결 해제됨")
            isServiceBound = false
            boundService = null
        }
    }

    fun bindToService() {
        val intent = Intent(this, BoundService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun unbindFromService() {
        if (isServiceBound) {
            unbindService(serviceConnection)
            isServiceBound = false
            boundService = null
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(TAG, "알림 권한이 허용되었습니다.")
        } else {
            Log.d(TAG, "알림 권한이 거부되었습니다.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: 새로운 액티비티 생성")

        askNotificationPermission()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ServiceTestScreen()
                }
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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

        unbindFromService()
    }
}

@Composable
fun ServiceTestScreen() {
    var activityCounter by rememberSaveable { mutableStateOf(0) }
    var serviceNumber by rememberSaveable { mutableStateOf(0) }
    var serviceStatus by rememberSaveable { mutableStateOf("서비스 연결 안됨") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            activityCounter++
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Service Test",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "Activity Counter: $activityCounter",
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        
        Text(
            text = "Service Number: $serviceNumber",
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        
        Text(
            text = serviceStatus,
            fontSize = 14.sp
        )
        
        Spacer(modifier = Modifier.height(40.dp))

        Text("포그라운드 서비스", fontWeight = FontWeight.Bold)
        Row {
            Button(onClick = {
                Intent(context, RunningService::class.java).also {
                    context.startService(it)
                }
            }) {
                Text("포그라운드 시작")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {
                Intent(context, RunningService::class.java).also {
                    context.stopService(it)
                }
            }) {
                Text("포그라운드 중지")
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))

        Text("바인드 서비스", fontWeight = FontWeight.Bold)
        Row {
            Button(onClick = {
                (context as MainActivity).bindToService()
            }) {
                Text("바인드 연결")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {
                (context as MainActivity).unbindFromService()
                serviceStatus = "서비스 연결 해제됨"
            }) {
                Text("바인드 해제")
            }
        }
        
        Spacer(modifier = Modifier.height(10.dp))
        
        Row {
            Button(onClick = {
                val mainActivity = context as MainActivity
                if (mainActivity.isServiceBound && mainActivity.boundService != null) {
                    serviceNumber = mainActivity.boundService!!.getCurrentNumber()
                    serviceStatus = mainActivity.boundService!!.getServiceStatus()
                }
            }) {
                Text("숫자 가져오기")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {
                val mainActivity = context as MainActivity
                if (mainActivity.isServiceBound && mainActivity.boundService != null) {
                    serviceNumber = mainActivity.boundService!!.generateNewNumber()
                    serviceStatus = mainActivity.boundService!!.getServiceStatus()
                }
            }) {
                Text("새 숫자 생성")
            }
        }
    }
}