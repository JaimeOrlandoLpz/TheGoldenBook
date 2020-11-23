package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Album
import mx.itesm.thegoldenbook.models.Owner
import mx.itesm.thegoldenbook.repositories.FirebaseRepository
import mx.itesm.thegoldenbook.ui.dialogs.AlbumDialog
import mx.itesm.thegoldenbook.utils.Utils

class ActividadMenu2 : AppCompatActivity() {
    private lateinit var tvUserName: TextView
    private lateinit var btnCrearLibro: Button
    private lateinit var btnGaleria: Button
    private lateinit var btnPerfil: Button
    private lateinit var btnLogout: Button
    private lateinit var dialogAlbum: AlbumDialog.Companion.Builder
    //private var count = -0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu2)

        tvUserName = findViewById(R.id.tvUserName)
        btnCrearLibro = findViewById(R.id.btnCrearLibro)
        btnGaleria = findViewById(R.id.btnGaleria)
        btnPerfil = findViewById(R.id.btnPerfil)
        btnLogout = findViewById(R.id.btnLogout)

        val currentUser: Owner? = Settings.getCurrentUser()

        dialogAlbum = AlbumDialog.Companion.Builder(this, object: ItemListener<Album> {
            override fun onItemSelected(model: Album) {
                Utils.print("Album titulo: " + model.titulo)
                FirebaseRepository.instance.insert(currentUser!!.uid, model)
            }
        })

        /*
        FirebaseRepository.instance.getCount(currentUser, object: ItemListener<Long> {
            override fun onItemSelected(model: Long) {
                //count = model
            }
        })
        */

        btnCrearLibro.setOnClickListener {
            dialogAlbum.create(null)
        }

        btnGaleria.setOnClickListener {
            startActivity(Intent(this, GaleriaLibrosActivity::class.java))
        }

        btnPerfil.setOnClickListener {
            startActivity(Intent(this, InfoUsuario::class.java))
        }

        btnLogout.setOnClickListener {
            logout()
        }
    }

    override fun onResume() {
        super.onResume()
        mostrarNombre()
    }

    fun visualizar() {
        val intent = Intent(this, VisualizarLibros::class.java)
        startActivity(intent)
    }

    fun ayudaClicked() {
        val intent = Intent(this, Ayuda::class.java)
        startActivity(intent)
    }

    fun mostrarNombre() {
        val currentUser = Settings.getCurrentUser()
        if(currentUser != null) {
            FirebaseRepository.instance.getUser(currentUser.uid, object: ItemListener<Owner> {
                override fun onItemSelected(model: Owner) {
                    Utils.print("User uid ${model.uid}")
                    Utils.print("User nombre ${model.nombre}")
                    Utils.print("User email ${model.email}")
                    Utils.print("User fotoPerfil ${model.fotoPerfil}")
                    Utils.print("User fechaNacimiento ${model.fechaNacimiento}")

                    Settings.setCurrentUser(model)
                    Settings.setLogged(true)

                    val text = if(model.nombre.isEmpty()) {
                        "Hola usuario"
                    } else {
                        "Hola, " + model.nombre
                    }

                    tvUserName.text = text
                }
            })
        } else {
            logout()
        }
    }

    private fun logout() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        LoginManager.getInstance().logOut()

        Settings.setLogged(false)
        Settings.setCurrentUser(null)

        startActivity(Intent(this, ActividadLogin::class.java))
        finish()
    }
}