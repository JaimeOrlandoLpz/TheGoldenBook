package mx.itesm.thegoldenbook.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.annotation.NonNull
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Album
import mx.itesm.thegoldenbook.utils.Utils

class DeleteAlbumDialog private constructor(context: Context, private val listener: ItemListener<Album>): Dialog(context) {
    private lateinit var btnAceptar: Button
    private lateinit var btnCancelar: Button
    private lateinit var album: Album

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_delete)

        btnAceptar = findViewById(R.id.btnAceptar)
        btnCancelar = findViewById(R.id.btnCancelar)

        btnAceptar.setOnClickListener {
            cancel()
            listener.onItemSelected(album)
        }

        btnCancelar.setOnClickListener {
            cancel()
        }
    }

    fun setModel(album: Album) {
        this.album = album
    }

    companion object {
        class Builder(@NonNull context: Context, @NonNull listener: ItemListener<Album>) {
            private val dialog = DeleteAlbumDialog(context, listener)

            fun create(album: Album) {
                dialog.setModel(album)

                if(dialog.isShowing) {
                    Utils.print("El dialogo ya se está mostrando")
                } else {
                    dialog.show()
                }
            }
        }
    }
}