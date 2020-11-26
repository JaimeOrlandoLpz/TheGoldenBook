@file:Suppress("DEPRECATION")

package mx.itesm.thegoldenbook.ui.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Owner
import mx.itesm.thegoldenbook.repositories.FirebaseRepository
import mx.itesm.thegoldenbook.utils.Constants
import mx.itesm.thegoldenbook.utils.DatePickerFragment
import mx.itesm.thegoldenbook.utils.Utils
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class InfoUsuarioActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()

    private lateinit var ivPhoto: ImageView
    private lateinit var edtNombre: EditText
    private lateinit var edtEmail: EditText
    private lateinit var btnFechaNacimiento: Button
    private lateinit var tvFechaNacimiento: TextView
    private lateinit var btnActualizar: Button

    private lateinit var currentUser: Owner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_usuario)

        ivPhoto = findViewById(R.id.ivPhoto)
        edtNombre = findViewById(R.id.edtNombre)
        edtEmail = findViewById(R.id.edtEmail)
        btnFechaNacimiento = findViewById(R.id.btnFechaNacimiento)
        tvFechaNacimiento = findViewById(R.id.tvFechaNacimiento)
        btnActualizar = findViewById(R.id.btnActualizar)

        currentUser = Settings.getCurrentUser()!!

        edtNombre.setText(currentUser.nombre)
        edtEmail.setText(currentUser.email)
        tvFechaNacimiento.text = Utils.getFormattedDate(currentUser.fechaNacimiento)

        ivPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

            startActivityForResult(intent, Constants.REQUEST_IMAGE_GALLERY)
        }

        btnFechaNacimiento.setOnClickListener {
            getDatePicker()
        }

        btnActualizar.setOnClickListener {
            val nombre = edtNombre.text.toString()
            val email = edtEmail.text.toString()
            val fechaNacimiento = tvFechaNacimiento.text.toString()

            if(nombre.isEmpty() || email.isEmpty()) {
                if(nombre.isEmpty()) {
                    edtNombre.error = "El campo no debe estar vacío"
                }

                if(email.isEmpty()) {
                    edtEmail.error = "El campo no debe estar vacío"
                }
            } else {
                try {
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val parsedDate: Date = dateFormat.parse(fechaNacimiento)!!
                    val timestamp = Timestamp(parsedDate.time)

                    val owner = Owner(currentUser.uid, nombre, email, currentUser.fotoPerfil, timestamp.time)
                    Utils.print(owner.fechaNacimiento.toString())
                    FirebaseRepository.instance.update(this, owner)

                    auth.currentUser?.updateEmail(email)?.addOnSuccessListener {
                        Utils.print("Cambio de correo exitoso")
                    }?.addOnFailureListener {
                        Utils.print(it.toString())
                    }
                } catch (ex: Exception) {
                    Utils.print(ex.toString())
                }
            }
        }

        val thumbRequest: RequestBuilder<Drawable> = Glide.with(application.applicationContext).load(R.drawable.avatar)

        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.getReferenceFromUrl(Constants.BUCKET + currentUser.uid)

        Glide.with(applicationContext)
            .load(gsReference)
            .thumbnail(thumbRequest)
            .error(thumbRequest)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(ivPhoto)
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
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val thumbRequest: RequestBuilder<Drawable> = Glide.with(application.applicationContext).load(R.drawable.avatar)

            Glide.with(applicationContext)
                .load(bitmap)
                .thumbnail(thumbRequest)
                .error(thumbRequest)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivPhoto)

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()

            FirebaseRepository.instance.uploadImage(currentUser.uid, byteArray, object: ItemListener<Boolean> {
                override fun onItemSelected(model: Boolean) {
                    if(model) {
                        Toast.makeText(
                            this@InfoUsuarioActivity,
                            "La imagen se actualizo correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@InfoUsuarioActivity,
                            "Ocurrió un error al actualizar la imagen, intentelo de nuevo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } catch (ex: Exception) {
            Utils.print("Error al cargar imagen: $ex")
        }
    }

    private fun getDatePicker() {
        val newFragment = DatePickerFragment(object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                val selectedDate = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
                tvFechaNacimiento.text = selectedDate
            }
        })

        newFragment.show(supportFragmentManager, "datePicker")
    }
}