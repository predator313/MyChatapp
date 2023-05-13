package com.aamirashraf.mychatapp

class Message {
    var message:String?=null
    var senderId:String?=null
    var imgId:Int?=null
    var isLocation:Boolean?=null

    constructor()
    constructor(message:String?,senderId:String?,imgId:Int?,isLocation:Boolean=false){
        this.message=message
        this.senderId=senderId
        this.imgId=imgId
        this.isLocation=isLocation
    }

}