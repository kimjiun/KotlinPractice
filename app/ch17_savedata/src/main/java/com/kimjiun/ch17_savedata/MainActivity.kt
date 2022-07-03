package com.kimjiun.ch17_savedata

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.kimjiun.ch17_savedata.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.File
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    lateinit var binding: ActivityMainBinding
    val TAG = "JIUNKIM"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //inAndExternalDir()
        // cameraProvide()

        // 공유된 프리퍼런스
        val sharedPref = getPreferences(MODE_PRIVATE) // 액티비티 단위로 데이터 저장
        val sharedPref2 = getSharedPreferences("my_prefs", MODE_PRIVATE) // my_prefs 파일에 데이터 저장

        sharedPref2.edit().run {
            putString("data1", "hello")
            putInt("data2", 10)
            commit()
        }

        val data1 = sharedPref2.getString("data1", "world")
        val data2 = sharedPref2.getInt("data2", 15)
        Log.d(TAG, "$data1   $data2")

        val fragmentManager = getSupportFragmentManager();
        val frag = MainFragment()

        val transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, frag).commitAllowingStateLoss();
    }

    // 공용저장소에 저장된 이미지 파일 가져오기
    fun getPublicImage(){
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME)
        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null)

        binding.button.setOnClickListener {
            cursor?.let{
                while(cursor.moveToNext()){
                    //Log.d(TAG, "_id : ${cursor.getString(0)}, name : ${cursor.getString(1)}")

                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(0)
                    )

                    val resolver = applicationContext.contentResolver
                    resolver.openInputStream(contentUri).use{ stream ->
                        val option = BitmapFactory.Options()
                        option.inSampleSize = 10
                        val bitmap = BitmapFactory.decodeStream(stream, null, option)
                        binding.galleryResult.setImageBitmap(bitmap)
                    }

                    Log.d(TAG, "contentUri : ${contentUri}")
                }
            }
        }
    }

    // 파일 프로바이더로 외부공유 - 파일 생성하고 카메라앱으로 찍은 사진을 해당 파일에 저장
    fun cameraProvide(){
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
        val filePath = file.absolutePath

        val photoURI: Uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)

        val requestCameraFileLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            val option = BitmapFactory.Options()
            option.inSampleSize = 10
            val bitmap = BitmapFactory.decodeFile(filePath, option)
            bitmap?.let {
                binding.galleryResult.setImageBitmap(bitmap)
            }
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        requestCameraFileLauncher.launch(intent)
    }

    // 파일저장
    fun inAndExternalDir(){
        val file = File(filesDir, "test.txt")
        val writeStream: OutputStreamWriter = file.writer()
        writeStream.write("hello world")
        writeStream.close()

        val readStream: BufferedReader = file.reader().buffered()
        readStream.forEachLine {
            Log.d(TAG, "$it")
        }

        openFileOutput("test.txt", Context.MODE_PRIVATE).use{
            it.write("hello world2".toByteArray())
        }

        openFileInput("test.txt").bufferedReader().forEachLine{
            Log.d(TAG, "$it")
        }

        val file2: File = File(getExternalFilesDir(null), "test3.txt")
        Log.d(TAG, "${file2?.absolutePath}")
        val writeStream2: OutputStreamWriter = file2.writer()
        writeStream2.write("hello world@!@!")
        writeStream2.flush()

        val readStream2: BufferedReader = file2.reader().buffered()
        readStream2.forEachLine { Log.d(TAG, "$it") }

    }

    // 내부 DB 사용
    fun useDB(){
        val db = openOrCreateDatabase("testdb", Context.MODE_PRIVATE, null)

        /*
        db.execSQL("create table USER_TB(id integer primary key autoincrement, name not null, phone)")
        db.execSQL("insert into USER_TB(name, phone) values (?, ?)", arrayOf("JIUNKIM", "01023822222"))
         */

        val values = ContentValues()
        values.put("name", "LEECHUNBAE")
        values.put("phone", "01099290000")
        //db.insert("USER_TB", null, values)

        val cursor = db.rawQuery("select * from USER_TB", null)

        val cursor2 = db.query("USER_TB", arrayOf("name", "phone"), "phone=?", arrayOf("01099290000"), null, null, null)

        while(cursor.moveToNext()){
            val name = cursor.getString(1)
            val phone = cursor.getString(2)

            Log.d("JIUNKIM", "NAME : $name / PHONE : $phone")
        }

        while(cursor2.moveToNext()){
            val name = cursor2.getString(0)
            val phone = cursor2.getString(1)

            Log.d("JIUNKIM", "NAME2 : $name / PHONE2 : $phone")
        }

        val dbHelper = DBHelper(this).writableDatabase
    }

    class DBHelper(context: Context) : SQLiteOpenHelper(context, "testdb", null, 1){
        override fun onCreate(p0: SQLiteDatabase?) {
            Log.d("JIUNKIM", "DBHelper onCreate")
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            Log.d("JIUNKIM", "DBHelper SQLiteOpenHelper")
        }

    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        val args = pref.extras
        val fragment =
            pref.fragment?.let {
                supportFragmentManager.fragmentFactory.instantiate(classLoader,
                    it
                )
            }

        fragment?.arguments = args

        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit()
        }

        return true
    }
}