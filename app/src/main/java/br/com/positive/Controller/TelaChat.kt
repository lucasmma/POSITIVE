package br.com.positive.Controller

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.positive.BaseActivity
import br.com.positive.Chat.ChatAdapter
import br.com.positive.Model.ChatUser
import br.com.positive.Model.Status
import br.com.positive.R
import br.com.positive.StaticFunctions
import br.com.positive.StaticInfos.StaticInformations
import kotlinx.android.synthetic.main.activitytelachat.*
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class TelaChat : BaseActivity(){
    val TAG_inner = "ChatActivity"
    var client = OkHttpClient()
    var ws : WebSocket? = null
    private val listener = EchoWebSocketListener()
    private var listaDeMensagens = ArrayList<ChatUser>()
    var layoutManager = LinearLayoutManager(this)
    lateinit var chatadapter : ChatAdapter
    var IDUSUARIO : Long = 0
    var IDCOMUNICADOR : Long = 0
    var IDCHAT : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IDUSUARIO = getUser().id!!

        setContentView(R.layout.activitytelachat)

        chatadapter = ChatAdapter(listaDeMensagens, getUser().id!!)

        chatrecycler.adapter = chatadapter
        chatrecycler.layoutManager = layoutManager

        enviar.setOnClickListener {
            if (!mensagem.text.isEmpty()){
                //criar mensagem, enviar, adicionar no chat e rolar a barra
                val chatUser = ChatUser(IDUSUARIO, IDCOMUNICADOR, getCurrentTimeStamp(), mensagem.text.toString(), Status.CHAT, IDCHAT)

                sendMessage(chatUser)

                mensagem.text.clear()
            }
        }

        voltar.setOnClickListener {
            ws?.close(200, "Disconected")
            finish()
        }

        proximo.setOnClickListener {
            proximaConversa()
        }

        start()
    }

    private fun proximaConversa() {
        val chatUser = ChatUser(IDUSUARIO, IDCOMUNICADOR, getCurrentTimeStamp(), "", Status.NOVO_CHAT, IDCHAT)
        sendMessage(chatUser)

        loading(true)
        loadConversa(ArrayList())
        IDCOMUNICADOR = 0
        IDCHAT = 0
    }

    fun getType() : String{
        val tipo =  intent.getStringExtra(StaticInformations.KEY)
        return if (tipo != null){
            tipo
        } else{
            alert("Erro", "Erro ao se conectar no chat")
            finish()
            ""
        }
    }

    private fun start(){
        client = OkHttpClient.Builder()
            .pingInterval(30, TimeUnit.SECONDS)
            .build()
        val user = getUser()
        val request =  Request.Builder().url( StaticInformations.URLCHAT + "usuario/${user.id}/tipo/${getType()}").build()
        ws = client.newWebSocket(request!!, listener)
        Log.i(TAG_inner,"Start the ChatApplication")
        client.dispatcher.executorService.shutdown()
    }

    private fun sendMessage(message: ChatUser) {
        val json = StaticFunctions.toJson(message)
        Log.d(TAG_inner, json)
        if (ws == null){
            start()
        }
        ws?.send(json)
    }

    fun updateLista(message : ChatUser){
        chatadapter.appendData(message)
        listaDeMensagens = chatadapter.getArray()
        layoutManager.scrollToPosition(listaDeMensagens.size-1)
    }

    fun loadConversa(newarray : ArrayList<ChatUser>){
        chatadapter.loadNewData(newarray)
        listaDeMensagens = newarray
    }

    private inner class EchoWebSocketListener : WebSocketListener(){
        override fun onOpen(webSocket: WebSocket, response: Response) {
            //nao precisa mandar o nome do usuario
            println("OnOpen")

        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            t.printStackTrace()
            println("OnFailure")
            super.onFailure(webSocket, t, response)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, null)
            println("Closed")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            println("Message Received")

            Log.d(TAG_inner,text)

            runOnUiThread {
                val messageReceived = StaticFunctions.fromJson(text, ChatUser::class.java) as ChatUser

                if (messageReceived.status == Status.CHAT){
                    if (IDCOMUNICADOR == 0.toLong() || IDCHAT == 0.toLong()){
                        loading(false)
                        IDCOMUNICADOR = messageReceived.de
                        IDCHAT = messageReceived.idChat
                    }
                    updateLista(messageReceived)

                } else if(messageReceived.status == Status.DESCONECTANDO){
                    IDCOMUNICADOR = 0
                    loading(true)
                    loadConversa(ArrayList())
                }
            }


            println("Saindo do onMessage")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            println("Message de bytes")
            super.onMessage(webSocket, bytes)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            println("Closed")
            super.onClosed(webSocket, code, reason)
        }

    }
}
