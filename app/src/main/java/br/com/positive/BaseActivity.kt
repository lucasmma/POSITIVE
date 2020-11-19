package br.com.positive

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.positive.Controller.TelaLogin
import br.com.positive.Controller.TelaMenu
import br.com.positive.Model.User
import br.com.positive.StaticInfos.StaticInformations
import com.google.gson.GsonBuilder
import com.squareup.picasso.BuildConfig
import kotlinx.android.synthetic.main.loadingactivity.*
import java.text.SimpleDateFormat
import java.util.*

open class BaseActivity : AppCompatActivity() {
    fun setUpVersionCode(textView: TextView){
        textView.text = "POSITIVE - Versão v${BuildConfig.VERSION_NAME}"
    }

    fun getCurrentTimeStamp():Long{
        return System.currentTimeMillis() / 1000
    }

    fun quitApp() {
        //apagar da memoria o usuario e a ultima atividade
//        getSharedPreferences(StaticInformations.NAME, Context.MODE_PRIVATE).edit().clear().apply()
//        getSharedPreferences(StaticInformations.CHECAR, Context.MODE_PRIVATE).edit().clear().apply()
        startActivity(Intent(this, TelaLogin::class.java))
    }

    fun validateEmailFormat(email : String) : Boolean {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true
        }
        return false
    }


    fun getCurrentDate() : String{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }


    fun loading(visible : Boolean){
        progressbarloading.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun voltar(){
        this.finish()
    }

    fun alert(title : String = "Error", message : String){
        val builder = AlertDialog.Builder(this@BaseActivity)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton("OK") { dialog, which -> dialog?.cancel() }
        builder.show()
    }

    fun getUser() : User{
        val userjson = getSharedPreferences(StaticInformations.NAME, 0).getString(StaticInformations.KEY, null)

        println("UserJson " + userjson)

        if (userjson != null){
            return GsonBuilder().create().fromJson(userjson, User::class.java)
        }
        else{
            alert("Erro", "Usuario deslogado")
            quitApp()
        }

        return User(0)


    }
}