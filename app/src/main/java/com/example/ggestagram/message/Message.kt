package com.example.ggestagram.message

data class Message(
    var message: String?,
    var sendId: String?
){
    constructor():this("","")
}