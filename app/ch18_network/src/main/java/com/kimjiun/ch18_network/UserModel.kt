package com.kimjiun.ch18_network

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class UserModel(
    var id: String,
    @SerializedName("first_name") // 키와 프로퍼티 이름이 다르면
    var firstName: String,
    var lastName: String,
    var avatar: String,
    var avatarBitmap: Bitmap
)