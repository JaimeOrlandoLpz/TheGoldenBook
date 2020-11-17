package mx.itesm.thegoldenbook.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.NonNull
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Album

class AlbumDialog private constructor(context: Context, private val listener: ItemListener<Album>): Dialog(context) {
    private lateinit var tvTituloAlbum: TextView
    private lateinit var edtTituloAlbum: EditText
    private lateinit var btnAceptar: Button
    private lateinit var btnCancelar: Button
    private var model: Album? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_album)

        tvTituloAlbum = findViewById(R.id.tvTituloAlbum)
        edtTituloAlbum = findViewById(R.id.edtTituloAlbum)
        btnAceptar = findViewById(R.id.btnAceptar)
        btnCancelar = findViewById(R.id.btnCancelar)

        btnAceptar.setOnClickListener {
            val titulo = edtTituloAlbum.text.toString()

            if(titulo.isEmpty()) {
                edtTituloAlbum.error = "El campo no debe estar vacío"
            } else {
                cancel()

                if(this.model != null) {
                    val item = model!!

                    val album = Album(
                        item.albumId,
                        item.ownerId,
                        titulo,
                        item.rutaPortada,
                        System.currentTimeMillis()
                    )

                    cancel()
                    listener.onItemSelected(album)
                } else {
                    val album = Album(-1, "", titulo, "", System.currentTimeMillis())

                    cancel()
                    listener.onItemSelected(album)
                }
            }
        }

        btnCancelar.setOnClickListener {
            cancel()
        }
    }

    fun setModel(model: Album?) {
        this.model = model
    }

    override fun show() {
        super.show()

        if(model != null) {
            edtTituloAlbum.setText(model!!.titulo)
            tvTituloAlbum.text = "Editar album"
        } else {
            edtTituloAlbum.setText("")
            tvTituloAlbum.text = "Nuevo album"
        }
    }

    companion object {
        class Builder(@NonNull context: Context, @NonNull listener: ItemListener<Album>) {
            private val dialog = AlbumDialog(context, listener)

            fun create(album: Album?) {
                dialog.setModel(album)

                if(dialog.isShowing) {
                    Log.d("Jaime", "El dialogo ya se está mostrando")
                } else {
                    dialog.show()
                }
            }
        }
    }
}