package br.com.positive.Model

enum class Status{
    DESCONECTANDO, CHAT, NOVO_CHAT
}

class ChatUser(val de : Long, val para : Long, val data: Long, val mensagem : String, val status : Status, val idChat : Long) {

}
