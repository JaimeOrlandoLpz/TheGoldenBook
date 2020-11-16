package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Album
import mx.itesm.thegoldenbook.models.Owner
import mx.itesm.thegoldenbook.ui.dialogs.AlbumDialog

class ActividadMenu2 : AppCompatActivity() {
    private lateinit var tvUserName: TextView
    private lateinit var btnCrearLibro: Button
    private lateinit var dialogAlbum: AlbumDialog.Companion.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu2)

        tvUserName = findViewById(R.id.tvUserName)
        btnCrearLibro = findViewById(R.id.btnCrearLibro)

        dialogAlbum = AlbumDialog.Companion.Builder(this, object: ItemListener<Album> {
            override fun onItemSelected(model: Album) {
                Log.d("Jaime", "Album titulo: " + model.titulo)
            }
        })

        btnCrearLibro.setOnClickListener {
            dialogAlbum.create(null)
        }

        val currentUser: Owner = Settings.getCurrentUser()
        val text = "Hola, " + currentUser.nombre + " " + currentUser.apellidoP
        tvUserName.text = text
    }

    fun galeriaClicked(view: View){
        val intentGaleria = Intent(this, AlbumsActivity::class.java).apply {

        }

        startActivity(intentGaleria)
    }

    fun visualizarClicked(view: View){
        val intentVisualizar = Intent(this, VisualizarLibros::class.java).apply {

        }
        startActivity(intentVisualizar)
    }

    fun usuarioClicked(view: View){
        val intentUsuario = Intent(this, InfoUsuario::class.java).apply {

        }
        startActivity(intentUsuario)
    }

    fun ayudaClicked(view:View){
        val intentAyuda = Intent(this, Ayuda::class.java).apply {

        }
        startActivity(intentAyuda)
    }
}