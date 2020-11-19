package br.com.positive.RestConection

import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

open class CustomRestHandler(){
    var TAG = "Custom Rest Handler"
    fun customGet(url : String, onError : (Int) -> Unit, onSucess : (String) -> Unit){
        val client = OkHttpClient.Builder().build()
        println("Get na URL : " + url)
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                response.body.use { responseBody ->
                    if (!response.isSuccessful){
                        onError(response.code)
                        println(response.body)
                    }

                    responseBody?.let {
                        val string = it.string()
                        Log.d("$TAG - CUSTOM GET", string)
                        try {
                            onSucess(string)
                        } catch (e: IOException){
                            e.printStackTrace()
                            println(string)
                        }
                    }
                }
            }
        })
    }

    fun customPost(body : String, url: String, onSucess: (String) -> Unit, onError: (Int) -> Unit){
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        println(body)
        val request = Request.Builder().url(url).addHeader("Content-Type","application/json; charset=utf-8").post(
            body.toRequestBody(JSON)
        ).build()

        val client = OkHttpClient().newBuilder().build()
        try{
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    response.body.use { responseBody ->
                        if (!response.isSuccessful){
                            onError(response.code)
                            throw IOException("Unexpected code ${response.body!!.string()}")
                        }

                        responseBody?.let {
                            val string = it.string()
                            Log.d("$TAG - CUSTOM POST", string)
                            try {
                                onSucess(string)
                            }catch (e: IOException){
                                e.printStackTrace()
                                onError(response.code)
                            }
                        }
                    }
                }
            })
        }
        catch (e : IOException){
            e.printStackTrace()
        }
    }
}