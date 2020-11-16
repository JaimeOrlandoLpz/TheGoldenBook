package mx.itesm.thegoldenbook.repositories

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import mx.itesm.thegoldenbook.models.Album
import mx.itesm.thegoldenbook.models.Owner
import mx.itesm.thegoldenbook.utils.Constants

class FirebaseRepository private constructor() {
    companion object {
        var instance: FirebaseRepository = FirebaseRepository()
    }

    fun insert(owner: Owner) {
        val databaseReference: DatabaseReference
        val firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference

        databaseReference
            .child(Constants.RefUsers)
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

    fun insert(album: Album) {
        val databaseReference: DatabaseReference
        val firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference

        databaseReference
            .child(Constants.RefUsers)
            .child(album.ownerId)
            .child(Constants.RefAlbums)
            .child(album.albumId.toString())
            .setValue(album)
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
}