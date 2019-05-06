package com.shouji2345.net

import android.util.Log
import com.facebook.react.modules.network.ReactCookieJarContainer
import com.google.gson.Gson
import com.shouji2345.http.EncryptionUtils
import com.shouji2345.http.RequestHandler.KEY
import com.shouji2345.userCetner.UserCenterSDKUtils
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


object RetrofitManager {

    private const val MEDIA_TYPE: String = "application/json; charset=utf-8"

    val service: RequestApi by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getRetrofit().create(RequestApi::class.java)
    }

    /**
     * 设置头
     */
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilderBuilder = originalRequest.newBuilder()
            requestBuilderBuilder.header("token", UserCenterSDKUtils.getToken() ?: "")
            requestBuilderBuilder.header("cookie", UserCenterSDKUtils.getUserCookie())
            requestBuilderBuilder.method(originalRequest.method(), originalRequest.body())
            val request = requestBuilderBuilder.build()
            chain.proceed(request)
        }
    }

    /**
     * 重试
     */
    private fun addRetryInterceptor(maxRetry: Int): Interceptor {
        return object : Interceptor {
            var mRetryNum: Int = 0

            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                var response = chain.proceed(request)
                while (!response.isSuccessful && mRetryNum < maxRetry) {
                    mRetryNum++
                    Log.i("Retry", "num:$mRetryNum")
                    response = chain.proceed(request)
                }
                return response
            }
        }
    }

    /**
     * 解密操作
     */
    private fun addEncryptInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            var response = chain.proceed(originalRequest)
            var newResponseString: String? = null
            if (response.code() == 200) {
                val oldResponseBody = response.body()
                try {
                    val oldResponseString = oldResponseBody?.string()
                    newResponseString = EncryptionUtils.strDeCode(oldResponseString, KEY)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    oldResponseBody?.close()
                }
            }
            newResponseString?.run {
                val mediaType = MediaType.parse(MEDIA_TYPE)
                val newResponseBody = ResponseBody.create(mediaType, this)
                response = response.newBuilder().body(newResponseBody).build()
                return@run response
            } ?: response
        }
    }

    private fun getRetrofit(): Retrofit {
        // 获取retrofit的实例
        return Retrofit.Builder()
                .baseUrl(UrlConstant.BASE_URL)  //自己配置
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

    }

    fun getOkHttpClient(beEncrypt: Boolean = true, needHeader: Boolean = true): OkHttpClient {
        //添加一个log拦截器,打印所有的log
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        //可以设置请求过滤的水平,body,basic,headers
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val sslContext = SSLContext.getInstance("TLS")
        val trustManager = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return emptyArray()
            }
        }
        sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
        val sslSocketFactory = sslContext.socketFactory
        val mOkHttpClientBuilder = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(10L, TimeUnit.SECONDS)
                .readTimeout(10L, TimeUnit.SECONDS)
                .writeTimeout(10L, TimeUnit.SECONDS)
                .cookieJar(ReactCookieJarContainer())
                .hostnameVerifier(object : HostnameVerifier {
                    override fun verify(hostname: String?, session: SSLSession?): Boolean {
                        return true
                    }
                }).sslSocketFactory(sslSocketFactory, trustManager)
                .addInterceptor(addRetryInterceptor(2))
                .addInterceptor(httpLoggingInterceptor) //日志,所有的请求响应度看到
        if (beEncrypt) {
            mOkHttpClientBuilder.addInterceptor(addEncryptInterceptor())
        }
        if (needHeader) {
            mOkHttpClientBuilder.addInterceptor(addHeaderInterceptor())
        }
        return mOkHttpClientBuilder.build()
    }
}
