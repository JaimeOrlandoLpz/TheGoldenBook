package mx.itesm.thegoldenbook.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_galeria_libros.*
import mx.itesm.thegoldenbook.ui.adapters.GalleryAdapter
import mx.itesm.thegoldenbook.R


class GaleriaLibros : AppCompatActivity() {

   val adapter = GalleryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_galeria_libros)
        super.onCreate(savedInstanceState)
        rv.adapter = adapter
    }


}