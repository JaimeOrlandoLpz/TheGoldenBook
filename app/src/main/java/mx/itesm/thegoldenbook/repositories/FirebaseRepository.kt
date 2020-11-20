package mx.itesm.thegoldenbook.repositories

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import mx.itesm.thegoldenbook.application.Settings
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
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
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

    fun update(context: Context, owner: Owner) {
        val databaseReference: DatabaseReference
        val firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child(Constants.RefUsers).child(owner.uid)
        databaseReference.updateChildren(owner.toMap()).addOnSuccessListener {
            Settings.setCurrentUser(owner)
            Toast.makeText(context, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
        }
    }

    fun registrar(context: Context, owner: Owner) {
        val databaseReference: DatabaseReference
        val firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child(Constants.RefUsers).child(owner.uid)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(
                        context,
                        "La cuenta ya existe, se iniciará sesión",
                        Toast.LENGTH_SHORT
                    ).show()
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

    fun uploadDefault(fileName: String, byteArray: ByteArray) {
        val storage = Firebase.storage
        // Create a storage reference from our app
        val storageRef: StorageReference = storage.reference

        // Create a reference to "mountains.jpg"
        val imageRef: StorageReference = storageRef.child(fileName)

        var uploadTask = imageRef.putBytes(byteArray)

        // Create a reference to 'images/mountains.jpg'
        //val mountainImagesRef: StorageReference = storageRef.child("images/mountains.jpg")
    }
}