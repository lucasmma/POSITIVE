package br.com.positive

import com.google.gson.GsonBuilder
import java.lang.reflect.Type

object StaticFunctions {
    fun toJson(any: Any) : String{
        return GsonBuilder().create().toJson(any)
    }

    fun fromJson(json : String , any : Type) : Any{
        return GsonBuilder().create().fromJson(json, any)
    }
}