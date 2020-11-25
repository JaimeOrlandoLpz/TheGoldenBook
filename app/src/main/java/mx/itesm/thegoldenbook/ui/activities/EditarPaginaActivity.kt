package mx.itesm.thegoldenbook.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Owner
import mx.itesm.thegoldenbook.models.Pagina
import mx.itesm.thegoldenbook.repositories.FirebaseRepository
import mx.itesm.thegoldenbook.utils.Constants
import mx.itesm.thegoldenbook.utils.Utils

class EditarPaginaActivity: AppCompatActivity() {
    private lateinit var ivPaginaImagen: ImageView
    private lateinit var edtDescripcion: EditText
    private lateinit var fab: ExtendedFloatingActionButton

    private lateinit var currentUser: Owner

    private var imagenValida = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_pagina)

        val intent = intent
        val bundle = intent.extras

        if(bundle == null) {
            finish()
            return
        }

        val albumId = bundle.getString(Constants.ParamAlbumId)
        val paginaId = bundle.getString(Constants.ParamPaginaId)

        if(albumId == null) {
            finish()
            return
        }

        if(paginaId == null) {
            finish()
            return
        }

        ivPaginaImagen = findViewById(R.id.ivPaginaImagen)
        edtDescripcion = findViewById(R.id.edtDescripcion)
        fab = findViewById(R.id.fab)

        ivPaginaImagen.visibility = View.GONE
        edtDescripcion.visibility = View.GONE
        fab.visibility = View.GONE

        currentUser = Settings.getCurrentUser()!!

        FirebaseRepository.instance.getPagina(currentUser.uid, albumId, paginaId, object: ItemListener<Pagina> {
            override fun onItemSelected(model: Pagina) {
                setPagina(model)
            }
        })
    }

    private fun setPagina(pagina: Pagina) {
        ivPaginaImagen.visibility = View.VISIBLE
        edtDescripcion.visibility = View.VISIBLE
        fab.visibility = View.VISIBLE

        edtDescripcion.setText(pagina.texto)

        ivPaginaImagen.setOnClickListener {
            Utils.print("Cargar imagen")
        }

        fab.setOnClickListener {
            val description = edtDescripcion.text.toString()

            if(description.isEmpty()) {
                edtDescripcion.error = "La descripción no puede estar vacía"
            } else {
                if(imagenValida) {
                    FirebaseRepository.instance.update(
                        currentUser.uid,
                        Pagina(
                            pagina.paginaId,
                            pagina.albumId,
                            description,
                            pagina.rutaImagen,
                            pagina.fechaCreacion
                        ),
                        object: ItemListener<Boolean> {
                            override fun onItemSelected(model: Boolean) {
                                if(model) {
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@EditarPaginaActivity,
                                        "Ocurrió un error al editar la página",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        })
                } else {
                    Toast.makeText(
                        this@EditarPaginaActivity,
                        "Debes cargar una imagen valida",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}