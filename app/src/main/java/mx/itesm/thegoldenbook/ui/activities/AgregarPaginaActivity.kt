package mx.itesm.thegoldenbook.ui.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Pagina
import mx.itesm.thegoldenbook.repositories.FirebaseRepository
import mx.itesm.thegoldenbook.utils.Constants
import mx.itesm.thegoldenbook.utils.Utils

class AgregarPaginaActivity: AppCompatActivity() {
    private lateinit var ivPaginaImagen: ImageView
    private lateinit var edtDescripcion: EditText
    private lateinit var fab: ExtendedFloatingActionButton
    private var imagenValida = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_pagina)

        val intent = intent
        val bundle = intent.extras

        if(bundle == null) {
            finish()
            return
        }

        val albumId = bundle.getString(Constants.ParamAlbumId)

        if(albumId == null) {
            finish()
            return
        }

        ivPaginaImagen = findViewById(R.id.ivPaginaImagen)
        edtDescripcion = findViewById(R.id.edtDescripcion)
        fab = findViewById(R.id.fab)

        val account = Settings.getCurrentUser()

        if(account == null) {
            finish()
            return
        }

        ivPaginaImagen.setOnClickListener {
            Utils.print("Cargar imagen")
        }

        fab.setOnClickListener {
            val description = edtDescripcion.text.toString()

            if(description.isEmpty()) {
                edtDescripcion.error = "La descripción no puede estar vacía"
            } else {
                if(imagenValida) {
                    val paginaId = "paginaId"
                    val rutaImagen = "rutaImagen"
                    val fechaCreacion = System.currentTimeMillis()

                    FirebaseRepository.instance.insert(account.uid, Pagina(paginaId, albumId, description, rutaImagen, fechaCreacion), object: ItemListener<Boolean> {
                        override fun onItemSelected(model: Boolean) {
                            if(model) {
                                finish()
                            } else {
                                Toast.makeText(
                                    this@AgregarPaginaActivity,
                                    "Ocurrió un error al agregar la página",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
                } else {
                    Toast.makeText(
                        this@AgregarPaginaActivity,
                        "Debes cargar una imagen valida",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}