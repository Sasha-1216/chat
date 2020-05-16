package com.example.chat

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserInformation (

     var firstName : String? = "",
     var lastName : String? = ""
) {
     @Exclude
     fun toMap(): Map<String, Any?> {
          return mapOf(
               "firstName" to firstName,
               "lastName" to lastName
          )
     }
}