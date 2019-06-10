package com.ahei.chatclient

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ahei.chatclient.model.Message
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.slf4j.LoggerFactory

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnSend: ImageButton
    private lateinit var etMsg: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val msgList: ArrayList<Message> = ArrayList()
    private val adapter = ListMessageAdapter(this, msgList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_main)
        btnSend = findViewById(R.id.btnSend)
        etMsg = findViewById(R.id.etMsg)
        recyclerView = findViewById(R.id.rvChat)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter

        btnSend.setOnClickListener(this)
        if (Client.channel == null) {
            Thread(Runnable { Client.run() }).start()
        }
    }

    override fun onClick(v: View?) {
        if(Client.channel?.isRegistered!!){
            var msg = etMsg.text.toString().trim()
            if (msg.isNotEmpty()) {
                etMsg.setText("")
                msg += "\r\n"
                Client.channel?.writeAndFlush(msg)
            }
        }else{
            Toast.makeText(this, "Connection lost", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        linearLayoutManager.scrollToPosition(msgList.size - 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleMessage(event: ChatMessageEvent) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showMessage(event: Message) {
        msgList.add(event)
        adapter.notifyDataSetChanged()
        linearLayoutManager.scrollToPosition(msgList.size - 1)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateOnlineNumber(number: Integer){
        this.title = number.toString()
    }
}

class ListMessageAdapter(context: Context, msgList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val VIEW_TYPE_USER_MSG = 0
    val VIEW_TYPE_OTHER_MSG = 1
    val context = context
    private val msgList = msgList

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (msgList[position].SenderId == UID) VIEW_TYPE_USER_MSG else VIEW_TYPE_OTHER_MSG
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_USER_MSG) {
            val view = LayoutInflater.from(context).inflate(R.layout.rv_item_user, parent, false)
            return UserHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.rv_item_other, parent, false)
            return OtherHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserHolder) {
            holder.tvName.text = msgList[position].SenderId
            holder.tvMsg.text = msgList[position].msg
        } else if (holder is OtherHolder) {
            holder.tvName.text = msgList[position].SenderId
            holder.tvMsg.text = msgList[position].msg
        }
    }

}

class UserHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvName = view.findViewById<TextView>(R.id.rviNameUser)
    val tvMsg = view.findViewById<TextView>(R.id.rviMsgUser)
}

class OtherHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvName = view.findViewById<TextView>(R.id.rviNameOther)
    val tvMsg = view.findViewById<TextView>(R.id.rviMsgOther)
}
