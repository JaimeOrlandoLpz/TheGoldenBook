package mx.itesm.Legacy.TheGoldenBook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_galeria_libros.*


class GaleriaLibros : AppCompatActivity() {

   val adapter = GalleryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_galeria_libros)
        super.onCreate(savedInstanceState)
        rv.adapter = adapter
    }


}