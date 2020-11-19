package br.com.positive.Controller

import android.content.Intent
import android.os.Bundle
import br.com.positive.BaseActivity
import br.com.positive.R
import br.com.positive.StaticInfos.StaticInformations
import kotlinx.android.synthetic.main.activitytelamenu.*

class TelaMenu : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitytelamenu)


        ouvinte.setOnClickListener { goToChat("OUVINTE") }

        paciente.setOnClickListener { goToChat("PACIENTE") }

        sair.setOnClickListener { quitApp() }

        setUpVersionCode(version)

        println(getUser())

    }

    private fun goToChat(usertype: String) {
        val intent = Intent(this@TelaMenu, TelaChat::class.java)
        intent.putExtra(StaticInformations.KEY, usertype)
        startActivity(intent)
    }
}