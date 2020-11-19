package br.com.positive.Controller

import android.content.Intent
import android.os.Bundle
import br.com.positive.BaseActivity
import br.com.positive.Model.User
import br.com.positive.R
import br.com.positive.RestConection.UserRestHandler
import br.com.positive.StaticInfos.StaticInformations
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activitytelalogin.*


class TelaLogin : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activitytelalogin)

        checarSeFoiLogado()


        fazercadastro.setOnClickListener { goToCadastro() }
        login.setOnClickListener {
            if (loginemail.text.isNotEmpty() && loginsenha.text.isNotEmpty()){
                loading(true)

                logar(loginemail.text.toString(), loginsenha.text.toString())
            }
            else{
                alert("Erro", "Preencha os dados corretamente")
            }
        }

    }

    private fun checarSeFoiLogado() {
        val isLoggedIn = getSharedPreferences(StaticInformations.CHECAR, 0)
        if (isLoggedIn.contains(StaticInformations.EMAIL) || isLoggedIn.contains(StaticInformations.SENHA)){
            loading(true)
            logar(isLoggedIn.getString(StaticInformations.EMAIL, null)!!, isLoggedIn.getString(StaticInformations.SENHA, null)!!)
        } else{
            loading(false)
        }
    }

    private fun logar(login: String, senha: String) {

        val user = User(null, login, null, null, senha, null)
        UserRestHandler.loginUsuario(user,
            onSucess = {newuser ->
                runOnUiThread {
                    loading(false)
                }
                when (newuser.mensagem) {
                    "login.sucesso" -> {

                        println(GsonBuilder().create().toJson(newuser))
                        getSharedPreferences(StaticInformations.CHECAR, 0).edit().putString(StaticInformations.EMAIL, loginemail.text.toString()).apply()
                        getSharedPreferences(StaticInformations.CHECAR, 0).edit().putString(StaticInformations.SENHA, loginsenha.text.toString()).apply()
                        getSharedPreferences(StaticInformations.NAME, 0).edit().putString(StaticInformations.KEY, GsonBuilder().create().toJson(newuser)).apply()
                        startActivity(Intent(this@TelaLogin, TelaMenu::class.java))
                    }
                    "usuario.inexistente" -> {
                        runOnUiThread{
                            alert("Erro ao Logar", "Usuario inexistente")
                        }
                    }
                    "senha.errada" -> {
                        runOnUiThread {
                            alert("Erro", "Senha Errada")
                        }
                    }
                }
            }, onError ={code ->
                runOnUiThread{
                    loading(false)
                    if (code == 500){
                        alert("ERRO", "Internal Server Error")
                    }
                    else{
                        println(code)
                    }
                }
            })
    }

    fun goToCadastro(){
        startActivity(Intent(this@TelaLogin, TelaCadastro::class.java))
    }
}