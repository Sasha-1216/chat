package com.example.chat

interface ChatCommunicator {
    fun passChatSessionInfo(chatId: String, recipientId: String, senderId: String)
}