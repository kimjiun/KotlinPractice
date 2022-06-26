package com.kimjiun.ch15_service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.kimjiun.ch15_service.databinding.ActivityMainBinding
import com.kimjiun.test_outter.IMyAidlInterface

class MainActivity : AppCompatActivity() {
    lateinit var connection: ServiceConnection
    lateinit var intent2 : Intent
    lateinit var binding: ActivityMainBinding
    lateinit var serviceBinder : MyService.MyBinder
    lateinit var serviceBinder2 : Messenger
    lateinit var aidlService: IMyAidlInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connection = object : ServiceConnection{
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                serviceBinder2 = Messenger(p1)
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                Log.d("JIUNKIM", "onServiceDisconnected")
            }
        }


        intent2 = Intent(this, MyService::class.java)
        bindService(intent2, connection, BIND_AUTO_CREATE)

        binding.startService.setOnClickListener {
            val msg = Message()
            msg.what = 10
            msg.obj = "hello"

            Log.d("JIUNKIM", "P! : ${serviceBinder2.send(msg)}")
        }


        // 외부앱의 서비스 실행
        val intent = Intent("ACTION_AIDL_SERVICE")
        intent.setPackage("com.kimjiun.test_outter")
        bindService(intent, connection2, BIND_AUTO_CREATE)

        binding.stopService.setOnClickListener {
            /*
            val bundle = Bundle()
            bundle.putString("data1", "hello")
            bundle.putInt("data2", 10)

            val msg = Message()
            msg.what=10
            msg.obj=bundle
            //messenger.send(msg)
             */

            Log.d("JIUNKIM", "FUNB : ${aidlService.funB()}")
        }

        // 잡 서비스
        var jobScheduler : JobScheduler? = getSystemService<JobScheduler>()
        JobInfo.Builder(1, ComponentName(this, MyJobService::class.java)).run {
            setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
            jobScheduler?.schedule(build())
        }

        var jobScheduler2: JobScheduler? = getSystemService<JobScheduler>()

        val extras = PersistableBundle()
        extras.putString("extra_data", "hello kkang")

        val builder = JobInfo.Builder(1, ComponentName(this, MyJobService::class.java))
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) // 와이파이 연결이 되있을때만 서비스 실행
        builder.setRequiresCharging(true)
        builder.setExtras(extras)

        val jobInfo = builder.build()

        jobScheduler2!!.schedule(jobInfo)
    }

    val connection2 = object : ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.d("JIUNKIM", "onServiceConnected")
            aidlService = IMyAidlInterface.Stub.asInterface(p1)
            aidlService.funA("hello")
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d("JIUNKIM", "onServiceDisconnected")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        stopService(intent2)
    }
}