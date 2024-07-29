package com.example.threadpractice.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadpractice.model.UserModel
import com.example.threadpractice.util.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AuthViewModel : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    private val db = FirebaseDatabase.getInstance()
   private val userRef = db.getReference("users")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: LiveData<FirebaseUser?> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _firebaseUser.value = currentUser
    }

    fun login(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _firebaseUser.postValue(auth.currentUser)
                getData(auth.currentUser!!.uid, context)
                _error.postValue("You have successfully login")
            } else {
                _error.postValue(task.exception?.message)
            }
        }
    }

    private fun getData(uid: String, context: Context) {


        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                SharedPref.storeData(
                    name = userData!!.name,
                    userName = userData.userName,
                    email = userData.email,
                    bio = userData.bio,
                    imageUri = userData.imageUrl,
                    context = context
                )
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun register(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri,
        context: Context
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _firebaseUser.postValue(firebaseUser.value)
                saveImage(
                    email = email,
                    password = password,
                    name = name,
                    bio = bio,
                    userName = userName,
                    imageUri = imageUri,
                    uid = auth.currentUser?.uid,
                    context = context
                )
            } else {
                _error.postValue(task.exception?.message)
            }
        }
    }


    private fun saveImage(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri?,
        uid: String?,
        context: Context
    ) {

        val uploadTask = imageRef.putFile(imageUri!!)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                saveData(
                    email = email,
                    password = password,
                    name = name,
                    bio = bio,
                    userName = userName,
                    toString = uri.toString(),
                    uid = uid,
                    context = context
                )
            }
        }


    }

    private fun saveData(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        toString: String,
        uid: String?,
        context: Context
    ) {
        val firestoreDb = Firebase.firestore
        val followersRef = firestoreDb.collection("followers").document(uid!!)
        val followingRef = firestoreDb.collection("following").document(uid)

        followingRef.set(mapOf("followingIds" to listOf<String>()))
        followersRef.set(mapOf("followerIds" to listOf<String>()))

        val userData = UserModel(
            email = email,
            password = password,
            name = name,
            bio = bio,
            userName = userName,
            imageUrl = toString,
            uid = uid
        )

        userRef.child(uid).setValue(userData).addOnSuccessListener {
            SharedPref.storeData(
                name = name,
                userName = userName,
                email = email,
                bio = bio,
                imageUri = toString,
                context = context
            )
        }.addOnFailureListener {}


    }


    fun updatdeProfile(
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri?,
        context: Context
    ) {
        val uid = auth.currentUser?.uid ?: return

        val userUpdate = userRef.child(uid)

        // Update profile picture if provided
        imageUri?.let { uri ->
            val newImageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")
            val uploadTask = newImageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                newImageRef.downloadUrl.addOnSuccessListener { newUri ->
                    val userData = mapOf(
                        "name" to name,
                        "bio" to bio,
                        "userName" to userName,
                        "imageUrl" to newUri.toString()
                    )
                    userUpdate.updateChildren(userData).addOnSuccessListener {
                        SharedPref.storeData(
                            name = name,
                            userName = userName,
                            email = SharedPref.getEmail(context), // Assuming SharedPref has method to get email
                            bio = bio,
                            imageUri = newUri.toString(),
                            context = context
                        )
                    }.addOnFailureListener { _error.postValue(it.message) }
                }
            }.addOnFailureListener { _error.postValue(it.message) }
        } ?: run {
            // If no new image, just update text fields
            val userData = mapOf(
                "name" to name,
                "bio" to bio,
                "userName" to userName
            )
            userUpdate.updateChildren(userData).addOnSuccessListener {
                SharedPref.storeData(
                    name = name,
                    userName = userName,
                    email = SharedPref.getEmail(context), // Assuming SharedPref has method to get email
                    bio = bio,
                    imageUri = SharedPref.getImageUrl(context), // Assuming SharedPref has method to get image URL
                    context = context
                )
            }.addOnFailureListener { _error.postValue(it.message) }
        }
    }

    fun updateProfile(
        name: String? = null,
        bio: String? = null,
        userName: String? = null,
        imageUri: Uri? = null,
        context: Context
    ) {
        val uid = auth.currentUser?.uid ?: return
        val userUpdate = userRef.child(uid)

        // Create a map to hold updates
        val updates = mutableMapOf<String, Any?>()

        // Only add fields that are not null or empty
        if (!name.isNullOrEmpty()) {
            updates["name"] = name
        }
        if (!bio.isNullOrEmpty()) {
            updates["bio"] = bio
        }
        if (!userName.isNullOrEmpty()) {
            updates["userName"] = userName
        }

        // Handle image update
        if (imageUri != null) {
            val newImageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")
            val uploadTask = newImageRef.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                newImageRef.downloadUrl.addOnSuccessListener { newUri ->
                    updates["imageUrl"] = newUri.toString()
                    applyUpdates(userUpdate, updates, context)
                }
            }.addOnFailureListener { exception ->
                _error.postValue(exception.message)
            }
        } else {
            // If no new image, just update text fields
            applyUpdates(userUpdate, updates, context)
        }
    }

    private fun applyUpdates(
        userUpdate: DatabaseReference,
        updates: Map<String, Any?>,
        context: Context
    ) {
        if (updates.isNotEmpty()) {
            userUpdate.updateChildren(updates).addOnSuccessListener {
                // Update SharedPref with new values
                SharedPref.storeData(
                    name = updates["name"] as? String ?: SharedPref.getName(context),
                    userName = updates["userName"] as? String ?: SharedPref.getUserName(context),
                    email = SharedPref.getEmail(context),
                    bio = updates["bio"] as? String ?: SharedPref.getBio(context),
                    imageUri = updates["imageUrl"] as? String ?: SharedPref.getImageUrl(context),
                    context = context
                )
                Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { exception ->
                _error.postValue(exception.message)
            }
        }
    }





    fun logout() {
        auth.signOut()
        _firebaseUser.postValue(null)
    }

}










