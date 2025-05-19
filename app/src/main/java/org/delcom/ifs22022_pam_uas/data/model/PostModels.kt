package org.delcom.ifs22022_pam_uas.data.model

data class Post(
    val id: Int,
    val caption: String,
    val imageUrl: String,
    val user: User
)

data class UpdatePostRequest(val caption: String)
