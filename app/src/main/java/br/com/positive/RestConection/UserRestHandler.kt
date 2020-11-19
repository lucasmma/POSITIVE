package br.com.positive.RestConection

import android.util.Log
import br.com.positive.Model.User
import br.com.positive.StaticFunctions
import br.com.positive.StaticInfos.StaticInformations
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException

object UserRestHandler : CustomRestHandler() {
    val TAG_inner = "USER REST HANDLER"
    fun cadastrarUsuario(user : User, onError : (Int) -> Unit, onSucess : (User) -> Unit){

        val url = StaticInformations.URL + "usuario"
        val body = StaticFunctions.toJson(user)

        customPost(body , url, onError = {code ->
            onError(code)
        }, onSucess = { response ->
            Log.d(TAG_inner, response)
            try {
                val newuser = GsonBuilder().create().fromJson(response, User::class.java)
                onSucess(newuser)
            } catch (e: IOException){
                e.printStackTrace()
                onError(0)
            }
        })

    }

    fun loginUsuario(user : User, onError : (Int) -> Unit, onSucess : (User) -> Unit){

        val url = StaticInformations.URL + "usuario/login"
        val body = StaticFunctions.toJson(user)

        customPost(body , url, onError = {code ->
            onError(code)
        }, onSucess = { response ->
            Log.d(TAG_inner, response)
            try {
                val newuser = GsonBuilder().create().fromJson(response, User::class.java)
                onSucess(newuser)
            } catch (e: IOException){
                e.printStackTrace()
                onError(0)
            }
        })

    }
}