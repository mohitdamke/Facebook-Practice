package com.example.facebookpractice.authentication.domain.repository

import com.example.facebookpractice.util.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginUser(email: String, password: String): Flow<Resource<Boolean>>
    suspend fun registerUser(
        email: String,
        password: String,
        name: String,
        username: String,
        surname: String
    ): Flow<Resource<Boolean>>

    suspend fun signOutUser(): Flow<Resource<Boolean>>
    suspend fun googleSignIn(credential: AuthCredential): Flow<Resource<AuthResult>>


}