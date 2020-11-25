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
import mx.itesm.thegoldenbook.models.Pagina
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

    fun insert(ownerId: String, album: Album) {
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
            val item = Album(albumId, ownerId, album.titulo, rutaPortada, fechaCreacion)

            databaseReference.setValue(item)
        }
    }

    fun insert(ownerId: String, pagina: Pagina, listener: ItemListener<Boolean>) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference: DatabaseReference = firebaseDatabase.reference
            .child(Constants.RefUsers)
            .child(ownerId)
            .child(Constants.RefAlbums)
            .child(pagina.albumId)
            .child(Constants.RefPages)
            .push()

        val paginaId = databaseReference.key

        if(paginaId != null) {
            val fechaCreacion = System.currentTimeMillis()
            val item = Pagina(paginaId, pagina.albumId, pagina.texto, pagina.rutaImagen, fechaCreacion)

            databaseReference.setValue(item).addOnSuccessListener {
                listener.onItemSelected(true)
            }.addOnFailureListener {
                listener.onItemSelected(false)
            }
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

    fun update(ownerId: String, pagina: Pagina, listener: ItemListener<Boolean>) {
        val databaseReference: DatabaseReference
        val firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference

        databaseReference
            .child(Constants.RefUsers)
            .child(ownerId)
            .child(Constants.RefAlbums)
            .child(pagina.albumId)
            .child(Constants.RefPages)
            .child(pagina.paginaId)
            .updateChildren(pagina.toMap())
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

    fun delete(ownerId: String, albumId: String, pagina: Pagina) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.reference
            .child(Constants.RefUsers)
            .child(ownerId)
            .child(Constants.RefAlbums)
            .child(albumId)
            .child(Constants.RefPages)
            .child(pagina.paginaId)

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

    fun getAlbumList(ownerId: String, listener: ItemListener<MutableList<Album>>) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
            .child(Constants.RefUsers)
            .child(ownerId)
            .child(Constants.RefAlbums)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val count = dataSnapshot.childrenCount
                    Utils.print("AlbumList Count: $count and ${dataSnapshot.key}")

                    val list: MutableList<Album> = ArrayList()
                    for(snapshot in dataSnapshot.children) {
                        Utils.print("Album Item Key ${snapshot.key}")
                        val album: Album? = snapshot.getValue(Album::class.java)
                        if(album != null) {
                            list.add(album)
                        }
                    }

                    listener.onItemSelected(list)
                } else {
                    listener.onItemSelected(ArrayList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Utils.print("Error $error")
            }
        })
    }

    fun getPaginasAlbum(context: Context, owner: Owner?, albumId: String, listener: ChildListener<Pagina>) {
        if(owner == null) {
            return
        }

        val databaseReference: DatabaseReference
        val firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference
            .child(Constants.RefUsers)
            .child(owner.uid)
            .child(Constants.RefAlbums)
            .child(albumId)
            .child(Constants.RefPages)

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val key = dataSnapshot.key
                Utils.print("onChildAdded: $key")

                val pagina: Pagina? = dataSnapshot.getValue(Pagina::class.java)

                if(key != null && pagina != null) {
                    Utils.print(pagina.paginaId)
                    listener.onChildAdded(pagina)
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val key = dataSnapshot.key
                Utils.print("onChildChanged: $key")

                val pagina: Pagina? = dataSnapshot.getValue(Pagina::class.java)

                if(key != null && pagina != null) {
                    Utils.print(pagina.paginaId)
                    listener.onChildChanged(pagina)
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val key = dataSnapshot.key
                Utils.print("onChildRemoved: $key")

                if(key != null) {
                    Utils.print("onChildRemoved 1 $key")
                    listener.onChildRemoved(key)
                } else {
                    Utils.print("Pagina nulo ${dataSnapshot.key}")
                }
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val key = dataSnapshot.key
                Utils.print("onChildMoved: $key")

                val pagina: Pagina? = dataSnapshot.getValue(Pagina::class.java)

                if(key != null && pagina != null) {
                    Utils.print(pagina.paginaId)
                    listener.onChildMoved(pagina)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Utils.print("onCancelled ${databaseError.toException()}")
                Toast.makeText(context, "Fallo al cargar albums", Toast.LENGTH_SHORT).show()
            }
        }

        databaseReference.addChildEventListener(childEventListener)
    }

    fun getPaginasList(ownerId: String, albumId: String, listener: ItemListener<MutableList<Pagina>>) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
            .child(Constants.RefUsers)
            .child(ownerId)
            .child(Constants.RefAlbums)
            .child(albumId)
            .child(Constants.RefPages)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val count = dataSnapshot.childrenCount
                    Utils.print("PaginasList Count: $count and ${dataSnapshot.key}")

                    val list: MutableList<Pagina> = ArrayList()
                    for(snapshot in dataSnapshot.children) {
                        Utils.print("Pagina Item Key ${snapshot.key}")
                        val pagina: Pagina? = snapshot.getValue(Pagina::class.java)
                        if(pagina != null) {
                            list.add(pagina)
                        }
                    }

                    listener.onItemSelected(list)
                } else {
                    listener.onItemSelected(ArrayList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Utils.print("Error $error")
            }
        })
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

    fun getUser(ownerId: String, listener: ItemListener<Owner>) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child(Constants.RefUsers).child(ownerId)
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    val owner = dataSnapshot.getValue(Owner::class.java)

                    if(owner != null) {
                        listener.onItemSelected(owner)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Utils.print("onCancelled: $error")
            }
        })
    }

    fun getPagina(ownerId: String, albumId: String, paginaId: String, listener: ItemListener<Pagina>) {
        val databaseReference = FirebaseDatabase.getInstance().reference
            .child(Constants.RefUsers)
            .child(ownerId)
            .child(Constants.RefAlbums)
            .child(albumId)
            .child(Constants.RefPages)
            .child(paginaId)
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    val pagina = dataSnapshot.getValue(Pagina::class.java)

                    if(pagina != null) {
                        listener.onItemSelected(pagina)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Utils.print("onCancelled: $error")
            }
        })
    }
}