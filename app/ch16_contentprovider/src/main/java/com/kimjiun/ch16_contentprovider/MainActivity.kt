package com.kimjiun.ch16_contentprovider

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.kimjiun.ch16_contentprovider.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var requestContactsLauncher: ActivityResultLauncher<Intent>
    lateinit var requestGalleryLauncher: ActivityResultLauncher<Intent>
    lateinit var requestCameraLauncher: ActivityResultLauncher<Intent>
    lateinit var requestCameraFileLauncher: ActivityResultLauncher<Intent>
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contentResolver.query(Uri.parse("content://com.kimjiun.test_outter"),
            null, null, null, null)

        contactTest()

        galleryTest()

        cameraTest()

        cameraTest2()

        binding.geoBtn.setOnClickListener {
            val geoIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.5662852,126.97749451"))
            startActivity(geoIntent)
        }

        binding.callBtn.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:114"))
            startActivity(callIntent)
        }
    }

    fun cameraTest2(){
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
        val filePath: String = file.absolutePath

        requestCameraFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val option = BitmapFactory.Options()
            option.inSampleSize = 10
            val bitmap = BitmapFactory.decodeFile(filePath, option)
            bitmap?.let {
                binding.galleryResult.setImageBitmap(bitmap)
            }
        }

        binding.cameraBtn2.setOnClickListener {
            val photoURI: Uri = FileProvider.getUriForFile(this, "com.kimjiun.ch16_contentprovider.fileprovider", file)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            requestCameraFileLauncher.launch(intent)
        }
    }

    fun cameraTest(){
        requestCameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            try {
                val bitmap = it?.data?.extras?.get("data") as Bitmap
                binding.galleryResult.setImageBitmap(bitmap)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.cameraBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            requestCameraLauncher.launch(intent)
        }
    }

    fun galleryTest(){
        requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            try{
                val calRatio = calculateInSampleSize(it!!.data!!.data!!,
                    binding.galleryResult.width,
                    binding.galleryResult.height)
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio

                var inputStream = contentResolver.openInputStream(it!!.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()

                bitmap?. let {
                    binding.galleryResult.setImageBitmap(bitmap)
                } ?: let{
                    Log.d("JIUNKIM", "bitmap null")
                }
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.galleryBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }
    }

    fun contactTest(){
        requestContactsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK){
                val cursor = contentResolver.query(
                    it!!.data!!.data!!,
                    arrayOf<String>(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER),
                    null, null,null)

                Log.d("JIUNKIM", "${cursor?.count}")
                if(cursor!!.moveToFirst()){
                    val name = cursor?.getString(0)
                    val num = cursor?.getString(1)
                    Log.d("JIUNKIM", "${name} / $num")
                }
            }
        }

        binding.contactBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            requestContactsLauncher.launch(intent)
        }
    }

    // 이미지의 축소비율을 계산
    fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight:Int): Int{
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true // Bitmap 객체가 만들어지지 않음

        try{
            var inputStream = contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        }
        catch (e: Exception){
            e.printStackTrace()
        }

        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if(height > reqHeight || width > reqWidth){
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while(halfHeight / inSampleSize >= reqHeight &&
                halfWidth / inSampleSize >= reqWidth){
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}