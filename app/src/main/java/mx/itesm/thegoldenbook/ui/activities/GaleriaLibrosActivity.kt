package mx.itesm.thegoldenbook.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.interfaces.ChildListener
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Album
import mx.itesm.thegoldenbook.repositories.FirebaseRepository
import mx.itesm.thegoldenbook.ui.adapters.AlbumsAdapter
import mx.itesm.thegoldenbook.ui.dialogs.AlbumDialog
import mx.itesm.thegoldenbook.utils.Utils

class GaleriaLibrosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlbumsAdapter
    private lateinit var dialogAlbum: AlbumDialog.Companion.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_galeria_libros)

        val currentUser = Settings.getCurrentUser()

        dialogAlbum = AlbumDialog.Companion.Builder(this, object: ItemListener<Album> {
            override fun onItemSelected(model: Album) {
                Utils.print("Actualizar id: " + model.albumId)
                FirebaseRepository.instance.insert(model)
            }
        })

        adapter = AlbumsAdapter(object: ItemListener<Album> {
            override fun onItemSelected(view: View, model: Album) {
                if(view.id == R.id.btnVer || view.id == R.id.cvContainer) {
                    Toast.makeText(applicationContext, "Ver ${model.titulo}", Toast.LENGTH_SHORT).show()
                } else if(view.id == R.id.btnEditar) {
                    dialogAlbum.create(model)
                } else if(view.id == R.id.btnBorrar) {
                    Toast.makeText(applicationContext, "Borrar ${model.titulo}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        FirebaseRepository.instance.getAlbums(this, currentUser, object: ChildListener<Album> {
            override fun onChildAdded(position: Int, model: Album) {
                adapter.add(position, model)
            }

            override fun onChildChanged(position: Int, model: Album) {
                adapter.changed(position, model)
            }

            override fun onChildRemoved(position: Int, model: Album) {
                adapter.remove(position)
            }

            override fun onChildMoved(model: Album) {
                Utils.print("onChildMoved ${model.titulo}")
            }
        })
    }
}