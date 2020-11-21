package mx.itesm.thegoldenbook.ui.activities

import android.os.Bundle
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
import mx.itesm.thegoldenbook.utils.Utils

class GaleriaLibros : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlbumsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_galeria_libros)

        val currentUser = Settings.getCurrentUser()

        adapter = AlbumsAdapter(object: ItemListener<Album> {
            override fun onItemSelected(model: Album) {

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