package com.example.chat

class Contact {
    val nickname: String
    val email: String
    lateinit var uid: String
    lateinit var chatId: String

    constructor(
    nickname: String,
    email: String) {
        this.nickname = nickname
        this.email = email
    }

    constructor(
        nickname: String,
        email: String,
        uid: String
    ) {
        this.nickname = nickname
        this.email = email
        this.uid = uid
    }

    constructor(
        nickname: String,
        email: String,
        uid: String,
        chatId: String
    ) {
        this.nickname = nickname
        this.email = email
        this.uid = uid
        this.chatId  = chatId
    }

    constructor(){
        this.nickname = ""
        this.email = ""
        this.uid = ""
        this.chatId = ""
    }
}