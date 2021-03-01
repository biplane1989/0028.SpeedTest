package com.tapi.vpncore.api

import com.tapi.vpncore.api.server.NetworkInfo
import com.tapi.vpncore.listserver.Settings
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import java.lang.reflect.ParameterizedType
import java.io.IOException
import java.lang.reflect.Type

const val BASE_URL_SERVER = "http://ip-api.com"
const val BASE_URL__LIST_SERVER = "https://c.speedtest.net"
const val GET_MY_NETWORK_INFO_SERVER = "json"
const val GET_MY_NETWORK_INFO_LIST_SERVER = "speedtest-servers-static.php"

sealed class Result<out T> {
    data class Success<T>(val data: T?) : Result<T>()
    data class Failure(val statusCode: Int?) : Result<Nothing>()
    object NetworkError : Result<Nothing>()
}

abstract class CallDelegate<TIn, TOut>(protected val proxy: Call<TIn>) : Call<TOut> {
    override fun execute(): Response<TOut> = throw NotImplementedError()
    final override fun enqueue(callback: Callback<TOut>) = enqueueImpl(callback)
    final override fun clone(): Call<TOut> = cloneImpl()

    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun isExecuted() = proxy.isExecuted
    override fun isCanceled() = proxy.isCanceled
    override fun timeout(): Timeout = proxy.timeout()

    abstract fun enqueueImpl(callback: Callback<TOut>)
    abstract fun cloneImpl(): Call<TOut>

}

class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, Result<T>>(proxy) {
    override fun enqueueImpl(callback: Callback<Result<T>>) = proxy.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val code = response.code()
            val result = if (code in 200 until 300) {
                val body = response.body()
                val successResult: Result<T> = Result.Success(body)
                successResult
            } else {
                Result.Failure(code)
            }

            callback.onResponse(this@ResultCall, Response.success(result))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val result = if (t is IOException) {
                Result.NetworkError
            } else {
                Result.Failure(null)
            }

            callback.onResponse(this@ResultCall, Response.success(result))
        }
    })

    override fun cloneImpl() = ResultCall(proxy.clone())

}

class ResultAdapter(private val type: Type) : CallAdapter<Type, Call<Result<Type>>> {
    override fun responseType() = type
    override fun adapt(call: Call<Type>): Call<Result<Type>> = ResultCall(call)
}


class MyCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit) = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType = getParameterUpperBound(0, returnType as ParameterizedType)
            when (getRawType(callType)) {
                Result::class.java -> {
                    val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                    ResultAdapter(resultType)
                }
                else -> null
            }
        }
        else -> null
    }
}

interface NetworkServer {

    @GET(GET_MY_NETWORK_INFO_SERVER)
    suspend fun getMyNetworkInfo(): Result<NetworkInfo>


    companion object {
        private val client: OkHttpClient = OkHttpClient.Builder().build()
        fun create(): NetworkServer {
            val retrofit = Retrofit.Builder().addCallAdapterFactory(MyCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL_SERVER)
                .client(client).build()

            return retrofit.create(NetworkServer::class.java)
        }
    }
}

interface NetworkListServer {

    @GET(GET_MY_NETWORK_INFO_LIST_SERVER)
    suspend fun getListServer(): Result<Settings>

    companion object {
        
        private val client: OkHttpClient = OkHttpClient.Builder().build()
        fun create(): NetworkListServer {

            val retrofit = Retrofit.Builder().addCallAdapterFactory(MyCallAdapterFactory())
                .addConverterFactory(SimpleXmlConverterFactory.create()// important part!
                ).baseUrl(BASE_URL__LIST_SERVER).client(client).build()

            return retrofit.create(NetworkListServer::class.java)
        }
    }
}