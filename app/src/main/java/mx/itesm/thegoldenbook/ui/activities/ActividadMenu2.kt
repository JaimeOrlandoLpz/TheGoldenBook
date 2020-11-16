package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Album
import mx.itesm.thegoldenbook.models.Owner
import mx.itesm.thegoldenbook.repositories.FirebaseRepository
import mx.itesm.thegoldenbook.ui.dialogs.AlbumDialog
import mx.itesm.thegoldenbook.utils.Constants

class ActividadMenu2 : AppCompatActivity() {
    private lateinit var tvUserName: TextView
    private lateinit var btnCrearLibro: Button
    private lateinit var dialogAlbum: AlbumDialog.Companion.Builder
    private lateinit var databaseReference: DatabaseReference
    private var count = -0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu2)

        tvUserName = findViewById(R.id.tvUserName)
        btnCrearLibro = findViewById(R.id.btnCrearLibro)

        val currentUser: Owner = Settings.getCurrentUser()

        dialogAlbum = AlbumDialog.Companion.Builder(this, object: ItemListener<Album> {
            override fun onItemSelected(model: Album) {
                Log.d("Jaime", "Album titulo: " + model.titulo)

                val album = Album(count, currentUser.uid, model.titulo, model.rutaPortada, model.fechaCreacion)
                FirebaseRepository.instance.insert(album)
            }
        })

        databaseReference = FirebaseDatabase.getInstance().reference.child(Constants.RefUsers).child(currentUser.uid).child(Constants.RefAlbums)
        Log.d("Jaime", databaseReference.toString())
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    count = snapshot.childrenCount
                    Log.d("Jaime", "Count: $count and ${snapshot.key}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Jaime", "Error $error")
            }
        })

        btnCrearLibro.setOnClickListener {
            dialogAlbum.create(null)
        }

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