package com.kimjiun.ch19_location

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.kimjiun.ch19_location.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    val TAG = "JIUNKIM"
    lateinit var binding: ActivityMainBinding
    var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //locationManagerTest()
        //googleProvider()

        // 지도 부르기
        (supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?)!!.getMapAsync(this)
    }

    // 지도 부르기
    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0

        // 카메라 이동
        val latLng = LatLng(37.566610, 126.978403)
        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()
        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(position))

        // 마커 추가
        val markerOption = MarkerOptions()
        //markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background))
        markerOption.position(latLng)
        markerOption.title("서울시청")
        markerOption.snippet("Tel:01-120")

        googleMap?.addMarker(markerOption)

        // 사용자 이벤트
        googleMap?.setOnMapClickListener { latLng2 -> Log.d(TAG, "click : ${latLng2}") }
        googleMap?.setOnMapLongClickListener { latLng2 -> Log.d(TAG, "long click : ${latLng2}") }
        googleMap?.setOnCameraIdleListener {
            val position = googleMap!!.cameraPosition
            val zoom = position.zoom
            val latitude = position.target.latitude
            val longitude = position.target.longitude
            Log.d(TAG, "change : ${zoom}, ${latitude}, ${longitude}")
        }

        // 마커 클릭시
        googleMap?.setOnMarkerClickListener { marker ->
            Log.d(TAG, "marker : ${marker.title}")
            true
        }

        // 마커 정보 클릭시
        googleMap?.setOnInfoWindowClickListener { marker ->
            Log.d(TAG, "marker : ${marker.snippet}")
        }
    }

    // 구글플레이 라이브러리
    fun googleProvider(){
        // GoogleApiClient 클라이언트 초기화
        val providerClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val connectionCallback= object : GoogleApiClient.ConnectionCallbacks{
            // 위치 제공자를 사용할수 있을때, 위치 획득
            override fun onConnected(p0: Bundle?) {
                if(ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    === PackageManager.PERMISSION_GRANTED){
                    providerClient.lastLocation.addOnSuccessListener (
                        this@MainActivity, object: OnSuccessListener<Location>{
                            override fun onSuccess(location: Location?) {
                                Log.d(TAG, "LOCATION : ${location?.latitude}, ${location?.longitude}, ${location?.accuracy}, ${location?.time}")
                            }

                        }
                    )
                }

            }
            // 위치 제공자를 사용할수 없을때
            override fun onConnectionSuspended(p0: Int) {
                Log.d(TAG, "onConnectionSuspended : $p0")
            }
        }

        // 사용할수 있는 위치 제공자가 없을때
        val onConnectionFailedCallback = object : GoogleApiClient.OnConnectionFailedListener{
            override fun onConnectionFailed(p0: ConnectionResult) {
                Log.d(TAG, "onConnectionFailed : $p0")
            }
        }

        val apiClient: GoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(connectionCallback)
            .addOnConnectionFailedListener(onConnectionFailedCallback)
            .build()
        apiClient.connect()
    }

    // 플랫폼 API 사용
    fun locationManagerTest(){
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager

        // 모든 위치 제공자 보기
        var result = "All Providers : "
        val providers = manager.allProviders

        for(provider in providers){
            result += "$provider, "
        }

        Log.d(TAG, "$result")


        // 사용가능한  위치 제공자 보기
        var result2 = "Enabled Providers : "
        val enableProviders = manager.getProviders(true)

        for(provider in enableProviders){
            result += "$provider, "
        }

        Log.d(TAG, "$result2")

        // 위치 정보 한번만 가져오기
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            === PackageManager.PERMISSION_GRANTED){
            val location: Location? = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            location?.let{
                val latitude = location.latitude
                val longitude = location.longitude
                val accuracy = location.accuracy
                val time = location.time

                Log.d(TAG, "LOCATION : $latitude, $longitude, $accuracy, $time")
            }
        }

        // 계속 위치를 가져오려면 리스너 사용
        val listener: LocationListener = object : LocationListener{
            override fun onLocationChanged(location: Location) {
                Log.d(TAG, "LOCATION : ${location.latitude}, ${location.longitude}, ${location.accuracy}, ${location.time}")
            }

            override fun onProviderDisabled(provider: String) {
                super.onProviderDisabled(provider)
            }

            override fun onProviderEnabled(provider: String) {
                super.onProviderEnabled(provider)
            }
        }

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10_000L, 10f, listener)


        binding.button.setOnClickListener {
            manager.removeUpdates(listener)
        }
    }
}