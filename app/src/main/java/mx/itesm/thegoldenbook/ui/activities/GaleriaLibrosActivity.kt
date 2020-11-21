package mx.itesm.thegoldenbook.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.interfaces.ChildListener
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Album
import mx.itesm.thegoldenbook.repositories.FirebaseRepository
import mx.itesm.thegoldenbook.ui.adapters.AlbumsAdapter
import mx.itesm.thegoldenbook.ui.dialogs.AlbumDialog
import mx.itesm.thegoldenbook.ui.dialogs.DeleteAlbumDialog
import mx.itesm.thegoldenbook.utils.Constants
import mx.itesm.thegoldenbook.utils.Utils

class GaleriaLibrosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlbumsAdapter
    private lateinit var dialogAlbum: AlbumDialog.Companion.Builder
    private lateinit var dialogDeleteAlbum: DeleteAlbumDialog.Companion.Builder
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_galeria_libros)

        val currentUser = Settings.getCurrentUser()

        dialogAlbum = AlbumDialog.Companion.Builder(this, object: ItemListener<Album> {
            override fun onItemSelected(model: Album) {
                Utils.print("Actualizar id: " + model.albumId)
                FirebaseRepository.instance.update(model)
            }
        })

        dialogDeleteAlbum = DeleteAlbumDialog.Companion.Builder(this, object: ItemListener<Album> {
            override fun onItemSelected(model: Album) {
                FirebaseRepository.instance.delete(model)
            }
        })

        adapter = AlbumsAdapter(object: ItemListener<Album> {
            override fun onItemSelected(view: View, model: Album) {
                if(view.id == R.id.btnVer || view.id == R.id.cvContainer) {
                    Toast.makeText(applicationContext, "Ver ${model.titulo}", Toast.LENGTH_SHORT).show()
                } else if(view.id == R.id.btnEditar) {
                    dialogAlbum.create(model)
                } else if(view.id == R.id.btnBorrar) {
                    dialogDeleteAlbum.create(model)
                }
            }
        })

        recyclerView = findViewById(R.id.recyclerView)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        FirebaseRepository.instance.getAlbums(this, currentUser, object: ChildListener<Album> {
            override fun onChildAdded(model: Album) {
                adapter.add(model)
            }

            override fun onChildChanged(model: Album) {
                adapter.changed(model)
            }

            override fun onChildRemoved(id: String) {
                Utils.print("onChildRemoved 2 $id")
                adapter.remove(id)
            }

            override fun onChildMoved(model: Album) {
                Utils.print("onChildMoved ${model.titulo}")
            }
        })

        if(currentUser == null) {
            return
        }

        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child(Constants.RefUsers).child(currentUser.uid).child(Constants.RefAlbums)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val count = dataSnapshot.childrenCount
                    Utils.print("Count: $count and ${dataSnapshot.key}")

                    val list: MutableList<Album> = ArrayList()
                    for(snapshot in dataSnapshot.children) {
                        Utils.print("Item Key ${snapshot.key}")
                        val album: Album? = snapshot.getValue(Album::class.java)
                        if(album != null) {
                            list.add(album)
                        }
                    }

                    adapter.setList(list)
                } else {
                    adapter.setList(ArrayList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Utils.print("Error $error")
            }
        })
    }
}