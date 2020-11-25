package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Owner
import mx.itesm.thegoldenbook.models.Pagina
import mx.itesm.thegoldenbook.repositories.FirebaseRepository
import mx.itesm.thegoldenbook.utils.Constants
import mx.itesm.thegoldenbook.utils.Utils
import java.io.ByteArrayOutputStream
import java.io.InputStream


class AgregarPaginaActivity: AppCompatActivity() {
    private lateinit var ivPaginaImagen: ImageView
    private lateinit var edtDescripcion: EditText
    private lateinit var fab: ExtendedFloatingActionButton

    private lateinit var currentUser: Owner
    private lateinit var bitmap: Bitmap
    private var imagenValida = false

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

        currentUser = Settings.getCurrentUser()!!

        ivPaginaImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

            startActivityForResult(intent, Constants.REQUEST_IMAGE_GALLERY)
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

                    FirebaseRepository.instance.insert(currentUser.uid, Pagina(
                        paginaId,
                        albumId,
                        description,
                        rutaImagen,
                        fechaCreacion
                    ), object : ItemListener<Pagina?> {
                        override fun onItemSelected(model: Pagina?) {
                            if (model != null) {
                                uploadImage(model)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            return
        }

        if (requestCode != Constants.REQUEST_IMAGE_GALLERY) {
            return
        }

        if(data == null) {
            return
        }

        val imageUri: Uri = data.data ?: return

        handleResult(imageUri)
    }

    private fun handleResult(imageUri: Uri) {
        try {
            val inputStream: InputStream = contentResolver.openInputStream(imageUri) ?: return
            bitmap = BitmapFactory.decodeStream(inputStream)

            Glide.with(this).load(bitmap).into(ivPaginaImagen)
            imagenValida = true
        } catch (ex: Exception) {
            imagenValida = false
            Utils.print("Error al cargar imagen: $ex")
        }
    }

    private fun uploadImage(pagina: Pagina) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()

        FirebaseRepository.instance.uploadImage(pagina.paginaId, byteArray, object: ItemListener<Boolean> {
            override fun onItemSelected(model: Boolean) {
                if(model) {
                    pagina.rutaImagen = pagina.paginaId
                    updatePagina(pagina)
                    bitmap.recycle()
                } else {
                    FirebaseRepository.instance.delete(currentUser.uid, pagina)
                }
            }
        })
    }

    private fun updatePagina(pagina: Pagina) {
        FirebaseRepository.instance.update(currentUser.uid, pagina, object: ItemListener<Boolean> {
            override fun onItemSelected(model: Boolean) {
                if(model) {
                    finish()
                }
            }
        })
    }
}