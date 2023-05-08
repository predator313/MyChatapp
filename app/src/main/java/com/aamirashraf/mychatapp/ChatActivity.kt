package com.aamirashraf.mychatapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mapbox.maps.MapView
import java.io.ByteArrayOutputStream

class ChatActivity : AppCompatActivity() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox:EditText
    private lateinit var sendButton:ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList:ArrayList<Message>
    private lateinit var auth:FirebaseAuth
    private lateinit var mdbRef:DatabaseReference
    var receiverRoom:String?=null
    var senderRoom:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
//        val intent=Intent()
        auth= FirebaseAuth.getInstance()
        val name=intent.getStringExtra("name")
        val receiverUid=intent.getStringExtra("uid")
        val senderUid=FirebaseAuth.getInstance().currentUser?.uid
        mdbRef=FirebaseDatabase.getInstance().getReference()
        senderRoom=receiverUid+senderUid
        receiverRoom=senderUid+receiverUid
        supportActionBar?.title=name
        val imgId=R.drawable.ic_action_sharelocation
//        supportActionBar?.setIcon(com.mapbox.maps.R.drawable.mapbox_compass_icon)
        messageRecyclerView=findViewById(R.id.chatRecyclerView)
        messageBox=findViewById(R.id.messageBox)
        sendButton=findViewById(R.id.sendBtn)
        messageList= ArrayList()
        messageAdapter= MessageAdapter(this,messageList)
//        val btnlocation=findViewById<ImageView>(R.id.location_share)
        //logic for the recycler view
//        val imgbtn=findViewById<ImageButton>(R.id.location_share)
        messageRecyclerView.layoutManager=LinearLayoutManager(this)
        messageRecyclerView.adapter=messageAdapter
        mdbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postsnapshot in snapshot.children){
                        val message=postsnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        sendButton.setOnClickListener {
            //adding message to db
            val message=messageBox.text.toString()
            val messageObject=Message(message,senderUid,imgId)
            mdbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mdbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== R.id.logout){
            auth.signOut()
            val intent=Intent(this@ChatActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
        if(item.itemId==R.id.location){
            val mv=findViewById<MapView>(R.id.mapView)
            val intent=Intent(this@ChatActivity,MapboxActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
        if(item.itemId==R.id.share_your_location){
//            val mv=findViewById<MapView>(R.id.mapView)
//            val intent=Intent(this@ChatActivity,MapboxActivity::class.java)
//            startActivity(intent)
//            finish()
//            Toast.makeText(this,"hello",Toast.LENGTH_LONG).show()
            val senderUid=FirebaseAuth.getInstance().currentUser?.uid
//            val message=messageBox.text.toString()
            val imgId=R.drawable.ic_action_sharelocation
//            val btn_share_your_location=findViewById<ImageView>(R.id.share_location_imgview)
//            btn_share_your_location.isVisible=true
//            btn_share_your_location.setOnClickListener {
//                val intent=Intent(this@ChatActivity,MapboxActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
            Toast.makeText(this@ChatActivity,"location shared successfully",Toast.LENGTH_LONG).show()

//            val messageObject=Message("Location",senderUid,imgId)
//            mdbRef.child("chats").child(senderRoom!!).child("messages").push()
//                .setValue(messageObject).addOnSuccessListener {
//                    mdbRef.child("chats").child(receiverRoom!!).child("messages").push()
//                        .setValue(messageObject)
//                    mdbRef.child("chats").child(receiverRoom!!).child("imgId").push()
//                }
//            messageBox.setText("")
////            btn_share_your_location.isVisible=true
//            val imageView_sender=findViewById<ImageView>(R.id.red_marker_location_sender)
//            imageView_sender.visibility=View.VISIBLE

            return true
        }
//
        return true
    }

}