package com.example.chat

//@IgnoreExtraProperties
//data class UserInformation (
//
//     var nickName : String? = "",
//     var lastName : String? = ""
//) {
//     @Exclude
//     fun toMap(): Map<String, Any?> {
//          return mapOf(
//               "firstName" to firstName,
//               "lastName" to lastName
//          )
//     }
//}

 class Message {
     var message: String? = null
     var sender: User? = null
     var createdAt: Long = 0
}

class User  {
     var nickName: String? = null

     init {
          this.nickName = nickName
     }
}





