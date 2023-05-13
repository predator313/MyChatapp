package com.aamirashraf.mychatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context:Context, val messageList:ArrayList<Message>, private val clickListner: ItemClickListner): RecyclerView.Adapter<ViewHolder>() {
    val ITEM_SEND=1
    val ITEM_RECEIVE=2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType==1){
            val view:View=LayoutInflater.from(context).inflate(R.layout.recive,parent,false)
            return ReceiveViewHolder(view)
        }
        else{
            val view:View=LayoutInflater.from(context).inflate(R.layout.sende,parent,false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMessage=messageList[position]
       if(holder.javaClass==SentViewHolder::class.java){
           //sent view holder
           val viewHolder=holder as SentViewHolder
           viewHolder.sentMessage.text=currentMessage.message
           if (currentMessage.isLocation==true){
               viewHolder.sendImg.visibility=View.VISIBLE
               viewHolder.sentMessage.visibility=View.GONE
               viewHolder.rlHello2.setBackgroundResource(0)
           }
           viewHolder.sendImg.setOnClickListener {
               clickListner.locationClick()
           }
       }
        else{
            //receive view holder
            val viewHolder=holder as ReceiveViewHolder
           viewHolder.receiveMessage.text=currentMessage.message
           if (currentMessage.isLocation==true){
               viewHolder.reciveImg.visibility=View.VISIBLE
               viewHolder.receiveMessage.visibility=View.GONE
               viewHolder.rlHello1.setBackgroundResource(0)

           }
           viewHolder.reciveImg.setOnClickListener {
               clickListner.locationClick()
           }
       }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage=messageList[position]
         if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SEND
        } else{
            return ITEM_RECEIVE
        }
    }
    override fun getItemCount(): Int {
        return messageList.size
    }
    class SentViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val sentMessage=itemView.findViewById<TextView>(R.id.txt_send_message)
        val sendImg=itemView.findViewById<ImageView>(R.id.red_marker_location_sender)
        val rlHello2=itemView.findViewById<RelativeLayout>(R.id.hello2)
    }
    class ReceiveViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val receiveMessage=itemView.findViewById<TextView>(R.id.txt_recive_message)
        val reciveImg=itemView.findViewById<ImageView>(R.id.red_marker_location_reciver)
        val rlHello1=itemView.findViewById<RelativeLayout>(R.id.hello)
    }
    interface ItemClickListner{
        fun locationClick()
    }

}