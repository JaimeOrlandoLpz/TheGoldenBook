package mx.itesm.thegoldenbook.repositories

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import mx.itesm.thegoldenbook.models.Owner

class UsersRepository private constructor() {
    companion object {
        var instance: UsersRepository = UsersRepository()
    }

    fun insert(owner: Owner) {
        val databaseReference: DatabaseReference
        val firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference

        databaseReference
            .child("Users")
            .child(owner.uid)
            .setValue(owner)
            .addOnCompleteListener {
                Log.d("Jaime", "Correcto")
            }.addOnSuccessListener {
                Log.d("Jaime", "OnSuccess")
            }.addOnFailureListener {
                Log.d("Jaime", it.toString())
            }.addOnCanceledListener {
                Log.d("Jaime", "OnCancel")
            }
    }

    fun update(owner: Owner) {

    }

    fun delete(owner: Owner) {

    }
}