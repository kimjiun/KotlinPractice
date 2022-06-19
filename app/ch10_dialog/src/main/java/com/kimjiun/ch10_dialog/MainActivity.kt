package com.kimjiun.ch10_dialog

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import com.kimjiun.ch10_dialog.databinding.CustomDialogBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val TAG:String = "KIMJIUN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permTest()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            toastTest()
        }

        //datePickerTest()
        //timePickerTest()
        //dialogTest()
        //customDialog()
        //soundAndVibTest()
        notiTest()
    }

    fun notiTest(){
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelId = "KIMJIUN"
            val channelName = "JIUN Channel"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

            channel.description = "JIUN Channel DESC" // 채널의 설명
            channel.setShowBadge(true) // 앱 아이콘에 알림 개수가 표시됨
            val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            channel.setSound(uri, audioAttributes) // 알림음 재생
            channel.enableLights(true) // 불빛 표시 여부
            channel.lightColor = Color.RED // 불빛 색상
            //channel.enableVibration(true) // 진동 여부
            //channel.vibrationPattern = longArrayOf(100, 200, 100, 200) // 진동 패턴

            // 채널을 NotifiCationManager에 등록
            manager.createNotificationChannel(channel)

            // 채널을 이용해 빌더 생성
            builder = NotificationCompat.Builder(this, channelId)
        }
        else{
            builder = NotificationCompat.Builder(this)
        }

        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("JIUN TITLE")
        builder.setContentText("JIUN NOTI TEST CONTENT")

        /* 알림 취소 막기
        builder.setAutoCancel(false) // 터치시
        builder.setOngoing(true) // 스와이프시
        */

        // 알림 터치 이벤트
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 10, intent,
            PendingIntent.FLAG_IMMUTABLE)
        //builder.setContentIntent(pendingIntent)

        // 액션 추가
        val actionIntent = Intent(this, MyReceiver::class.java)
        val actionPendingIntent = PendingIntent.getBroadcast(this, 20, actionIntent, PendingIntent.FLAG_IMMUTABLE)
        builder.addAction(
            NotificationCompat.Action.Builder(
                android.R.drawable.stat_notify_more, "ACTION", actionPendingIntent
            ).build()
        )

        // 원격 입력 - 알림에서 사용자 입력을 직접받는 기법
        val KEY_TEXT_REPLY = "key_text_reply"
        var replyLabel: String = "답장"
        var remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(replyLabel)
            build()
        }
        val replyIntent = Intent(this, MyReceiver::class.java)
        val replyPendingIntent = PendingIntent.getBroadcast(this, 30, replyIntent, PendingIntent.FLAG_MUTABLE)
        builder.addAction(
            NotificationCompat.Action.Builder(
                android.R.drawable.ic_menu_send, "답장", replyPendingIntent
            ).addRemoteInput(remoteInput).build()
        )

        // 프로그레스 바
        //builder.setProgress(100, 0, false)

        // 큰 이미지
        val bigPicture = BitmapFactory.decodeResource(resources, R.drawable.pep)
        val bigStyle = NotificationCompat.BigPictureStyle()
        bigStyle.bigPicture(bigPicture)
        //builder.setStyle(bigStyle)

        // 긴 텍스트 - 이메일 내용을 보여줄때
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.bigText("dkfldalfnafnlandlnskaldnalskdnlkasdnlkasndlkaskndl")
        //builder.setStyle(bigTextStyle)

        // 상자 스타일 - 문자열을 목록으로 출력
        val style = NotificationCompat.InboxStyle()
        style.addLine("1 Course")
        style.addLine("2 Course")
        style.addLine("3 Course")
        style.addLine("4 Course")
        //builder.setStyle(style)

        // 메세지 스타일 - 여러사람이 주고받은 메시지를 구분해서 출력할 때
        val sender1: Person = Person.Builder()
            .setName("kim")
            .setIcon(IconCompat.createWithResource(this, R.drawable.person1))
            .build()

        val sender2: Person = Person.Builder()
            .setName("lee")
            .setIcon(IconCompat.createWithResource(this, R.drawable.person2))
            .build()

        val message1 = NotificationCompat.MessagingStyle.Message(
            "hello",
            System.currentTimeMillis(),
            sender1
        )

        val message2 = NotificationCompat.MessagingStyle.Message(
            "world",
            System.currentTimeMillis(),
            sender2
        )

        val messageStyle = NotificationCompat.MessagingStyle(sender1)
            .addMessage(message1)
            .addMessage(message2)
        builder.setStyle(messageStyle)


        manager.notify(9, builder.build())

        /* 프로그레스바
        thread {
            for(i in 1..100){
                builder.setProgress(100, i, false)
                manager.notify(9, builder.build())
                SystemClock.sleep(100)
            }
        }
        */

        //manager.cancel(9)
    }

    fun soundAndVibTest(){
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(applicationContext, notification)
        //ringtone.play()

        val mp3Arr = arrayOf(R.raw.sample1, R.raw.sample2, R.raw.sample3)
        val random = Random
        val randNum = random.nextInt(mp3Arr.size)
        val player: MediaPlayer = MediaPlayer.create(this, mp3Arr[randNum])
        //player.start()

        val vibrator = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val vibratorManager = this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        }
        else{
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        /*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            vibrator.vibrate(
                VibrationEffect.createOneShot(500,
                VibrationEffect.DEFAULT_AMPLITUDE)
            )
        }
        else{
            vibrator.vibrate(500)
        }

         */


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            vibrator.vibrate(
                VibrationEffect.createWaveform(longArrayOf(500, 1000, 500, 2000), // 0.5초 쉬고 1초 울리고 0.5초 쉬고 2초 울림
                intArrayOf(0, 50, 0, 200), -1) // 50의 세기, 200의 세기 / 반복여부
            )
        }
        else{
            vibrator.vibrate(longArrayOf(500, 1000, 500, 2000), -1)
        }
    }

    fun customDialog(){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = inflater.inflate(R.layout.custom_dialog, null)

        val binding2 = CustomDialogBinding.inflate(layoutInflater)
        val rootView2 = binding2.root

        val dialogBinding = CustomDialogBinding.inflate(layoutInflater)
        AlertDialog.Builder(this).run {
            setTitle("INPUT")
            setView(dialogBinding.root)
            setPositiveButton("X", null)
            show()
        }
    }

    fun dialogTest(){
        val eventHandler = object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                if(p1 == DialogInterface.BUTTON_POSITIVE){
                    Log.d(TAG,"BUTTON_POSITIVE")
                }
                else if(p1 == DialogInterface.BUTTON_NEGATIVE){
                    Log.d(TAG,"BUTTON_NEGATIVE")
                }
            }
        }

        val items = arrayOf<String>("apple", "banana", "orange", "melon")

        AlertDialog.Builder(this).run {
            setTitle("hello")
            setIcon(R.drawable.ic_launcher_background)
            setPositiveButton("OK" ,eventHandler)
            setNegativeButton("CANCEL" ,eventHandler)
            /*
            // 아이템 선택후 닫힘
            setItems(items, object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    Log.d(TAG,"selected : ${items[p1]}")
                }
            })
            // 체크박스
            setMultiChoiceItems(items, booleanArrayOf(true, false, true, false),
                object : DialogInterface.OnMultiChoiceClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int, p2: Boolean) {
                    Log.d(TAG,"${items[p1]}이 ${if(p2) "선택되었습니다." else "선택해제되었습니다"}")
                }

            })
            */
            // 라디오버튼
            setSingleChoiceItems(items, 1, object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    Log.d(TAG,"selected : ${items[p1]}")
                }
            })
            setCancelable(false) // 뒤로가기 버튼
            show()
        }.setCanceledOnTouchOutside(false) // 바깥 눌렀을떄 닫기
    }

    fun timePickerTest(){
        TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener{
            override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                Log.d(TAG,"hour : $p1, minute : $p2")
            }
        },15, 0, true).show()
    }

    fun datePickerTest(){
        DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                Log.d(TAG,"year : $p1, month : ${p2 + 1}, dayOfMonth : $p3")
            }
        }, 2022, 6, 7).show()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun toastTest(){
        val toast = Toast.makeText(this, "HELLo", Toast.LENGTH_SHORT)

        toast.addCallback(
            object : Toast.Callback(){
                override fun onToastHidden() {
                    super.onToastHidden()
                    Log.d(TAG,"onToastHidden")
                }

                override fun onToastShown() {
                    super.onToastShown()
                    Log.d(TAG,"onToastShown")
                }
            }
        )

        toast.show();
    }

    fun permTest(){
        // 퍼미션 확인
        val status = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION")

        // 퍼미션 요청
        val reqestPerm = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){isGranted ->
            if(isGranted){
                Log.d("jiunkim", "callback, granted")
            }
            else{
                Log.d("jiunkim", "callback, denied")
            }
        }

        reqestPerm.launch("android.permission.ACCESS_FINE_LOCATION")
    }
}
