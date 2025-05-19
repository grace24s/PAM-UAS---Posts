package org.delcom.ifs22022_pam_uas.data.model

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val name: String, val email: String, val password: String)
data class AuthResponse(val accessToken: String, val user: User)
data class User(val id: Int, val name: String, val email: String)
