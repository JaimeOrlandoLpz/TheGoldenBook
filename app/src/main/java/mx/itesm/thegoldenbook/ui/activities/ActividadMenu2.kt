package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.models.Owner

class ActividadMenu2 : AppCompatActivity() {
    private lateinit var tvUserName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu2)

        tvUserName = findViewById(R.id.tvUserName)

        val currentUser: Owner = Settings.getCurrentUser()
        val text = "Hola, " + currentUser.nombre + " " + currentUser.apellidoP
        tvUserName.text = text
    }

    fun crearNuevoClicked(view: View){
        val intentCrearNuevo = Intent(this, CrearLibro::class.java).apply {

        }

        startActivity(intentCrearNuevo)
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