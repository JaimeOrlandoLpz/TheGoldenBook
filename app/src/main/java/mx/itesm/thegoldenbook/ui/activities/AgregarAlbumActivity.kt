package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Owner
import mx.itesm.thegoldenbook.models.Album
import mx.itesm.thegoldenbook.repositories.FirebaseRepository
import mx.itesm.thegoldenbook.utils.Constants
import mx.itesm.thegoldenbook.utils.Utils
import java.io.ByteArrayOutputStream
import java.io.InputStream


class AgregarAlbumActivity: AppCompatActivity() {
    private lateinit var ivAlbumImagen: ImageView
    private lateinit var edtDescripcion: EditText
    private lateinit var fab: ExtendedFloatingActionButton

    private lateinit var currentUser: Owner
    private lateinit var bitmap: Bitmap
    private var imagenValida = false
    private var cargandoImagen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_album)

        ivAlbumImagen = findViewById(R.id.ivAlbumImagen)
        edtDescripcion = findViewById(R.id.edtDescripcion)
        fab = findViewById(R.id.fab)

        currentUser = Settings.getCurrentUser()!!

        ivAlbumImagen.setOnClickListener {
            if(cargandoImagen) {
                Toast.makeText(this@AgregarAlbumActivity, "Espere un momento por favor", Toast.LENGTH_SHORT).show()
            } else {
                cargandoImagen = true

                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

                startActivityForResult(intent, Constants.REQUEST_IMAGE_GALLERY)
            }
        }

        fab.setOnClickListener {
            val description = edtDescripcion.text.toString()

            if(description.isEmpty()) {
                edtDescripcion.error = "La descripción no puede estar vacía"
            } else {
                if(imagenValida) {
                    fab.visibility = View.GONE

                    FirebaseRepository.instance.insert(currentUser.uid, Album(
                        "albumId",
                        currentUser.uid,
                        description,
                        "rutaImagen",
                        System.currentTimeMillis()
                    ), object : ItemListener<Album?> {
                        override fun onItemSelected(model: Album?) {
                            if (model != null) {
                                uploadImage(model)
                            } else {
                                Toast.makeText(
                                    this@AgregarAlbumActivity,
                                    "Ocurrió un error al agregar la página",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
                } else {
                    Toast.makeText(
                        this@AgregarAlbumActivity,
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
            cargandoImagen = false
            return
        }

        if (requestCode != Constants.REQUEST_IMAGE_GALLERY) {
            cargandoImagen = false
            return
        }

        if(data == null) {
            cargandoImagen = false
            return
        }

        val imageUri: Uri = data.data ?: return

        handleResult(imageUri)
    }

    private fun handleResult(imageUri: Uri) {
        try {
            val inputStream: InputStream = contentResolver.openInputStream(imageUri) ?: return
            bitmap = BitmapFactory.decodeStream(inputStream)

            Glide.with(applicationContext)
                .load(bitmap)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivAlbumImagen)
            imagenValida = true
        } catch (ex: Exception) {
            imagenValida = false
            cargandoImagen = false
            Utils.print("Error al cargar imagen: $ex")
        }
    }

    private fun uploadImage(album: Album) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()

        FirebaseRepository.instance.uploadImage(album.albumId, byteArray, object: ItemListener<Boolean> {
            override fun onItemSelected(model: Boolean) {
                if(model) {
                    album.rutaPortada = album.albumId
                    updateAlbum(album)
                    bitmap.recycle()
                } else {
                    FirebaseRepository.instance.delete(album)
                }
            }
        })
    }

    private fun updateAlbum(album: Album) {
        FirebaseRepository.instance.update(album, object: ItemListener<Boolean> {
            override fun onItemSelected(model: Boolean) {
                if(model) {
                    finish()
                }
            }
        })
    }
}