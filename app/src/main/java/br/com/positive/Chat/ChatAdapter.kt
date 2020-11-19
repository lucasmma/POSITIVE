package br.com.positive.Chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import br.com.positive.Model.ChatUser
import br.com.positive.R


class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view){
    var usermessage = view.findViewById<TextView>(R.id.messageuser)
    val time = view.findViewById<TextView>(R.id.time)
}
class ChatReceivedViewHolder(view:View) : RecyclerView.ViewHolder(view){
    val messagereceived = view.findViewById<TextView>(R.id.usermessagereceived)
    val timereceived = view.findViewById<TextView>(R.id.timereceived)
}

class ChatAdapter(var chat: ArrayList<ChatUser>, private val idusuario : Long) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context
    val TAG = "ChatAdapter"
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val view: View
//        println(chat.last().de.toString() + " : " + idusuario)
//        println("Numero: " + p1)

        context = p0.context
        return if (p1 == 1) {
            view = LayoutInflater.from(p0.context).inflate(R.layout.adaptercellusermessagesending, p0, false)
            ChatViewHolder(view)
        } else{
            view = LayoutInflater.from(p0.context).inflate(R.layout.adaptercellusermessagereceiving, p0, false)
            ChatReceivedViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int) : Int{
        return if (chat[position].de == idusuario) {
            1
        } else {
            0
        }
    }

    override fun getItemCount(): Int {
        return if (chat.isNotEmpty()) chat.size else 0
    }


    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val item = chat[p1]
        Log.d(TAG, p1.toString() + " " + item.mensagem)

        if (p0.itemViewType == 1){
            val p2 = p0 as ChatViewHolder
            p2.usermessage.text = item.mensagem
            p2.time.text = convertToDate(item.data)
        }
        else{
            val p2 = p0 as ChatReceivedViewHolder
            p2.messagereceived.text = item.mensagem
            p2.timereceived.text = convertToDate(item.data)
        }
    }
    fun appendData(user: ChatUser){
        chat.add(user)
        notifyDataSetChanged()
    }
    //adicionar um loaddata q recarregue apenas o ultimo adapter

    fun loadNewData(newData: ArrayList<ChatUser>){
        chat = newData
        notifyDataSetChanged()
    }

    private fun convertToDate(timestamp: Long) : String{
        val date = Date(timestamp * 1000)
        return SimpleDateFormat("dd/MM/yyyy|HH:mm", Locale.getDefault()).format(date)
    }

    fun getArray() : ArrayList<ChatUser>{
        return chat
    }



}

