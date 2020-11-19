package br.com.positive.Model

class User(val id : Long?, val email : String, val nome : String?, val sobrenome : String?, val senha : String, val mensagem : String?) {
    constructor(id: Long?) : this(id, "", null, null, "", null)
}