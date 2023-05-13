package com.aamirashraf.mychatapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
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
import androidx.core.app.ActivityCompat
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

class ChatActivity : AppCompatActivity() ,MessageAdapter.ItemClickListner{
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox:EditText
    private lateinit var sendButton:ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList:ArrayList<Message>
    private lateinit var auth:FirebaseAuth
    private lateinit var mdbRef:DatabaseReference
    var receiverRoom:String?=null
    var senderRoom:String?=null
    var latti=28.54
    var longi=77.20

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
        messageAdapter= MessageAdapter(this,messageList,this)
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
            val intent=Intent(this@ChatActivity,ShowCurrentLocationActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
        if(item.itemId==R.id.share_your_location){
//
            val senderUid=FirebaseAuth.getInstance().currentUser?.uid
//            val message=messageBox.text.toString()
            val imgId=R.drawable.ic_action_sharelocation
//
            Toast.makeText(this@ChatActivity,"location shared successfully",Toast.LENGTH_LONG).show()

//
//            messageList.add(Message("",senderUid,imgId,true))
            val messageObject=Message("",senderUid,imgId,true)
            mdbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mdbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageAdapter.notifyDataSetChanged()

            return true
        }
//
        return true
    }

    override fun locationClick() {
//        val lat=intent.getDoubleExtra("lat",28.54)
//        val lon=intent.getDoubleExtra("lon",77.20)
//        val intent=Intent(this,MapboxActivity::class.java)
//        startActivity(intent)
//        finish()

        getCurrentLocation()
        Intent(this,MapboxActivity::class.java).also {
            it.putExtra("lat",latti)
            it.putExtra("lon",longi)
            startActivity(it)
        }
    }
    private fun getCurrentLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // get the current location coordinates
                 latti = location.latitude
                longi = location.longitude

                // do something with the location coordinates
            }

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
//            getCurrentLocation()
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
    }


}