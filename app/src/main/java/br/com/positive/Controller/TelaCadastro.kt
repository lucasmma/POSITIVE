package br.com.positive.Controller

import android.os.Bundle
import android.widget.EditText
import br.com.positive.BaseActivity
import br.com.positive.Model.User
import br.com.positive.R
import br.com.positive.RestConection.UserRestHandler
import kotlinx.android.synthetic.main.activitytelacadastro.*

class TelaCadastro : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitytelacadastro)


        loading(false)
        voltar.setOnClickListener { voltar() }

        cadastrar.setOnClickListener { cadastrar() }
    }

    private fun cadastrar(){

        val isFilled = checkIfScreenIsFilled()
        if (isFilled.first){
            alert("Erro", isFilled.second!!)
            return
        }

        loading(true)
        val user = User(null,emailcadastro.text.toString(),nomecadastro.text.toString(),sobrenomecadastro.text.toString(),senhacadastro.text.toString(), null)
        UserRestHandler.cadastrarUsuario(user,
            onSucess = {newuser ->
                runOnUiThread {
                    loading(false)
                    newuser.mensagem
                    when (newuser.mensagem) {
                        "cadastro.sucesso" -> {
                            alert("Cadastro","Sucesso ao cadastrar, volte e realize o login novamente para entrar no aplicativo")
                        }
                        "usuario.existente" -> {
                            alert("Erro", "Esse e-mail já está sendo utilizado!")
                        }
                        "falhou" -> {
                            alert("Erro", "Falha ao realizar a operação")
                        }
                        else -> {
                            alert("Erro", "Internal Server Error")
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

    private fun getArrayWithAvisos(): ArrayList<Pair<EditText, String>> {
        val array = ArrayList<Pair<EditText, String>>()
        array.add(Pair(nomecadastro, getString(R.string.preenchaonomecorretamente)))
        array.add(Pair(sobrenomecadastro, getString(R.string.preenchaosobrenomecorretamente)))
        array.add(Pair(senhacadastro, getString(R.string.preenchasuasenha)))
        array.add(Pair(senhaconfirmacadastro, getString(R.string.confirmesuasenha)))
        return array
    }

    private fun checkIfScreenIsFilled(): Pair<Boolean, String?> {
        //check email
        if (!validateEmailFormat(emailcadastro.text.toString())){
            return Pair(true, "Preencha com um email valido")
        }

        val array = getArrayWithAvisos()
        for (i in array){
            if (i.first.text.isEmpty()){
                return Pair(true, i.second)
            }
        }


        if (senhacadastro.text.toString() != senhaconfirmacadastro.text.toString()){
            return Pair(true, "As senhas devem ser iguais")
        }


        return Pair(false, null)
    }

}
