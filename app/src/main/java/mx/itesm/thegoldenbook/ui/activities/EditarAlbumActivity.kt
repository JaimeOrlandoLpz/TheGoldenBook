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
import com.google.firebase.storage.FirebaseStorage
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Owner
import mx.itesm.thegoldenbook.models.Album
import mx.itesm.thegoldenbook.repositories.FirebaseRepository
import mx.itesm.thegoldenbook.utils.Constants
import java.io.ByteArrayOutputStream
import java.io.InputStream

class EditarAlbumActivity: AppCompatActivity() {
    private lateinit var ivAlbumImagen: ImageView
    private lateinit var edtDescripcion: EditText
    private lateinit var fab: ExtendedFloatingActionButton

    private lateinit var currentUser: Owner
    private var albumId: String = ""
    private var cargaPendiente: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_album)

        val intent = intent
        val bundle = intent.extras

        if(bundle == null) {
            finish()
            return
        }

        albumId = bundle.getString(Constants.ParamAlbumId)!!

        ivAlbumImagen = findViewById(R.id.ivAlbumImagen)
        edtDescripcion = findViewById(R.id.edtDescripcion)
        fab = findViewById(R.id.fab)

        ivAlbumImagen.visibility = View.GONE
        edtDescripcion.visibility = View.GONE
        fab.visibility = View.GONE

        currentUser = Settings.getCurrentUser()!!

        FirebaseRepository.instance.getAlbum(currentUser.uid, albumId, object: ItemListener<Album> {
            override fun onItemSelected(model: Album) {
                setAlbum(model)
            }
        })
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
            cargaPendiente = true

            val inputStream: InputStream = contentResolver.openInputStream(imageUri) ?: return
            val bitmap = BitmapFactory.decodeStream(inputStream)

            Glide.with(applicationContext)
                .load(bitmap)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivAlbumImagen)

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()

            FirebaseRepository.instance.deleteImage(albumId, object: ItemListener<Boolean> {
                override fun onItemSelected(model: Boolean) {
                    if(model) {
                        uploadImage(byteArray)
                    } else {
                        showError()
                    }
                }
            })
        } catch (ex: Exception) {
            showError()
        }
    }

    private fun uploadImage(byteArray: ByteArray) {
        FirebaseRepository.instance.uploadImage(albumId, byteArray, object: ItemListener<Boolean> {
            override fun onItemSelected(model: Boolean) {
                if(model) {
                    Toast.makeText(
                        this@EditarAlbumActivity,
                        "La imagen se actualizo correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showError()
                }

                cargaPendiente = false
            }
        })
    }

    private fun setAlbum(album: Album) {
        ivAlbumImagen.visibility = View.VISIBLE
        edtDescripcion.visibility = View.VISIBLE
        fab.visibility = View.VISIBLE

        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.getReferenceFromUrl(Constants.BUCKET + album.rutaPortada)

        Glide.with(applicationContext)
            .load(gsReference)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(ivAlbumImagen)

        edtDescripcion.setText(album.titulo)

        ivAlbumImagen.setOnClickListener {
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
                FirebaseRepository.instance.update(
                    Album(
                        album.albumId,
                        album.ownerId,
                        description,
                        album.rutaPortada,
                        album.fechaCreacion
                    ), object: ItemListener<Boolean> {
                        override fun onItemSelected(model: Boolean) {
                            if(model) {
                                this@EditarAlbumActivity.onBackPressed()
                            } else {
                                Toast.makeText(
                                    this@EditarAlbumActivity,
                                    "Ocurrió un error al editar la página",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
            }
        }
    }

    override fun onBackPressed() {
        if(cargaPendiente) {
            Toast.makeText(
                this@EditarAlbumActivity,
                "Espere a que la imagen termine de cargar",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            super.onBackPressed()
        }
    }

    private fun showError() {
        cargaPendiente = false

        Toast.makeText(
            this@EditarAlbumActivity,
            "Ocurrió un error al actualizar la imagen, intentelo de nuevo",
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}