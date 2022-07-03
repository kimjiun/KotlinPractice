package com.kimjiun.ch18_network

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.PhoneStateListener
import android.telephony.ServiceState
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.kimjiun.ch18_network.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var manager: TelephonyManager
    val TAG = "JIUNKIM"

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        // telephonyTest()
        // checkConnectivity()
        // volleyTest()
        // retrofitTest()

        glideTest()
    }

    fun glideTest(){
        // 리소스 가져오기
        Glide.with(this)
            .load(R.drawable.ic_launcher_background)
            .into(binding.image)

        // 파일 가져오기
        val requsetLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            Glide.with(this)
                .load(it.data?.data)
                .into(binding.image3)
        }

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        requsetLauncher.launch(intent)

        // 서버 이미지 가져오기
        val imageUrl = "https://t1.daumcdn.net/daumtop_chanel/op/20200723055344399.png"
        Glide.with(this)
            .load(imageUrl)
            .override(200, 200)  // 크기 조절
            .placeholder(R.drawable.ic_launcher_background) // 지정한 이미지를 먼저 출력
            .error(R.drawable.ic_launcher_foreground) // 에러 발생시 이미지 출력
            .into(binding.image2)

        Glide.with(this)
            .load(imageUrl)
            .into(object : CustomTarget<Drawable>(){
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    TODO("Not yet implemented")
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("Not yet implemented")
                }

            })

        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    TODO("Not yet implemented")
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun retrofitTest(){
        val networkService = MyRetrofit().networkService

        val userListCall = networkService.doGetUserList("1")

        userListCall.enqueue(object: Callback<UserListModel> {
            override fun onResponse(call: Call<UserListModel>, response: retrofit2.Response<UserListModel>){
                Log.d(TAG, "RESP : $response")
            }

            override fun onFailure(call: Call<UserListModel>, t: Throwable) {
                Log.d(TAG, "ERROR : $t")
            }
        })

        val test3Call = networkService.test3(mapOf<String, String>("one" to "hello", "two" to "hello"), "jiun")
        val test4Call = networkService.test4(UserModel("1","1","1","1",
            getDrawable(R.drawable.ic_launcher_background)?.toBitmap()!!
        ), "jiun")
    }

    fun volleyTest(){
        val queue = Volley.newRequestQueue(this)
        val url = "https://www.daum.net"

        // String 데이터 요청
        val stringReqGet = StringRequest(Request.Method.GET, url,
            Response.Listener<String>{
                Log.d(TAG, "data : $it")
            }, Response.ErrorListener{
                Log.d(TAG, "error : $it")
            }
        )

        queue.add(stringReqGet)

        val stringReqPost = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String>{
                Log.d(TAG, "data : $it")
            }, Response.ErrorListener{
                Log.d(TAG, "error : $it")
            }
        ){
            override fun getParams(): MutableMap<String, String>? {
                return mutableMapOf<String, String>("one" to "hello", "two" to "world")
            }
        }
        queue.add(stringReqPost)

        // 이미지 데이터 요청
        val imageUrl = "https://t1.daumcdn.net/daumtop_chanel/op/20200723055344399.png"
        val imageRequest = ImageRequest(imageUrl, Response.Listener { response -> binding.image.setImageBitmap(response)},
            0, 0, ImageView.ScaleType.CENTER_CROP, null, Response.ErrorListener { Log.d(TAG, "error : $it") })

        queue.add(imageRequest)

        val imgMap = HashMap<String, Bitmap>()
        val imageLoader = ImageLoader(queue, object : ImageLoader.ImageCache{
            override fun getBitmap(url: String?): Bitmap? {
                return imgMap[url]
            }

            override fun putBitmap(url: String?, bitmap: Bitmap?) {
                if (bitmap != null) {
                    if (url != null) {
                        imgMap.put(url, bitmap)
                    }
                }
            }
        })

        binding.networkImage.setImageUrl(imageUrl, imageLoader)

        // Json 데이터 요청
        val jsonReq = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener<JSONObject>{response ->
                val title = response.getString("title")
                val date = response.getString("date")
            }, Response.ErrorListener{
                Log.d(TAG, "error : $it")
            })

        // Json Array 데이터 요청
        val jsonArrayReq = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener<JSONArray>{response ->
                for(i in 0..response.length()){
                    val jsonObject = response[i] as JSONObject
                    val title = jsonObject.getString("title")
                    val date = jsonObject.getString("date")
                }
            }, Response.ErrorListener{
                Log.d(TAG, "error : $it")
            })
    }

    fun checkConnectivity(){
        val country = manager.networkCountryIso
        val operatorName = manager.networkOperatorName
        //val phoneNum = manager.line1Number

        //Log.d(TAG, "$country / $operatorName / $phoneNum")

        Log.d(TAG, "NETWORK : ${isNetworkAvailable()}")

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkReq: NetworkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        connectivityManager.requestNetwork(networkReq, object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d(TAG, "NETWORK onAvailable")
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Log.d(TAG, "NETWORK onUnavailable")
            }
        })
    }

    fun isNetworkAvailable(): Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when{
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
        else{
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

    fun telephonyTest(){
        // 안드로이드 11 까지만
        val phoneStateListener = object : PhoneStateListener(){
            // 통화 관련 상태
            override fun onServiceStateChanged(serviceState: ServiceState?) {
                var str = "str"

                when(serviceState?.state){
                    ServiceState.STATE_EMERGENCY_ONLY -> str = "EMERGENCY" // 긴급전화만
                    ServiceState.STATE_OUT_OF_SERVICE -> str = "OUT_OF_SERVICE" // 통화 불가능
                    ServiceState.STATE_POWER_OFF -> str = "POWER_OFF" // 비행모드 등 전화를 끔
                    ServiceState.STATE_IN_SERVICE -> str = "IN_SERVICE" // 통화 가능
                }
                Log.d(TAG, "onServiceStateChanged : ${str}")
            }

            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                var str = "str"

                when(state){
                    TelephonyManager.CALL_STATE_IDLE -> str = "IDLE"
                    TelephonyManager.CALL_STATE_OFFHOOK -> str = "OFFHOOK"
                    TelephonyManager.CALL_STATE_RINGING -> str = "RINGING"
                }
                Log.d(TAG, "onCallStateChanged : ${str}")
            }
        }


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            manager.registerTelephonyCallback(mainExecutor, object : TelephonyCallback(), TelephonyCallback.CallStateListener{
                override fun onCallStateChanged(state: Int) {
                    var str = "str"
                    when(state){
                        TelephonyManager.CALL_STATE_IDLE -> str = "IDLE"
                        TelephonyManager.CALL_STATE_OFFHOOK -> str = "OFFHOOK"
                        TelephonyManager.CALL_STATE_RINGING -> str = "RINGING"
                    }
                    Log.d(TAG, "onCallStateChanged : ${str}")
                }

            })
        }
        else{
            // 안드로이드 11까지만
            manager.listen(phoneStateListener,PhoneStateListener.LISTEN_SERVICE_STATE or PhoneStateListener.LISTEN_CALL_STATE)
            manager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        }
    }
}