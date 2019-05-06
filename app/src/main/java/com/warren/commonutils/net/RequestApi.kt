package com.shouji2345.net

import com.shouji2345.http.bean.CommonResponse
import com.shouji2345.http.bean.PersonalInfo
import com.shouji2345.http.bean.TabList
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface RequestApi {

    @POST(".")
    fun requestTabList(@Body body: RequestBody): Observable<CommonResponse<TabList>>

    @POST(".")
    fun requestPersonal(@Body body: RequestBody): Observable<CommonResponse<PersonalInfo>>
}