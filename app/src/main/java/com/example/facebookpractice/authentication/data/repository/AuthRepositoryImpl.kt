package com.example.facebookpractice.authentication.data.repository

import android.util.Log
import com.example.facebookpractice.authentication.domain.repository.AuthRepository
import com.example.facebookpractice.util.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore

) : AuthRepository {

    private var operationSuccessful: Boolean = false

    override suspend fun loginUser(email: String, password: String): Flow<Resource<Boolean>> =
        flow {

            operationSuccessful = false
            try {
                emit(Resource.Loading())
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    operationSuccessful = true
                }.await()
                emit(Resource.Success(operationSuccessful))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "Error in the sign in"))
            }

        }

    override suspend fun registerUser(
        email: String,
        password: String,
        name: String,
        username: String,
        surname: String
    ): Flow<Resource<Boolean>> = flow {
        operationSuccessful = false
        try {
            emit(Resource.Loading())
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                operationSuccessful = true
            }.await()
            if (operationSuccessful) {
                val userid = firebaseAuth.currentUser?.uid!!
                val obj = com.example.facebookpractice.authentication.data.User(
                    username = username,
                    name = name,
                    surname = surname,
                    userId = userid,
                    password = password,
                    email = email
                )
                firestore.collection("users").document(userid).set(obj)
                    .addOnSuccessListener {
                        Log.d("TAG", "firebaseSignUp: success $userid")
                    }.await()
                emit(Resource.Success(operationSuccessful))
            } else {
                emit(Resource.Success(operationSuccessful))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error in the sign up"))
        }
    }


    override suspend fun signOutUser(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            firebaseAuth.signOut()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error in the SignOut"))
        }
    }


    override suspend fun googleSignIn(credential: AuthCredential): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithCredential(credential).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }
}
