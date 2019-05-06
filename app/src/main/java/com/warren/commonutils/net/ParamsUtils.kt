package com.shouji2345.net

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.shouji2345.BuildConfig
import com.shouji2345.MainApplication
import com.shouji2345.http.EncryptionUtils
import com.shouji2345.lib.base.utils.DeviceUtil
import com.shouji2345.lib.base.utils.NetStateUtils
import com.shouji2345.utils.AppUtils
import com.shouji2345.utils.SharedPreferencesUtils
import okhttp3.FormBody
import okhttp3.RequestBody

class ParamsUtils {
    companion object {
        private const val KEY = "bbf317368428a9ea838b75a98ace37e4"
        private const val ENCRYPT_VERSION = "1"
        private fun getCommonParamJson(): CommonParamJson {
            val context = MainApplication.applicationContext
            val channel = AppUtils.getChannel(context)
            return CommonParamJson(BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, channel, "android", DeviceUtil.getIMEI(context), NetStateUtils.getLocalMacAddress(), DeviceUtil.getAndroidId(context))
        }

        private fun createParams(postController: String, postMethod: String, scope: String): String {
            val gson = Gson()
            val mCommonParamJson = getCommonParamJson()
            val mExtraParamJson = if (!TextUtils.isEmpty(scope)) ExtraParamJson(scope) else null
            val mPostRequest = PostRequest(postController, postMethod, mCommonParamJson, mExtraParamJson)
            return gson.toJson(mPostRequest)
        }

        fun createRequestBody(postController: String, postMethod: String, scope: String = ""): RequestBody {
            val formBodyBuilder = FormBody.Builder()
            val original = createParams(postController, postMethod, scope)
            Log.i("ParamsUtils", "original:$original postController:$postController postMethod:$postMethod scope:$scope")
            formBodyBuilder.add("data", EncryptionUtils.strCode(original, KEY))
            formBodyBuilder.add("version", ENCRYPT_VERSION)
            return formBodyBuilder.build()
        }
    }
}