package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
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

        adapter = AlbumsAdapter(this, object: ItemListener<Album> {
            override fun onItemSelected(view: View, model: Album) {
                if(view.id == R.id.btnVer || view.id == R.id.cvContainer) {
                    val albumId = model.albumId

                    val intent = Intent()
                    intent.putExtra(Constants.ParamAlbumId, albumId)
                    intent.setClass(this@GaleriaLibrosActivity, VisualizarEsteLibroActivity::class.java)
                    startActivity(intent)
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

        if(currentUser == null) {
            return
        }

        FirebaseRepository.instance.getAlbumList(currentUser.uid, object: ItemListener<MutableList<Album>> {
            override fun onItemSelected(model: MutableList<Album>) {
                adapter.setList(model)
            }
        })
    }
}