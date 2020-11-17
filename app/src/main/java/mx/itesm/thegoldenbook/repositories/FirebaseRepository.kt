package mx.itesm.thegoldenbook.repositories

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
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
        databaseReference = firebaseDatabase.reference.child(Constants.RefUsers).child(owner.uid)
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    Log.d("Jaime", "Usuario ${owner.uid} existe")
                } else {
                    databaseReference.setValue(owner)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Jaime", "Eror insert owner: $error")
            }
        })
    }

    fun registrar(context: Context, owner: Owner) {
        val databaseReference: DatabaseReference
        val firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child(Constants.RefUsers).child(owner.uid)
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    Toast.makeText(context, "La cuenta ya existe, se iniciará sesión", Toast.LENGTH_SHORT).show()
                } else {
                    databaseReference.removeEventListener(this)
                    databaseReference.setValue(owner)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Jaime", "Eror insert owner: $error")
            }
        })
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