package com.aamirashraf.mychatapp

class Message {
    var message:String?=null
    var senderId:String?=null
    var imgId:Int?=null
    constructor()
    constructor(message:String?,senderId:String?,imgId:Int?){
        this.message=message
        this.senderId=senderId
        this.imgId=imgId
    }

}