package com.kimjiun.ch18_network

import com.google.gson.annotations.SerializedName

data class UserListModel(
    var page: String,
    @SerializedName("per_page") // 키와 프로퍼티 이름이 다르면
    var perPage: String,
    var total: String,
    @SerializedName("total_pages")
    var totalPages: String,
    var data: List<UserModel>
)