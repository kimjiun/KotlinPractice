package com.kimjiun.ch10_dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

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
        dialogTest()
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