package mx.itesm.thegoldenbook.repositories

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.interfaces.ChildListener
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Album
import mx.itesm.thegoldenbook.models.Owner
import mx.itesm.thegoldenbook.utils.Constants
import mx.itesm.thegoldenbook.utils.Utils


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
                    Utils.print("Usuario ${owner.uid} existe")
                } else {
                    databaseReference.setValue(owner)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Utils.print("Eror insert owner: $error")
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
                Utils.print("Eror insert owner: $error")
            }
        })
    }

    fun insert(ownerId: String, titulo: String) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference: DatabaseReference = firebaseDatabase.reference
            .child(Constants.RefUsers)
            .child(ownerId)
            .child(Constants.RefAlbums)
            .push()

        val albumId = databaseReference.key

        if(albumId != null) {
            val rutaPortada = ""
            val fechaCreacion = System.currentTimeMillis()
            val album = Album(albumId, ownerId, titulo, rutaPortada, fechaCreacion)

            databaseReference.setValue(album)
        }
    }

    fun update(album: Album) {
        val databaseReference: DatabaseReference
        val firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference

        databaseReference
            .child(Constants.RefUsers)
            .child(album.ownerId)
            .child(Constants.RefAlbums)
            .child(album.albumId.toString())
            .updateChildren(album.toMap())
            .addOnCompleteListener {
                Utils.print("Correcto")
            }.addOnSuccessListener {
                Utils.print("OnSuccess")
            }.addOnFailureListener {
                Utils.print(it.toString())
            }.addOnCanceledListener {
                Utils.print("OnCancel")
            }
    }

    fun delete(album: Album) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.reference
            .child(Constants.RefUsers)
            .child(album.ownerId)
            .child(Constants.RefAlbums)
            .child(album.albumId)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    Utils.print("Remove ${snapshot.key}")
                    snapshot.ref.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Utils.print("onCancelled ${databaseError.toException()}")
            }
        })
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

    fun getAlbums(context: Context, owner: Owner?, listener: ChildListener<Album>) {
        if(owner == null) {
            return
        }

        val databaseReference: DatabaseReference
        val firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child(Constants.RefUsers).child(owner.uid).child(
            Constants.RefAlbums
        )

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val key = dataSnapshot.key
                Utils.print("onChildAdded: $key")

                val album: Album? = dataSnapshot.getValue(Album::class.java)

                if(key != null && album != null) {
                    Utils.print(album.titulo)
                    listener.onChildAdded(album)
                } else {
                    Utils.print("Album nulo ${dataSnapshot.key}")
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val key = dataSnapshot.key
                Utils.print("onChildChanged: $key")

                val album: Album? = dataSnapshot.getValue(Album::class.java)

                if(key != null && album != null) {
                    Utils.print(album.titulo)
                    listener.onChildChanged(album)
                } else {
                    Utils.print("Album nulo ${dataSnapshot.key}")
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val key = dataSnapshot.key
                Utils.print("onChildRemoved: $key")

                if(key != null) {
                    Utils.print("onChildRemoved 1 $key")
                    listener.onChildRemoved(key)
                } else {
                    Utils.print("Album nulo ${dataSnapshot.key}")
                }
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val key = dataSnapshot.key
                Utils.print("onChildMoved: $key")

                val album: Album? = dataSnapshot.getValue(Album::class.java)

                if(key != null && album != null) {
                    Utils.print(album.titulo)
                    listener.onChildMoved(album)
                } else {
                    Utils.print("Album nulo ${dataSnapshot.key}")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Utils.print("onCancelled ${databaseError.toException()}")
                Toast.makeText(context, "Fallo al cargar albums", Toast.LENGTH_SHORT).show()
            }
        }

        databaseReference.addChildEventListener(childEventListener)
    }

    fun getCount(currentUser: Owner?, listener: ItemListener<Long>) {
        if(currentUser == null) {
            return
        }

        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child(
            Constants.RefUsers
        ).child(currentUser.uid).child(Constants.RefAlbums)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val count = snapshot.childrenCount
                    Utils.print("Count: $count and ${snapshot.key}")
                    listener.onItemSelected(count)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Utils.print("Error $error")
            }
        })
    }
}